package com.example.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com/example/gulimall/order/dao")
@EnableDiscoveryClient
public class GuliMallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuliMallOrderApplication.class, args);
    }

}
