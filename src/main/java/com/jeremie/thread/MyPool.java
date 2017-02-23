package com.jeremie.thread;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author guanhong 2017/2/22.
 */
public class MyPool<T extends PoolObject> {
    private Deque<T> poolBeanLinkedListPool = new ConcurrentLinkedDeque<>();
    private PoolBeanFactory<T> poolBeanFactory;
    private Map<Integer, T> integerTMap = new HashMap<>();

    //private static long timeout = 180000;//ms

    private AtomicInteger size = new AtomicInteger(0);
    private static int INITIAL_SIZE = 10;
    private static int INCREASE_SIZE = 20;
    private static int MAX_SIZE = 20;


    MyPool(PoolBeanFactory<T> poolBeanFactory) {
        if (poolBeanFactory == null) {
            throw new NullPointerException("poolBeanFactory should now be null!");
        }
        this.poolBeanFactory = poolBeanFactory;
        this.newPoolBeans(INITIAL_SIZE);
    }

    private void newPoolBeans(int size) {
        this.size.addAndGet(size);
        for (int i = 0; i < size; i++) {
            T connection = this.poolBeanFactory.init();
            this.poolBeanLinkedListPool.add(connection);
            this.integerTMap.put(connection.getId(), connection);
        }
    }

    public synchronized T getConnection() {
        if (this.poolBeanLinkedListPool.isEmpty()) {
            if (this.size.get() < MAX_SIZE) {
                increasePoolSize();
            } else {
                while (this.poolBeanLinkedListPool.isEmpty()) {
                    //暂时设定等待一会
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //取出连接池中一个连接
        T result = this.poolBeanLinkedListPool.removeFirst(); // 删除第一个连接返回
        System.out.println("获取连接 id：" + result.getId());
        return result;
    }

    //将连接放回连接池
    public void releaseConnection(int id) {
        if (integerTMap.get(id) != null) {
            System.out.println("释放连接 id：" + id);
            this.poolBeanLinkedListPool.add(integerTMap.get(id));
        }
    }

    private void increasePoolSize() {
        System.out.println("init size : " + Math.min(MAX_SIZE - this.size.get(), INCREASE_SIZE));
        this.newPoolBeans(Math.min(MAX_SIZE - this.size.get(), INCREASE_SIZE));
    }
}
