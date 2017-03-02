package com.jeremie.demo;


import com.jeremie.spring.ApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author guanhong 2017/2/22.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        ApplicationContext.setScanPackages("com.jeremie.demo");
        ApplicationContext.init();
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        for (int i = 0; i < 80; i++) {
            executorService.execute(new FooThread());
        }
        executorService.shutdown();
    }


    public static class FooThread implements Runnable {

        @Override
        public void run() {
            try {
                MyService myService = (MyService) ApplicationContext.getBean("com.jeremie.demo.MyService");
                myService.test1();
                myService.test2();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
