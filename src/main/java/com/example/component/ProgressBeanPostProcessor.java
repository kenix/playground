/*
* Created at 00:10 on 26/11/15
*/
package com.example.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * @author zzhao
 */
@Slf4j
public class ProgressBeanPostProcessor implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {

    private static Subject<String, String> beans = ReplaySubject.create();

    static Observable<String> observe() {
        return beans.observeOn(Schedulers.io());
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        beans.onNext(beanName);
        if (bean instanceof EmbeddedServletContainerFactory) {
            return wrap((EmbeddedServletContainerFactory) bean);
        }
        return bean;
    }

    private EmbeddedServletContainerFactory wrap(EmbeddedServletContainerFactory fac) {
        if (fac instanceof TomcatEmbeddedServletContainerFactory) {
            ((TomcatEmbeddedServletContainerFactory) fac).addContextValves(new ProgressValve());
        }
        return (initializers) -> {
            final EmbeddedServletContainer container = fac.getEmbeddedServletContainer(initializers);
            log.info("<getEmbeddedServletContainer> eagerly starting embedded servlet container {}", container);
            container.start();
            return container;
        };
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        beans.onCompleted();
    }
}
