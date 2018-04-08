package com.jeremie.spring;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author guanhong 2017/2/22.
 */
public class TransactionDynamicHandler extends BaseDynamicService implements MethodInterceptor {

    private Object dynamicObject;

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        this.before(method);
        Object result;
        try {
            result = method.invoke(this.dynamicObject, objects);
            this.after();
            return result;
        } catch (Exception e) {
            this.exception(e);
            throw e;
        }
    }
}
