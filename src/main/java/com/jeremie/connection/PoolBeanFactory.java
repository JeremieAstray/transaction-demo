package com.jeremie.connection;

/**
 * @author guanhong 2017/2/22.
 */
public interface PoolBeanFactory<T> {
    T init();
}
