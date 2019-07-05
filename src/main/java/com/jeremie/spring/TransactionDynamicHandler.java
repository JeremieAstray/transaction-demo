package com.jeremie.spring;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *
 */
public abstract class TransactionDynamicHandler implements TransactionHandler, MethodInterceptor {

    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        this.before(o, method, objects, methodProxy);
        Object result;
        try {
            result = methodProxy.invokeSuper(o, objects);
            this.after(o, method, objects, methodProxy);
            return result;
        } catch (Exception e) {
            this.exception(o, method, objects, methodProxy, e);
            throw e;
        }
    }
}
