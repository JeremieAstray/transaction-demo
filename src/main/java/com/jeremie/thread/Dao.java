package com.jeremie.thread;

/**
 * @author guanhong 2017/2/22.
 */
public class Dao {
    private static ThreadLocal<Connection> connectionThreadLocal = ThreadLocal.withInitial(ApplicationContext.connectionPool::getConnection);

    public void query(String currentThreadId) {
        connectionThreadLocal.get().query(currentThreadId);
    }

    public void readObject(String currentThreadId) throws Exception {
        connectionThreadLocal.get().read(currentThreadId);
        //throw new NullPointerException();
    }

    public void deleteObject(String currentThreadId) {
        connectionThreadLocal.get().delete(currentThreadId);
    }

    public void multiQuery(String currentThreadId) {
        connectionThreadLocal.get().query(currentThreadId);
    }

    public Connection getCurrentConnection() {
        return connectionThreadLocal.get();
    }

    public void releaseConnection() {
        ApplicationContext.connectionPool.releaseConnection(connectionThreadLocal.get().getId());
        connectionThreadLocal.remove();
    }

}
