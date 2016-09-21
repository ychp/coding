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
public @interface RpcConsumer {

    String group() default "";

    String version() default "";

    String timeout() default "";

    String check() default "";

    int retries() default 0;

    String directUrl() default "";
}
