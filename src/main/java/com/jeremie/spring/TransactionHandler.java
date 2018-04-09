package com.jeremie.spring;

import java.lang.reflect.Method;

/**
 */
public interface TransactionHandler {

    void before(Method method);

    void after();

    void exception(Exception e) throws Exception;
}
