package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.config.threadPool.ThreadPoolConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ThreadPoolConfiguration.class)
public @interface EnableThreadPool {
}
