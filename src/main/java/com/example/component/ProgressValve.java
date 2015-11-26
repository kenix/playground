/*
* Created at 00:24 on 26/11/15
*/
package com.example.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.springframework.util.FileCopyUtils;
import rx.Subscription;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * @author zzhao
 */
@Slf4j
public class ProgressValve extends ValveBase {

    public ProgressValve() {
        super(true); // async
        ProgressBeanPostProcessor.observe().subscribe(
                beanName -> log.info("<ProgressValve> bean found: {}", beanName),
                ex -> log.error("<ProgressValve> failed", ex),
                this::removeSelf
        );
    }

    private void removeSelf() {
        log.info("<removeSelf> application started, de-registering ...");
        getContainer().getPipeline().removeValve(this);
    }

    @Override
    public void invoke(Request req, Response resp) throws IOException, ServletException {
        switch (req.getRequestURI().substring(req.getContextPath().length())) {
            case "/init.stream":
                streamProgress(req.startAsync());
                break;
            case "/health":
            case "/info":
                resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                break;
            default:
                wireLoadingPage(resp);
        }
    }

    private void streamProgress(AsyncContext ctx) throws IOException {
        final ServletResponse resp = ctx.getResponse();
        // setup event streaming
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        resp.flushBuffer();
        // stream event
        final Subscription subscription = ProgressBeanPostProcessor
                .observe()
                .map(beanName -> "data: " + beanName)
                .subscribe(
                        event -> streamEvent(event, resp),
                        ex -> log.error("<streamProgress> ", ex),
                        () -> complete(ctx)
                );
        unsubscribeOnDisconnect(subscription, ctx);
    }

    private void unsubscribeOnDisconnect(final Subscription subscription, AsyncContext ctx) {
        ctx.addListener(new AsyncListenerMixin((et, event) -> {
            if (et != AsyncListenerMixin.EventType.Start) {
                subscription.unsubscribe();
            }
        }));
    }

    private void complete(AsyncContext ctx) {
        streamEvent("event: complete", ctx.getResponse()); // ??
        ctx.complete();
    }

    private void streamEvent(String event, ServletResponse resp) {
        try {
            final PrintWriter pw = resp.getWriter();
            pw.print(event);
            pw.println();
            pw.flush();
        } catch (IOException e) {
            log.error("<streamEvent> failed to stream {}", event, e);
        }
    }

    private void wireLoadingPage(Response response) throws IOException {
        try (final InputStream is = getClass().getResourceAsStream("loading.html")) {
            FileCopyUtils.copy(is, response.getOutputStream());
        }
    }
}
