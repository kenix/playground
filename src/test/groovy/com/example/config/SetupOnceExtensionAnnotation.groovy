package com.example.config;

import org.spockframework.runtime.extension.ExtensionAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zzhao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtensionAnnotation(SetupOnceExtension.class)
public @interface SetupOnceExtensionAnnotation {
    String setupMethodName() default "";

    String cleanupMethodName() default "";
}
