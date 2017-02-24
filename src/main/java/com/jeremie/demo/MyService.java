package com.jeremie.demo;

import com.jeremie.annotation.Autowrie;
import com.jeremie.annotation.Service;
import com.jeremie.annotation.Transaction;

/**
 * @author guanhong 2017/2/22.
 */
@Service
@Transaction
public class MyService {

    @Autowrie
    private MyDao myDao;

    public void test1() throws Exception {
        myDao.readObject();
        myDao.query();
    }

    @Transaction(open = false)
    public void test4() throws Exception{
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
