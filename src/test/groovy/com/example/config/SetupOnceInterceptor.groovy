package com.example.config

import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation

/**
 * @author zzhao
 */
class SetupOnceInterceptor extends AbstractMethodInterceptor {

    String setupMethodName

    String cleanupMethodName

    @Override
    void interceptFeatureExecution(IMethodInvocation invocation) throws Throwable {
        def instance = invocation.sharedInstance
        if (this.setupMethodName) {
            instance."$setupMethodName"()
        }
        invocation.proceed()
        if (this.cleanupMethodName) {
            instance."$cleanupMethodName"()
        }
    }
}
