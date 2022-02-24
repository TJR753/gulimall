package com.example.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.gulimall.product.entity.BrandEntity;
import com.example.gulimall.product.service.BrandService;
import org.junit.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GuliMallProductApplicationTest {

    @Autowired
    private BrandService brandService;

    @Test
    public void saveTest(){
        BrandEntity brand = new BrandEntity();
        brand.setDescript("华为");
        brand.setName("hello");
        brandService.save(brand);
    }
    @Test
    public void updateTest(){
        BrandEntity brand = new BrandEntity();
        brand.setBrandId(1L);
        brand.setDescript("百度");
        brandService.updateById(brand);
    }
    @Test
    public void selectTest(){
        System.out.println(brandService.getOne(new QueryWrapper<BrandEntity>().eq("brand_id", 1L)));
    }
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void redisTest(){
        stringRedisTemplate.opsForValue().set("a","a");
        System.out.println(stringRedisTemplate.opsForValue().get("a"));
    }

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redissonTest(){
        System.out.println(redissonClient);
    }
}
