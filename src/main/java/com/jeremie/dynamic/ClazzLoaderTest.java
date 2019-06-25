package com.jeremie.dynamic;

import com.jeremie.dynamic.bean.InterfaceBean;

/**
 * @author guanhong 2019-04-30.
 */
public class ClazzLoaderTest {

    public static void main(String[] args) {
        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(threadClassLoader);
        Class clazz = InterfaceBean.class;
        ClassLoader clazzClassLoader = clazz.getClassLoader();
        System.out.println(clazzClassLoader);
        System.out.println(123);
    }
}
