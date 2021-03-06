package com.jeremie.demo;

import com.jeremie.bean.factory.annotation.Autowried;
import com.jeremie.bean.factory.annotation.Transaction;
import com.jeremie.stereotype.Service;

/**
 */
@Service
@Transaction
public class MyService {

    @Autowried
    private MyDao myDao;

    public void test1() throws Exception {
        myDao.readObject();
        myDao.query();
    }

    public void test4() throws Exception {
        this.test2();
        myDao.multiQuery();
    }

    public void test2() throws Exception {
        myDao.query();
        myDao.deleteObject();
        myDao.readObject();
        this.test3();
    }

    public void test3() {
        myDao.deleteObject();
    }

    public MyDao getMyDao() {
        return this.myDao;
    }

    public void setMyDao(MyDao myDao) {
        this.myDao = myDao;
    }
}
