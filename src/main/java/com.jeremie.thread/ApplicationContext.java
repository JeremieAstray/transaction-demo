package com.jeremie.thread;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author guanhong 2017/2/22.
 */
public class ApplicationContext {
    public static MyPool<Connection> connectionPool = new MyPool<>(new Connection.ConnectionPoolBeanFactory());

    //单例
    public static Dao dao = new Dao();
    public static Service service = (Service) Enhancer.create(Service.class, new DynamicService());


}
