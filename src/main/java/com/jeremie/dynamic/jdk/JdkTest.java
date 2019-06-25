package com.jeremie.dynamic.jdk;

import com.jeremie.dynamic.bean.InterfaceBean;

import java.lang.reflect.Proxy;

/**
 * @author guanhong 2019/4/19.
 */
public class JdkTest {
    public static void main(String[] args) {
        //代理接口
        testInterface();
    }

    private static void testInterface() {
        /*//代理接口
        Class clazz = InterfaceBean.class;
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, params) -> {
            if (method.getName().equals("invoke")) {
                return params[0] + "，invoke方法测试";
            }
            return params[0];
        });
        //动态对象
        InterfaceBean interfaceBean = (InterfaceBean) o;
        System.out.println(interfaceBean.invoke("jdk动态代理invoke"));
        System.out.println(interfaceBean.invoke2("jdk动态代理invoke2"));*/
    }

    private static void testAbstrctBean() {

    }
}
