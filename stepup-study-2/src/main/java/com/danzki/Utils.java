package com.danzki;

import com.danzki.annotations.Cache;
import com.danzki.annotations.Mutator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    private static Map<String, Object> cacheData = new HashMap<>();
    public static List<String> getAnnotatedMethods(Object obj, Class annotation) {
        Method[] methods = obj.getClass().getDeclaredMethods();
        List<String> annotatedMethods = new ArrayList<>();
        for(Method method : methods) {
            if (method.getAnnotation(annotation) != null) {
                annotatedMethods.add(method.getName());
            }
        }
        return annotatedMethods;
    }

    public InvocationHandler getInvocationhandler(Object obj, Method method, Object... args) throws InvocationTargetException, IllegalAccessException {
//        Logic

        return (InvocationHandler) method.invoke(obj, args);
    }

    public static <T> T cache(T obj) {
        List<String> annotatedCacheMethods = getAnnotatedMethods(obj, Cache.class);
        List<String> annotatedMutatorMethods = getAnnotatedMethods(obj, Mutator.class);

        InvocationHandler handler = (proxy, method, args) -> {
            String methodName = method.getName();
            Object result = null;
            //Cache annotation
            if(annotatedCacheMethods.contains(methodName)) {
                if (cacheData.containsKey(methodName)) {
                    return cacheData.get(methodName);
                }
                result = method.invoke(obj, args);
                cacheData.put(methodName, result);
                return result;
            }

            //Mutator annotation
            if (annotatedMutatorMethods.contains(methodName)) {
                cacheData.clear();
            }

            return method.invoke(obj, args);
        };

        return (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), handler);
    }
}
