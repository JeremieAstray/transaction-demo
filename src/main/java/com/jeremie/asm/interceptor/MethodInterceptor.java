package com.jeremie.asm.interceptor;

import java.lang.reflect.Method;

/**
 * @author guanhong 2019-07-04.
 */
public interface MethodInterceptor {

    public Object intercept(Object obj, Method method, Object[] args) throws Throwable;
}
