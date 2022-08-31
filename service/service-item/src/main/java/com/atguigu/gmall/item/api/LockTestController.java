package com.atguigu.gmall.item.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.lock.RedisDistLock;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: cxz
 * @create； 2022-08-30 19:34
 **/

@RestController
@RequestMapping("/lock")
public class LockTestController {
    @Autowired
    RedisDistLock redisDistLock;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RedissonClient redissonClient;
    int i = 0;
    //Lock lock = new ReentrantLock();

    @GetMapping("aa")
    public Result method() throws InterruptedException {
        RLock lock = redissonClient.getLock("lock-hello");
        System.out.println("拿到锁");
        lock.lock();
        Thread.sleep(8000);
        lock.unlock();
        System.out.println("解锁");
        return Result.ok();
    }
    @GetMapping("1")
    public Result countDownLatchTest1(){
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("sl-lock");
        countDownLatch.countDown();
        return Result.ok("得到一颗龙珠");
    }
    @GetMapping("2")
    public Result method2() throws InterruptedException {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("sl-lock");
        countDownLatch.trySetCount(7);
        countDownLatch.await();
        return Result.ok("神龙出");
    }

    @GetMapping("/incr")
    public Result increment(){
        //lock.lock();本地锁
        String lock = redisDistLock.lock();

            String a = redisTemplate.opsForValue().get("a");
            int i = Integer.parseInt(a);
            i++;
            redisTemplate.opsForValue().set("a",i+"");

           // lock.unlock();
            redisDistLock.unlock(lock);

        return Result.ok();
    }

}
