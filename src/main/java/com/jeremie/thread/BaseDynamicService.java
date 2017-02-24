package com.jeremie.thread;

import java.lang.reflect.Method;
import java.util.Stack;

/**
 * @author guanhong 2017/2/24.
 */
public class BaseDynamicService {

    private static ThreadLocal<Stack<Method>> methodStack = ThreadLocal.withInitial(Stack::new);


    //PROPAGATION_REQUIRED事务处理方式简单实现
    protected void before(Method method) {
        if (methodStack.get().isEmpty()) {
            ApplicationContext.connectionThreadLocal.get().startTransaction();
        }
        methodStack.get().push(method);
    }

    protected void after() {
        methodStack.get().pop();
        if (methodStack.get().empty()) {
            ApplicationContext.connectionThreadLocal.get().commit();
            ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
            ApplicationContext.connectionThreadLocal.remove();
        }
    }

    protected void exception(Exception e) throws Exception {
        e.printStackTrace();
        methodStack.get().pop();
        if (methodStack.get().empty()) {
            ApplicationContext.connectionThreadLocal.get().rollBack();
            ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
            ApplicationContext.connectionThreadLocal.remove();
        }
    }
}
