package com.example.config

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FeatureInfo

/**
 * @author zzhao
 */
class SetupOnceExtension extends AbstractAnnotationDrivenExtension<SetupOnceExtensionAnnotation> {

    @Override
    void visitFeatureAnnotation(SetupOnceExtensionAnnotation annotation, FeatureInfo feature) {
        //Retrieve the name of the setup method we'd like to invoke from our annotation
        def setupMethodName = annotation.setupMethodName()
        def cleanupMethodName = annotation.cleanupMethodName()
        //Construct and subscribe our event interceptor
        def interceptor = new SetupOnceInterceptor(
                setupMethodName: setupMethodName,
                cleanupMethodName: cleanupMethodName
        )
        feature.addInterceptor(interceptor)
    }
}
