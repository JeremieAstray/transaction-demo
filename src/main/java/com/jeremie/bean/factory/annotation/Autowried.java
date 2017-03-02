package com.jeremie.bean.factory.annotation;

import java.lang.annotation.*;

/**
 * @author guanhong 2017/2/24.
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowried {
    boolean required() default true;
}
