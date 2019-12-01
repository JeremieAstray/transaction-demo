package com.jeremie.dynamic.service.sign;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SignDynamicProxyHandler implements InvocationHandler {

    private SignInterface signInterface;

    public SignDynamicProxyHandler(SignInterface signInterface) {
        this.signInterface = signInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (proxy instanceof SignInterface) {
            System.out.println("签证前准备");
            Object result = method.invoke(signInterface, args);
            System.out.println("签证后处理");
            return result;
        }
        return method.invoke(proxy, args);
    }
}
