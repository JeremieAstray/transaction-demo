package com.jeremie.dynamic.cglib;

import com.jeremie.dynamic.bean.InterfaceBean;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

/**
 * @author guanhong 2019/4/19.
 */
public class CglibTest {
    public static void main(String[] args) {
        //代理接口
        //testInterface();
        int[] list = new int[]{1, 123213, 2, 3, 4, 5, 4, 3, 2, 1, 123213};
        int result = 0;
        for (int i = 0; i < list.length; i++) {
            result = result ^ list[i];
        }
        System.out.println(result);
    }

    private static void testInterface() {
        /*//代理接口
        Enhancer hancer = new Enhancer();
        hancer.setInterfaces(new Class[]{InterfaceBean.class});
        hancer.setCallback((InvocationHandler) (o, method, params) -> {
            if (method.getName().equals("invoke")) {
                return params[0] + "，invoke方法测试";
            }
            return params[0];
        });
        Object o = hancer.create();
        //动态对象
        InterfaceBean interfaceBean = (InterfaceBean) o;
        System.out.println(interfaceBean.invoke("jdk动态代理invoke"));
        System.out.println(interfaceBean.invoke2("jdk动态代理invoke2"));*/
    }
}
