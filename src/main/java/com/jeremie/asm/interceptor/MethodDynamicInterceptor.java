package com.jeremie.asm.interceptor;

import java.lang.reflect.Method;

/**
 * @author guanhong 2019-07-04.
 */
public abstract class MethodDynamicInterceptor implements MethodInterceptor, TransactionHandler {

    public Object intercept(Object o, Method method, Object[] objects) throws Throwable {
        Object result;
        this.before(o, method, objects);
        try {
            result = method.invoke(o, objects);
            this.after(o, method, objects);
            return result;
        } catch (Exception e) {
            this.exception(o, method, objects, e);
            throw e;
        }
    }
}
