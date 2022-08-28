package com.atguigu.gmall.common.config.threadPool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: cxz
 * @create； 2022-08-28 13:31
 **/

@Configuration
@EnableConfigurationProperties(AppThreadPoolProperties.class)
public class ThreadPoolConfiguration {
    @Autowired
    AppThreadPoolProperties appThreadPoolProperties;
    @Value("${spring.application.name}")
    String applicationName;
    @Bean
    public ThreadPoolExecutor getThreadPoolExecutor(){
        return new ThreadPoolExecutor(appThreadPoolProperties.getCore(),
                appThreadPoolProperties.getMax(),
                appThreadPoolProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(appThreadPoolProperties.getQueueSize()),
                new ThreadFactory() {
                    int i = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(applicationName+"[core-thread-"+ i++ +"]");
                        return thread;
                    }
                },new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
