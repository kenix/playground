/*
* vwd KL
* Created by zzhao on 1/15/16 12:36 PM
*/
package com.example.util.thc;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

/**
 */
public interface MethodFinder extends Serializable {

  default SerializedLambda serialized() {
    try {
      final Method method = getClass().getDeclaredMethod("writeReplace");
      method.setAccessible(true);
      return (SerializedLambda) method.invoke(this);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
  }

  default Class<?> getContainingClass() {
    try {
      final String className = serialized().getImplClass().replaceAll("/", ".");
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  default Method method() {
    final SerializedLambda lambda = serialized();
    final Class<?> containingClass = getContainingClass();
    return Arrays
        .stream(containingClass.getDeclaredMethods())
        .filter(method -> Objects.equals(method.getName(), lambda.getImplMethodName()))
        .findFirst()
        .orElseThrow(UnableToGuessMethodException::new);
  }

  default int parameterCount() {
    return method().getParameters().length;
  }

  default Parameter parameter(int n) {
    return method().getParameters()[n];
  }

  default Object defaultValueForParameter(int n) {
    return DefaultValue.ofType(parameter(n).getType());
  }

  class UnableToGuessMethodException extends RuntimeException {

  }
}
