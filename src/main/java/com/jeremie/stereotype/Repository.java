package com.jeremie.stereotype;

import java.lang.annotation.*;

/**
 * @author guanhong 2017/2/27.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Repository {
    String value() default "";
}
