package com.jeremie.asm;

import com.jeremie.asm.interceptor.MethodInterceptor;
import com.jeremie.asm.interceptor.MyMethodInterceptor;

/**
 * @author guanhong 2019-07-04.
 */
public class SubAsmTest {

    private AsmTest asmTest = new AsmTest();

    public MethodInterceptor methodInterceptor = new MyMethodInterceptor();

    public String getName() {
        return asmTest.getName();
    }

    public int getAge() {
        return asmTest.getAge();
    }

    public int getId() throws Throwable {

        return (int) methodInterceptor.intercept(asmTest, asmTest.getClass().getMethod("getId"), new Object[]{});
    }

    public String getAll() {
        return asmTest.getAll();
    }

    public void addAge() throws Throwable {
        asmTest.addAge();
    }

    public String testParam(int test, int test2, int test3) throws Throwable {
        methodInterceptor.intercept(asmTest, asmTest.getClass().getMethod("testParam", Integer.TYPE, Integer.TYPE, Integer.TYPE), new Object[]{test, test2, test3});

        return asmTest.testParam(test, test2, test3);
    }
}
