package com.example;

import com.example.component.ProgressBeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public ProgressBeanPostProcessor progressBeanPostProcessor() {
        return new ProgressBeanPostProcessor();
    }
}
