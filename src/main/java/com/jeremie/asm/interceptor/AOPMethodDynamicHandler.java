package com.jeremie.asm.interceptor;

import java.lang.reflect.Method;

/**
 *
 */
public class AOPMethodDynamicHandler extends MethodDynamicInterceptor {

    @Override
    public void before(Object o, Method method, Object[] objects) {
        System.out.println(method.getName() + " start");
    }

    @Override
    public void after(Object o, Method method, Object[] objects) {
        System.out.println(method.getName() + " end");
    }

    @Override
    public void exception(Object o, Method method, Object[] objects, Exception e) throws Exception {
        System.out.println(method.getName() + " exception");
    }
}
