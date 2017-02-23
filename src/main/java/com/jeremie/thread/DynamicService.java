package com.jeremie.thread;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Stack;

/**
 * @author guanhong 2017/2/22.
 */
public class DynamicService implements MethodInterceptor {

    private static ThreadLocal<Stack<Method>> methodStack = ThreadLocal.withInitial(Stack::new);

    private Service service;

    DynamicService(Service service) {
        this.service = service;
        this.service.setDao(ApplicationContext.dao);
    }

    private void before(Method method) {
        if (methodStack.get().isEmpty()) {
            ApplicationContext.connectionThreadLocal.get().startTransaction();
        }
        methodStack.get().push(method);
    }

    private void after() {
        methodStack.get().pop();
        if (methodStack.get().empty()) {
            ApplicationContext.connectionThreadLocal.get().commit();
            ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
            ApplicationContext.connectionThreadLocal.remove();
        }
    }

    private void exception(Exception e) throws Exception {
        e.printStackTrace();
        methodStack.get().pop();
        if (methodStack.get().empty()) {
            ApplicationContext.connectionThreadLocal.get().rollBack();
            ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
            ApplicationContext.connectionThreadLocal.remove();
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        this.before(method);
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
