package com.jeremie.spring;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *
 */
public interface TransactionHandler {

    void before(Object o, Method method, Object[] objects, MethodProxy methodProxy);

    void after(Object o, Method method, Object[] objects, MethodProxy methodProxy);

    void exception(Object o, Method method, Object[] objects, MethodProxy methodProxy, Exception e) throws Exception;
}
