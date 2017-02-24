package com.jeremie.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author guanhong 2017/2/24.
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface Transaction {
    boolean open() default true;

}
