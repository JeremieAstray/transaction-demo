package com.jeremie.thread;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author guanhong 2017/2/22.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        for (int i = 0; i < 80; i++) {
            executorService.execute(new FooThread());
        }
        executorService.shutdown();
    }


    public static class FooThread implements Runnable {

        @Override
        public void run() {
            ApplicationContext.service.test1();
            try {
                ApplicationContext.service.test2();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
