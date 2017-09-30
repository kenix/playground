package com.example;

import com.example.component.ProgressBeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

//    @Bean
//    public ProgressBeanPostProcessor progressBeanPostProcessor() {
//        return new ProgressBeanPostProcessor();
//    }

    @Bean
    public ServletRegistrationBean helloServletReg() {
        final ServletRegistrationBean regBean
                = new ServletRegistrationBean(new HttpRequestHandlerServlet(), "/play/hello");
        regBean.setName("hello");
        return regBean;
    }
}
