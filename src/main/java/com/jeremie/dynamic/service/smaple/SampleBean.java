package com.jeremie.dynamic.service.smaple;

/**
 * @author guanhong 2019/4/19.
 */
public class SampleBean {
    public String invoke(String test) {
        System.out.println("正常类调用：" + test);
        return test;
    }
    public String invoke2(String test) {
        System.out.println("正常类调用：" + test);
        return test;
    }
}
