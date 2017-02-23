package com.jeremie.thread;

/**
 * @author guanhong 2017/2/22.
 */
public class Service {

    private Dao dao;

    public void test1() {
        dao.query(Thread.currentThread().getName());
    }

    public void test2() throws Exception {
        dao.query(Thread.currentThread().getName());
        dao.deleteObject(Thread.currentThread().getName());
        dao.readObject(Thread.currentThread().getName());
    }

    public void test3() {
        dao.deleteObject(Thread.currentThread().getName());
    }

    public void test4() {
        dao.multiQuery(Thread.currentThread().getName());
    }

    public Dao getDao() {
        return this.dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}
