package com.jinhx.java.aspect;

import java.lang.annotation.*;

/**
 * TimeConsuming
 *
 * @author jinhx
 * @since 2022-01-13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimeConsuming {

    /**
     * 是否打印参数
     */
    boolean printParams() default true;

}
