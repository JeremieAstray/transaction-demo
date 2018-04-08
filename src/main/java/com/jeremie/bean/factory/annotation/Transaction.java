package com.jeremie.bean.factory.annotation;

import com.jeremie.spring.TransactionDynamicHandler;

import java.lang.annotation.*;

/**
 * @author guanhong 2017/2/24.
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transaction {
    boolean open() default true;

    Class transactionDynamicClass() default TransactionDynamicHandler.class;

}
