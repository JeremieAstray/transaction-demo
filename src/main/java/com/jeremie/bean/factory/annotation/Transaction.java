package com.jeremie.bean.factory.annotation;

import com.jeremie.spring.PropagationRequiredTransactionDynamicHandler;

import java.lang.annotation.*;

/**
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transaction {
    boolean open() default true;

    Class transactionDynamicClass() default PropagationRequiredTransactionDynamicHandler.class;

}
