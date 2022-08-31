package com.atguigu.gmall.item.cache.impl;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.cache.CacheOpsService;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: cxz
 * @create； 2022-08-30 22:53
 **/
@Service
public class CacheOpsServiceImpl implements CacheOpsService {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RedissonClient redissonClient;
    /**
     * 查询Redis缓存
     * @param
     * @return
     */
    @Override
    public <T> T getCacheData(String cacheKey, Class<T> clz) {
        String detail = redisTemplate.opsForValue().get(cacheKey);
        //引入空值机制
        if (SysRedisConst.NULL_VAL.equals(detail)){
            return null;
        }
        T t = Jsons.toObj(detail, clz);
        return t;
    }

    /**
     * 询问布隆过滤器
     * @param skuId
     * @return
     */
    @Override
    public boolean bloomContains(Long skuId) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);

        return bloomFilter.contains(skuId);
    }

    /**
     * 获取分布式锁
     * @param skuId
     * @return
     */
    @Override
    public boolean tryLock(Long skuId) {
        //1.准备锁的key
        String lockKey = SysRedisConst.LOCK_SKU_DETAIL + skuId;
        RLock lock = redissonClient.getLock(lockKey);
        //2.尝试加锁
        boolean tryLock = lock.tryLock();
        return tryLock;
    }

    /**
     * 将数据放入缓存
     * @param cacheKey
     * @param detail
     */
    @Override
    public void cacheData(String cacheKey, Object detail) {
        //1.防止布隆过滤器误判，先判断
        SkuDetailTo skuDetailTo = (SkuDetailTo)detail;
        if(skuDetailTo.getSkuInfo() == null){
            //2.将空值放入放入缓存，缓存时间30分钟
            redisTemplate.opsForValue().set(cacheKey,
                    SysRedisConst.NULL_VAL,
                    SysRedisConst.NULL_VAL_TTL, TimeUnit.SECONDS);
        }else{
            String dataStr = Jsons.toStr(detail);
            //3.将数据放入缓存缓存时间7天
            redisTemplate.opsForValue().set(cacheKey,
                    dataStr,
                    SysRedisConst.SKUDETAIL_TTL,
                    TimeUnit.SECONDS);
        }
    }

    /**
     * 解锁
     * @param skuId
     */
    @Override
    public void unlock(Long skuId) {
        String lockKey = SysRedisConst.LOCK_SKU_DETAIL + skuId;
        RLock lock = redissonClient.getLock(lockKey);
        //解锁
        lock.unlock();
    }
}
