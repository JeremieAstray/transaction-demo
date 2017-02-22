package com.jeremie.thread;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author guanhong 2017/2/22.
 */
public class MyPool<T extends PoolObject> {
    private LinkedList<T> poolBeanLinkedListPool = new LinkedList<>();
    private PoolBeanFactory<T> poolBeanFactory;
    private Map<Integer, T> integerTMap = new HashMap<>();

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
                while (this.poolBeanLinkedListPool.isEmpty()) {

                }
            }
        }
        //取出连接池中一个连接
        T result = this.poolBeanLinkedListPool.removeFirst(); // 删除第一个连接返回
        integerTMap.putIfAbsent(result.getId(), result);
        return result;
    }

    //将连接放回连接池
    public synchronized void releaseConnection(int id) {
        if (integerTMap.get(id) != null) {
            this.poolBeanLinkedListPool.add(integerTMap.get(id));
        }
    }

    private void increasePoolSize() {
        this.newPoolBeans(Math.min(MAX_SIZE - this.size, INCREASE_SIZE));
    }
}
