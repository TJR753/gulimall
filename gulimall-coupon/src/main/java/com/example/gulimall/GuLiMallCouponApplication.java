package com.example.gulimall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com/example/gulimall/coupon/dao")
@EnableDiscoveryClient
public class GuLiMallCouponApplication {
    public static void main(String[] args){
      SpringApplication.run(GuLiMallCouponApplication.class,args);
    }
}
