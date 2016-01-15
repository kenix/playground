/*
* vwd KL
* Created by zzhao on 1/15/16 12:56 PM
*/
package com.example.util.thc;

import java.util.HashMap;
import java.util.Map;

/**
 */
public final class DefaultValue {

    private DefaultValue() {
        throw new AssertionError("not for instantiation or inheritance");
    }

    private static Map<Class<?>, Object> defaultValues = new HashMap<>();

    static {
        defaultValues.put(int.class, 0);
        defaultValues.put(Integer.class, 0);
        defaultValues.put(boolean.class, false);
        defaultValues.put(Boolean.class, false);
        defaultValues.put(byte.class, (byte) 0);
        defaultValues.put(Byte.class, 0);
        defaultValues.put(char.class, ' ');
        defaultValues.put(Character.class, ' ');
        defaultValues.put(short.class, (short) 0.0);
        defaultValues.put(Short.class, (short) 0.0);
        defaultValues.put(long.class, 0l);
        defaultValues.put(Long.class, 0L);
        defaultValues.put(float.class, 0.0f);
        defaultValues.put(Float.class, 0.0f);
        defaultValues.put(double.class, 0.0d);
        defaultValues.put(Double.class, 0.0d);
    }

    public static <T> T ofType(Class<?> type) {
        return (T) defaultValues.getOrDefault(type, null);
    }
}
