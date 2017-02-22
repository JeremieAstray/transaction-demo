package com.jeremie.thread;

import java.util.LinkedList;

/**
 * @author guanhong 2017/2/22.
 */
public class MyPool<T> {
    private LinkedList<T> poolBeanLinkedListPool = new LinkedList<>();
    private PoolBeanFactory<T> poolBeanFactory;

    //private static long timeout = 180000;//ms

    private int size = 0;
    private static int INITIAL_SIZE = 20;
    private static int INCREASE_SIZE = 20;
    private static int MAX_SIZE = 20;


    MyPool(PoolBeanFactory<T> poolBeanFactory) {
        if (poolBeanFactory == null) {
            throw new NullPointerException("poolBeanFactory should now be null!");
        }
        this.poolBeanFactory = poolBeanFactory;
        this.size = INITIAL_SIZE;
        this.newPoolBeans(this.size);
    }

    private void newPoolBeans(int size) {
        for (int i = 0; i < size; i++) {
            this.poolBeanLinkedListPool.add(this.poolBeanFactory.init());
        }
    }

    public synchronized T getConnection() {
        if (this.poolBeanLinkedListPool.isEmpty()) {
            if (this.size < MAX_SIZE) {
                increasePoolSize();
            } else {
                while (this.poolBeanLinkedListPool.isEmpty()){

                }
            }
        }
        //取出连接池中一个连接
        return this.poolBeanLinkedListPool.removeFirst(); // 删除第一个连接返回
    }

    //将连接放回连接池
    public synchronized void releaseConnection(T poolBean) {
        this.poolBeanLinkedListPool.add(poolBean);
    }

    private void increasePoolSize() {
        this.newPoolBeans(Math.min(MAX_SIZE - this.size, INCREASE_SIZE));
    }
}
