package com.jeremie.asm;

import com.jeremie.asm.interceptor.MethodInterceptor;
import com.jeremie.asm.interceptor.MyMethodInterceptor;

/**
 * @author guanhong 2019-07-04.
 */
public class SubAsmTest extends AsmTest {

    public MethodInterceptor methodInterceptor = new MyMethodInterceptor();

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public int getAge() {
        return super.getAge();
    }

    @Override
    public int getId() throws Throwable {
        methodInterceptor.intercept(this, this.getClass().getMethod("getId"), new Object[]{});
        return super.getId();
    }

    @Override
    public String getAll() {
        return super.getAll();
    }

    @Override
    public void addAge() throws Throwable {
        super.addAge();
    }

    @Override
    public String testParam(int test, int test2, int test3) throws Throwable {
        methodInterceptor.intercept(this, this.getClass().getMethod("testParam", Integer.TYPE, Integer.TYPE, Integer.TYPE), new Object[]{test, test2, test3});

        return super.testParam(test, test2, test3);
    }
}
