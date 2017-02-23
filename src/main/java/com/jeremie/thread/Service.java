package com.jeremie.thread;

/**
 * @author guanhong 2017/2/22.
 */
public class Service {

    private Dao dao;

    public void test1() {
        dao.query();
    }

    public void test2() throws Exception {
        dao.query();
        dao.deleteObject();
        dao.readObject();
    }

    public void test3() {
        dao.deleteObject();
    }

    public void test4() {
        dao.multiQuery();
    }

    public Dao getDao() {
        return this.dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}
