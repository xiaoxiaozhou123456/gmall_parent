package com.atguigu.gmall.product.init;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.product.service.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author: cxz
 * @create； 2022-08-31 16:10
 **/
@Service
@Slf4j
public class SkuIdBloomInitService {
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    SkuInfoService skuInfoService;
    /**
     * 项目一启动，就初始化布隆过滤器
     */
    @PostConstruct
    public void initBloom(){
        log.info("布隆过滤器初始化中-------");
        //1.查询所有skuId
       List<Long> skuIds = skuInfoService.findAllSkuId();
       //2.初始化布隆过滤器
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);
        //3。判断布隆过滤器是否存在
        boolean exists = bloomFilter.isExists();
        if (!exists) {
            //如果不存在，初始化参数
            bloomFilter.tryInit(50000,0.0001);//期望插入数据量，误判率
        }
        for (Long skuId : skuIds) {
            //把所有id添加到布隆过滤器中站位
            bloomFilter.add(skuId);
        }
        log.info("布隆初始化完成....，总计添加了 {} 条数据",skuIds.size());

    }
}
