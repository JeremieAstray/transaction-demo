package com.jeremie.thread;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author guanhong 2017/2/22.
 */
public class DynamicService implements MethodInterceptor {


    private Service service;

    DynamicService() {
        this.service = new Service();
        this.service.setDao(ApplicationContext.dao);
    }

    private void before() {
        this.service.getDao().getCurrentConnection().startTransaction();
    }

    private void after() {
        this.service.getDao().getCurrentConnection().commit();
        this.service.getDao().removeConnection();
    }

    private void exception(Exception e) throws Exception {
        e.printStackTrace();
        this.service.getDao().getCurrentConnection().rollBack();
        this.service.getDao().removeConnection();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        this.before();
        Object result = null;
        try {
            result = method.invoke(this.service, objects);
            this.after();
            return result;
        } catch (Exception e) {
            this.exception(e);
            throw e;
        }
    }
}
