package com.jeremie.demo;

import com.jeremie.connection.Connection;
import com.jeremie.stereotype.Repository;
import com.jeremie.spring.ApplicationContext;

/**
 * @author guanhong 2017/2/22.
 */
@Repository
public class MyDao {

    private Connection getConnection() {
        return ApplicationContext.connectionThreadLocal.get();
    }

    public void query() {
        getConnection().query();
    }

    public void readObject() throws Exception {
        getConnection().read();
        //throw new NullPointerException();
    }

    public void deleteObject() {
        getConnection().delete();
    }

    public void multiQuery() {
        getConnection().query();
    }

}
