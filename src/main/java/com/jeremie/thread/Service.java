package com.jeremie.thread;

/**
 * @author guanhong 2017/2/22.
 */
public class Service {

    private Dao dao;

    public void test1() {
        dao.test1(Thread.currentThread().getName());
    }

    public void test2() throws Exception {
        dao.test1(Thread.currentThread().getName());
        dao.test3(Thread.currentThread().getName());
        dao.test2(Thread.currentThread().getName());
    }

    public void test3() {
        dao.test3(Thread.currentThread().getName());
    }

    public void test4() {
        dao.test4(Thread.currentThread().getName());
    }

    public Dao getDao() {
        return this.dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}
