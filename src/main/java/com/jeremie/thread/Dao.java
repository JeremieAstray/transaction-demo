package com.jeremie.thread;

/**
 * @author guanhong 2017/2/22.
 */
public class Dao {

    public void query(String currentThreadId) {
        ApplicationContext.connectionThreadLocal.get().query(currentThreadId);
    }

    public void readObject(String currentThreadId) throws Exception {
        ApplicationContext.connectionThreadLocal.get().read(currentThreadId);
        //throw new NullPointerException();
    }

    public void deleteObject(String currentThreadId) {
        ApplicationContext.connectionThreadLocal.get().delete(currentThreadId);
    }

    public void multiQuery(String currentThreadId) {
        ApplicationContext.connectionThreadLocal.get().query(currentThreadId);
    }

}
