/*
* Created at 22:23 on 29/11/15
*/
package com.example.web;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zzhao
 */
@Component("hello")
public class HelloServlet implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final PrintWriter writer = resp.getWriter();
        writer.println("Hallo");
        writer.flush();
    }
}
