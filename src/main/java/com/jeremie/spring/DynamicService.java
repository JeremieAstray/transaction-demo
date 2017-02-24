package com.jeremie.spring;

import com.jeremie.demo.MyService;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author guanhong 2017/2/22.
 */
public class DynamicService extends BaseDynamicService implements MethodInterceptor {

    private MyService myService;

    DynamicService(MyService myService) {
        this.myService = myService;
        this.myService.setMyDao(ApplicationContext.myDao);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        this.before(method);
        Object result;
        try {
            result = method.invoke(this.myService, objects);
            this.after();
            return result;
        } catch (Exception e) {
            this.exception(e);
            throw e;
        }
    }
}
