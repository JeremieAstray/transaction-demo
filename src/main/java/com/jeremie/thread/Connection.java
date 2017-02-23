package com.jeremie.thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author guanhong 2017/2/22.
 */
public class Connection implements PoolObject {
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private int id;

    public Connection() {
        this.id = atomicInteger.incrementAndGet();
    }

    public int getId() {
        return this.id;
    }

    public void query() {
        try {
            //模拟一下操作吧
            System.out.println("connection id:" + this.id + " threadId : " + Thread.currentThread().getName());
            System.out.println("2秒");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        try {
            //模拟一下操作吧
            System.out.println("connection id:" + this.id + " threadId : " + Thread.currentThread().getName());
            System.out.println("500毫秒");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            //模拟一下操作吧
            System.out.println("connection id:" + this.id + " threadId : " + Thread.currentThread().getName());
            System.out.println("20毫秒");
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startTransaction() {
        System.out.println("start transaction");
    }

    public void commit() {
        System.out.println("commit transaction");
    }

    public void rollBack() {
        System.out.println("roll back transaction");
    }

    /*public static class ConnectionPoolBeanFactory implements PoolBeanFactory<Connection> {

        @Override
        public Connection init() {
            return new Connection();
        }
    }*/
}
