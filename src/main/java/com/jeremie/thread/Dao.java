package com.jeremie.thread;

/**
 * @author guanhong 2017/2/22.
 */
public class Dao {

    public void query() {
        ApplicationContext.connectionThreadLocal.get().query();
    }

    public void readObject() throws Exception {
        ApplicationContext.connectionThreadLocal.get().read();
        //throw new NullPointerException();
    }

    public void deleteObject() {
        ApplicationContext.connectionThreadLocal.get().delete();
    }

    public void multiQuery() {
        ApplicationContext.connectionThreadLocal.get().query();
    }

}
