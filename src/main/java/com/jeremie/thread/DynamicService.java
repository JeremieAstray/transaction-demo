package com.jeremie.thread;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author guanhong 2017/2/22.
 */
public class DynamicService implements MethodInterceptor {


    private Service service;

    DynamicService(Service service) {
        this.service = service;
        this.service.setDao(ApplicationContext.dao);
    }

    private void before() {
        ApplicationContext.connectionThreadLocal.get().startTransaction();
    }

    private void after() {
        ApplicationContext.connectionThreadLocal.get().commit();
        ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
        ApplicationContext.connectionThreadLocal.remove();
    }

    private void exception(Exception e) throws Exception {
        e.printStackTrace();
        ApplicationContext.connectionThreadLocal.get().rollBack();
        ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
        ApplicationContext.connectionThreadLocal.remove();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        this.before();
        Object result;
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
