package com.ychp.coding.rpc.annotation;

import java.lang.annotation.*;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/21
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RpcProvider {

    Class<?> interfaceClass() default void.class;

    String interfaceName() default "";

    String group() default "";

    String version() default "";

    String export() default "";
}
