package com.atguigu.gmall.item.cache;

import com.atguigu.gmall.model.to.SkuDetailTo;

public interface CacheOpsService {
    <T> T getCacheData(String s,Class<T> clz);

    boolean bloomContains(Long skuId);

    boolean tryLock(Long skuId);

    void cacheData(String cacheKey, Object detail);

    void unlock(Long skuId);
}
