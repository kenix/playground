/*
* vwd KL
* Created by zzhao on 1/15/16 12:49 PM
*/
package com.example.cli;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.util.thc.TypeReference;

/**
 * @author zzhao
 */
public class OpenLamdaCli {

    public static SerializedLambda serialized(Object obj) {
        try {
            final Method method = obj.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T create(TypeReference<T> tr) {
        return tr.newInstance();
    }

    public static void main(String[] args) {
        final ArrayList<String> id = create(i -> i);
        id.add("blah");
        System.out.println(id);

        final HashMap<String, String> map = create(foo -> foo);
        System.out.println(map);

        final SerializedLambda lambda = serialized((TypeReference<Integer>) i -> i);
        System.out.println(lambda.getCapturingClass());
        System.out.println(lambda.getFunctionalInterfaceClass());
        System.out.println(lambda.getFunctionalInterfaceMethodName());
        System.out.println(lambda.getFunctionalInterfaceMethodSignature());
        System.out.println(lambda.getImplClass());
        System.out.println(lambda.getImplMethodName());
        System.out.println(lambda.getImplMethodKind());
        System.out.println(lambda.getImplMethodSignature());
        System.out.println(lambda.getCapturedArgCount());
        System.out.println(lambda.getInstantiatedMethodType());
    }
}
