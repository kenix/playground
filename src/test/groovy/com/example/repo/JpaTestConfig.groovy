package com.example.repo

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.web.*
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * @author zzhao
 */
@Configuration
@EnableAutoConfiguration(exclude = [
        ServerPropertiesAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        MultipartAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class,
        DispatcherServletAutoConfiguration.class,
        AopAutoConfiguration.class,
        GroovyTemplateAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class
])
@EntityScan(basePackages = ["com.example.domain"])
@EnableJpaRepositories(basePackages = ["com.example.repo"])
@EnableTransactionManagement
@PropertySources([
        @PropertySource(value = "classpath:application.properties"),
        @PropertySource(value = "classpath:application.yml")
])
class JpaTestConfig {
}
