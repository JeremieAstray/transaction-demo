package com.jeremie.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author guanhong 2017/2/24.
 */
@Bean
@Target(value = {ElementType.TYPE})
public @interface Service {
}
