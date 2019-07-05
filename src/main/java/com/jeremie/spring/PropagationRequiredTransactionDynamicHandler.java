package com.jeremie.spring;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Stack;

/**
 *
 */
public class PropagationRequiredTransactionDynamicHandler extends TransactionDynamicHandler {

    private static ThreadLocal<Stack<Method>> methodStack = ThreadLocal.withInitial(Stack::new);

    //PROPAGATION_REQUIRED事务处理方式简单实现
    public void before(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        if (methodStack.get().isEmpty()) {
            ApplicationContext.connectionThreadLocal.get().startTransaction();
        }
        methodStack.get().push(method);
    }

    public void after(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        methodStack.get().pop();
        if (methodStack.get().empty()) {
            ApplicationContext.connectionThreadLocal.get().commit();
            ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
            ApplicationContext.connectionThreadLocal.remove();
        }
    }

    public void exception(Object o, Method method, Object[] objects, MethodProxy methodProxy, Exception e) throws Exception {
        e.printStackTrace();
        methodStack.get().pop();
        if (methodStack.get().empty()) {
            ApplicationContext.connectionThreadLocal.get().rollBack();
            ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
            ApplicationContext.connectionThreadLocal.remove();
        }
    }
}
