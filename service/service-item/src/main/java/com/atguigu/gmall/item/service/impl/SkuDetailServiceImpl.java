package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.cache.CacheOpsService;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    //Map<Long,SkuDetailTo> map = new ConcurrentHashMap<>();
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    CacheOpsService cacheOpsService;

    /**
     * 查询商品详情
     *
     * @param skuId
     * @return
     */
    public SkuDetailTo getSkuDetail1(Long skuId) {
        SkuDetailTo detailTo = new SkuDetailTo();
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            //1.查询skuinfo信息
            Result<SkuInfo> skuInfo = skuDetailFeignClient.getSkuInfo(skuId);
            detailTo.setSkuInfo(skuInfo.getData());
            return skuInfo.getData();
        }, threadPoolExecutor);
        CompletableFuture<Void> skuImagesFuture = skuInfoFuture.thenAcceptAsync(r -> {
            if (r != null) {
                //2.查询sku图片信息
                Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
                r.setSkuImageList(skuImages.getData());
            }
        }, threadPoolExecutor);

        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync(r -> {
            if (r != null) {
                //3.查询商品的完整分类信息
                Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(r.getCategory3Id());
                detailTo.setCategoryView(categoryView.getData());
            }

        }, threadPoolExecutor);

        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            //4.查询实时价格
            Result<BigDecimal> price = skuDetailFeignClient.get1010Price(skuId);
            detailTo.setPrice(price.getData());
        }, threadPoolExecutor);

        CompletableFuture<Void> saleAttrAndValueMarkSkuFuture = skuInfoFuture.thenAcceptAsync(r -> {
            if (r != null) {
                //5.查询当前sku对应的spu定义的所有销售属性名和值（固定好顺序）并且标记好当前sku属于哪一种组合
                Result<List<SpuSaleAttr>> saleAttrAndValueMarkSku = skuDetailFeignClient.getSaleAttrAndValueMarkSku(skuId, r.getSpuId());
                detailTo.setSpuSaleAttrList(saleAttrAndValueMarkSku.getData());
            }
        }, threadPoolExecutor);

        CompletableFuture<Void> allSkuSaleAttrValueJsonFuture = skuInfoFuture.thenAcceptAsync(r -> {
            if (r != null) {
                //6. 商品（sku）的所有兄弟产品的销售属性名和值组合关系全部查出来，并封装成
                Result<String> allSkuSaleAttrValueJson = skuDetailFeignClient.getAllSkuSaleAttrValueJson(r.getSpuId());
                detailTo.setValuesSkuJson(allSkuSaleAttrValueJson.getData());
            }
        }, threadPoolExecutor);

        CompletableFuture.allOf(skuImagesFuture, categoryViewFuture, priceFuture
                , saleAttrAndValueMarkSkuFuture, allSkuSaleAttrValueJsonFuture).join();

        return detailTo;
    }

    /**
     * 将商品详情缓存在Redis中
     *
     * @param skuId
     * @return
     */
    public SkuDetailTo getSkuDetail2(Long skuId) {
        String detail = stringRedisTemplate.opsForValue().get("skuInfo:item:product" + skuId);
        if ("x".equals(detail)) {
            return null;
        }
        if (StringUtils.isEmpty(detail)) {

            SkuDetailTo detailTo = getSkuDetail1(skuId);
            String cacheJson = "x";
            if (detailTo == null) {
                stringRedisTemplate.opsForValue().set("skuInfo:item:product" + skuId, cacheJson);
            } else {
                String skuInfoStr = Jsons.toStr(detailTo);
                stringRedisTemplate.opsForValue().set("skuInfo:item:product" + skuId, skuInfoStr);
            }
            return detailTo;
        }
        return Jsons.toObj(detail, SkuDetailTo.class);
    }

    /**
     * 将商品缓存到到Redis中整合redisson增加布隆过滤器和分布式锁解决缓存穿透，和缓存击穿问题
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        String cacheKey = SysRedisConst.SKU_INFO_PREFIX + skuId;
        //1.先从缓存中获取
        SkuDetailTo skuDetailTo = cacheOpsService.getCacheData(cacheKey + skuId,SkuDetailTo.class);
        //2.判断缓存中有没有
        if (skuDetailTo == null) {
            //3.缓存中没有，查看布隆过滤器
            boolean contains = cacheOpsService.bloomContains(skuId);
            if (!contains) {
                //4.布隆过滤器中没有,一定没有
                log.info("[{}]商品 - 布隆判定没有，检测到隐藏的攻击风险....", skuId);
                return null;
            }
                //5.布隆过滤器中有，获取分布式锁
                boolean lock = cacheOpsService.tryLock(skuId);
                if (lock) {
                    //6.抢到锁进行回源
                    log.info("[{}]商品 缓存未命中，布隆说有，准备回源.....", skuId);
                    SkuDetailTo detail = getSkuDetail1(skuId);
                    //7.将数据放入缓存
                    cacheOpsService.cacheData(cacheKey, detail);
                    //8.解锁
                    cacheOpsService.unlock(skuId);
                    return detail;
                }
                    //10.没抢中锁
                    try {
                        Thread.sleep(1000);
                      return cacheOpsService.getCacheData(cacheKey + skuId,SkuDetailTo.class);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        //缓存中有直接返回
        return skuDetailTo;
    }

    /**
     * 将查询数据缓存到map中
     * @param skuId
     * @return
     *
     @Override public SkuDetailTo getSkuDetail(Long skuId) {
     SkuDetailTo detailTo = map.get(skuId);
     if (detailTo == null){
     SkuDetailTo skuDetail1 = getSkuDetail1(skuId);
     map.put(skuId,skuDetail1);
     return skuDetail1;
     }
     return detailTo;
     }
     */
}
