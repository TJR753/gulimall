package com.example.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan(value = "com/example/gulimall/product/dao")
@EnableDiscoveryClient
public class GuliMallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuliMallProductApplication.class, args);
    }

}
