package com.jeremie.stereotype;

import java.lang.annotation.*;

/**
 * @author guanhong 2017/2/24.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
