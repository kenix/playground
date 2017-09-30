/*
* vwd KL
* Created by zzhao on 1/15/16 11:37 AM
*/
package com.example.util.thc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Super Type Token {@literal http://gafter.blogspot.co.uk/2006/12/super-type-tokens.html}
 * References a generic type.
 *
 * @author crazybob@google.com (Bob Lee)
 */
public abstract class TypeRef<T> {

  private final Type type;

  private volatile Constructor<?> constructor;

  protected TypeRef() {
    final Type superclass = getClass().getGenericSuperclass();
    System.out.println("superclass: " + superclass);
    if (superclass instanceof Class) {
      throw new IllegalArgumentException("Missing type parameter");
    }
    this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    System.out.println("type: " + this.type);
  }

  @SuppressWarnings("unchecked")
  public T newInstance() {
    if (this.constructor == null) {
      final Class<?> rawType = type instanceof Class<?>
          ? (Class<?>) type
          : (Class<?>) ((ParameterizedType) type).getRawType();
      try {
        this.constructor = rawType.getConstructor();
      } catch (NoSuchMethodException e) {
        throw new IllegalStateException(e);
      }
    }
    try {
      return (T) constructor.newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
  }

  public Type getType() {
    return this.type;
  }

  public static <T> T create(TypeRef<T> type) {
    return type.newInstance();
  }
}
