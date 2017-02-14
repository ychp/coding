package com.ychp.rpc.annotation;

import java.lang.annotation.*;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
@Target({ElementType.TYPE})
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
