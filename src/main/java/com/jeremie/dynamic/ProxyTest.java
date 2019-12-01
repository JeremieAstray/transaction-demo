package com.jeremie.dynamic;

import com.jeremie.dynamic.service.sign.SignDynamicProxyHandler;
import com.jeremie.dynamic.service.sign.SignImpl;
import com.jeremie.dynamic.service.sign.SignInterface;
import com.jeremie.dynamic.service.sign.SignStaticProxy;

import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) {
        SignInterface signInterface = new SignImpl();
        //自己处理
        signInterface.sign("我的资料");

        //静态代理
        SignStaticProxy signStaticProxy = new SignStaticProxy(new SignImpl());
        signStaticProxy.sign("我的资料");

        //动态代理
        SignInterface dynamicObject = (SignInterface) Proxy.newProxyInstance(SignInterface.class.getClassLoader()
                , new Class[]{SignInterface.class}, new SignDynamicProxyHandler(signInterface));
        dynamicObject.sign("我的资料");
    }
}
