package com.jeremie.demo;

import com.jeremie.stereotype.Repository;
import com.jeremie.spring.ApplicationContext;

/**
 * @author guanhong 2017/2/22.
 */
@Repository
public class MyDao {

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
