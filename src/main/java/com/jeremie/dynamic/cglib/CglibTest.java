package com.jeremie.dynamic.cglib;

import com.jeremie.dynamic.service.smaple.AbstractBean;
import com.jeremie.dynamic.service.smaple.InterfaceBean;
import com.jeremie.dynamic.service.smaple.SampleBean;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author guanhong 2019/4/19.
 */
public class CglibTest {
    public static void main(String[] args) {
        //代理接口
        testInterface();
        //代理虚拟类
        testAbstractClass();
        //代理类
        testClass();
    }

    private static void testInterface() {
        //代理接口
        //增强器
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{InterfaceBean.class});
        enhancer.setCallback((InvocationHandler) (o, method, params) -> {
            if (method.getName().equals("invoke")) {
                System.out.println("代理接口invoke");
                return params[0] + "，invoke方法测试";
            }
            return params[0];
        });
        //创建对代理对象
        Object o = enhancer.create();
        //动态对象
        InterfaceBean interfaceBean = (InterfaceBean) o;
        System.out.println(interfaceBean.invoke("cglib动态代理invoke"));
        System.out.println(interfaceBean.invoke2("cglib动态代理invoke2"));
    }

    private static void testAbstractClass() {
        //代理虚拟类
        Enhancer hancer = new Enhancer();
        hancer.setSuperclass(AbstractBean.class);
        hancer.setCallback((InvocationHandler) (o, method, params) -> {
            if (method.getName().equals("invoke")) {
                System.out.println("代理虚拟类invoke");
                return params[0] + "，invoke方法测试";
            }
            return params[0];
        });
        Object o = hancer.create();
        //动态对象
        AbstractBean abstractBean = (AbstractBean) o;
        System.out.println(abstractBean.invoke("cglib虚拟类动态代理invoke"));
        System.out.println(abstractBean.invoke2("cglib虚拟类动态代理invoke2"));
    }

    private static void testClass() {
        //代理类
        Enhancer hancer = new Enhancer();
        hancer.setSuperclass(SampleBean.class);
        hancer.setCallback((MethodInterceptor) (o, method, params,methodProxy) -> {
            if (method.getName().equals("invoke")) {
                System.out.println("代理类invoke");
                return methodProxy.invokeSuper(o, params) + "，invoke方法测试";
            }
            return methodProxy.invokeSuper(o, params);
        });
        Object o = hancer.create();
        //动态对象
        SampleBean sampleBean = (SampleBean) o;
        System.out.println(sampleBean.invoke("cglib类动态代理invoke"));
        System.out.println(sampleBean.invoke2("cglib类动态代理invoke2"));
    }
}
