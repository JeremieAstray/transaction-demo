package com.jeremie.thread;

/**
 * @author guanhong 2017/2/22.
 */
public class Dao {
    private static ThreadLocal<Connection> connectionThreadLocal = ThreadLocal.withInitial(ApplicationContext.connectionPool::getConnection);

    public void test1(String currentThreadId) {
        connectionThreadLocal.get().doSomething(currentThreadId);
    }

    public void test2(String currentThreadId) throws Exception {
        connectionThreadLocal.get().doSomething2(currentThreadId);
        //throw new NullPointerException();
    }

    public void test3(String currentThreadId) {
        connectionThreadLocal.get().doSomething3(currentThreadId);
    }

    public void test4(String currentThreadId) {
        connectionThreadLocal.get().doSomething(currentThreadId);
    }

    public Connection getCurrentConnection() {
        return connectionThreadLocal.get();
    }

    public void removeConnection() {
        ApplicationContext.connectionPool.releaseConnection(connectionThreadLocal.get());
        connectionThreadLocal.remove();
    }

}
