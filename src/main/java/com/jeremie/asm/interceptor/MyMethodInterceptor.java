package com.jeremie.asm.interceptor;

import java.lang.reflect.Method;

/**
 * @author guanhong 2019-07-04.
 */
public class MyMethodInterceptor implements MethodInterceptor {

    public Object intercept(Object o, Method method, Object[] objects) throws Throwable {
        System.out.println(method.getName() + " start");
        Object result;
        try {
            result = o.getClass().getSuperclass().getMethod(method.getName()).invoke(o, objects);
            System.out.println(method.getName() + " end");
            return result;
        } catch (Exception e) {
            System.out.println(method.getName() + " exception");
            throw e;
        }
    }
}
