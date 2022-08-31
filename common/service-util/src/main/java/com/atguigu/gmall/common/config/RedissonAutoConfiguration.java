package com.atguigu.gmall.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author: cxz
 * @create； 2022-08-30 21:59
 **/
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedissonAutoConfiguration {
    @Resource
    RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();
        config.useSingleServer()
                .setAddress("redis://"+host+":"+port)
                .setPassword(password);

        RedissonClient client = Redisson.create(config);
        return client;
    }
}
