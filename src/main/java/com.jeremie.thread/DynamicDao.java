package com.jeremie.thread;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author guanhong 2017/2/22.
 */
@Deprecated
public class DynamicDao implements MethodInterceptor {


    private Dao dao;

    DynamicDao() {
        this.dao = new Dao();
    }

    private void before() {
    }

    private void after() {
    }

    private void exception(Exception e) throws Exception {
        e.printStackTrace();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        this.before();
        Object result = null;
        try {
            result = method.invoke(this.dao, objects);
            this.after();
            return result;
        } catch (Exception e) {
            this.exception(e);
            throw e;
        }
    }
}
