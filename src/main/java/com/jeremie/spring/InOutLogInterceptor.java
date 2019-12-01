package com.jeremie.spring;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang.time.DateFormatUtils;

import java.lang.reflect.Method;
import java.util.Date;

public class InOutLogInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result;
        try {
            System.out.println("before invoke method " + method.getName() + " in time "
                    + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            result = proxy.invokeSuper(obj, args);
            System.out.println("after invoke method " + method.getName() + " out time "
                    + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            return result;
        } catch (Exception e) {
            System.out.println("invoke method " + method.getName() + " error time "
                    + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            throw e;
        }
    }
}
