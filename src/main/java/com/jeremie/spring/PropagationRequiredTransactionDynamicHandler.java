package com.jeremie.spring;

import java.lang.reflect.Method;
import java.util.Stack;

/**
 */
public class PropagationRequiredTransactionDynamicHandler extends TransactionDynamicHandler {

    private static ThreadLocal<Stack<Method>> methodStack = ThreadLocal.withInitial(Stack::new);


    //PROPAGATION_REQUIRED事务处理方式简单实现
    public void before(Method method) {
        if (methodStack.get().isEmpty()) {
            ApplicationContext.connectionThreadLocal.get().startTransaction();
        }
        methodStack.get().push(method);
    }

    public void after() {
        methodStack.get().pop();
        if (methodStack.get().empty()) {
            ApplicationContext.connectionThreadLocal.get().commit();
            ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
            ApplicationContext.connectionThreadLocal.remove();
        }
    }

    public void exception(Exception e) throws Exception {
        e.printStackTrace();
        methodStack.get().pop();
        if (methodStack.get().empty()) {
            ApplicationContext.connectionThreadLocal.get().rollBack();
            ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
            ApplicationContext.connectionThreadLocal.remove();
        }
    }
}
