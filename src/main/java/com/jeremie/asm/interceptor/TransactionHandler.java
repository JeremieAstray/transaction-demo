package com.jeremie.asm.interceptor;

import java.lang.reflect.Method;

/**
 *
 */
public interface TransactionHandler {

    void before(Object o, Method method, Object[] objects);

    void after(Object o, Method method, Object[] objects);

    void exception(Object o, Method method, Object[] objects, Exception e) throws Exception;
}
