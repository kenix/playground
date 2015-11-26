/*
* Created at 02:39 on 26/11/15
*/
package com.example.component;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

/**
 * @author zzhao
 */
@Component
public class SlowBeans implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        IntStream
                .range(0, 10)
                .forEach(i -> registry.registerBeanDefinition(
                        "slow-" + i,
                        new RootBeanDefinition(SlowBean.class)));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    public static class SlowBean {
        private static final RateLimiter limiter = RateLimiter.create(5);

        @PostConstruct
        public void init() {
            limiter.acquire();
        }
    }
}
