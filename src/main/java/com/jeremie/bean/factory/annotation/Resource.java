package com.jeremie.bean.factory.annotation;

import java.lang.annotation.*;

/**
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Resource {
}
