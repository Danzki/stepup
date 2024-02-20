package com.danzki;

import com.danzki.annotations.Cache;
import com.danzki.annotations.Mutator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CacheUtils {

    private Clock clock;

    public CacheUtils(Clock clock) {
        this.clock = clock;
    }

    public CacheUtils() {
        this(System::currentTimeMillis);
    }

    public static Map<String, Long> getAnnotatedMethodsMap(Object obj, Class annotation) {
        Method[] methods = obj.getClass().getDeclaredMethods();
        Map<String, Long> annotatedMethods = new HashMap<>();
        for(Method method : methods) {
            if (method.getAnnotation(annotation) != null) {
                Cache cacheAnnotation = (Cache) method.getAnnotation(annotation);
                annotatedMethods.put(method.getName(), cacheAnnotation.time());
            }
        }

        return annotatedMethods;
    }

    public static List<String> getAnnotatedMethodsList(Object obj, Class annotation) {
        Method[] methods = obj.getClass().getDeclaredMethods();
        List<String> annotatedMethods = new ArrayList<>();
        for(Method method : methods) {
            if (method.getAnnotation(annotation) != null) {
                annotatedMethods.add(method.getName());
            }
        }

        return annotatedMethods;
    }

    private static ObjectCache getLatestCacheValue(List<ObjectCache> objectCaches) {
        ListIterator li = objectCaches.listIterator(objectCaches.size());
        while (li.hasPrevious()) {
            return (ObjectCache) li.previous();
        }
        return null;
    }

    private static void deleteNotLive(List<ObjectCache> objectCaches, Clock clock) {
        ListIterator li = objectCaches.listIterator(objectCaches.size());
        while (li.hasPrevious()) {
            ObjectCache dc = (ObjectCache) li.previous();
            if (!dc.isLive(clock.currentMillis())) {
                synchronized (objectCaches) {
                    objectCaches.remove(objectCaches.indexOf(dc));
                }
            }
        }
    }

    public <T> T cache(T obj) {
        Map<String, List<ObjectCache>> cacheData = new ConcurrentHashMap<>();

        Map<String, Long> annotatedCacheMethods = getAnnotatedMethodsMap(obj, Cache.class);
        List<String> annotatedMutatorMethods = getAnnotatedMethodsList(obj, Mutator.class);

        InvocationHandler handler = (proxy, method, args) -> {
            String methodName = method.getName();
            Object result;
            ObjectCache cacheLatest = null;
            Long liveTime = annotatedCacheMethods.get(methodName);
            if (liveTime != null) {
                if (cacheData.containsKey(methodName)) {
                    deleteNotLive(cacheData.get(methodName), clock);
                    cacheLatest = getLatestCacheValue(cacheData.get(methodName));
                } else {
                    cacheData.put(methodName, new ArrayList<>());
                }
                if (cacheLatest == null) {
                    result = method.invoke(obj, args);
                    ObjectCache cacheValue = new ObjectCache(result, clock, liveTime);
                    cacheData.get(methodName).add(cacheValue);
                    synchronized (cacheData) {
                        cacheData.put(methodName, cacheData.get(methodName));
                    }
                } else {
                    result = cacheLatest.getValue();
                }
                return result;
            }

            //Mutator annotation
            if (annotatedMutatorMethods.contains(methodName)) {
                synchronized (cacheData) {
                    cacheData.clear();
                }
                return method.invoke(obj, args);
            }

            return method.invoke(obj, args);
        };

        return (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), handler);
    }
}
