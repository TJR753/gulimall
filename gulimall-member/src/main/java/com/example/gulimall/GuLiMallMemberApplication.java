package com.example.gulimall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com/example/gulimall/member/dao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.gulimall.member.feign")
public class GuLiMallMemberApplication {
   public static void main(String[] args) {
       SpringApplication.run(GuLiMallMemberApplication.class, args);
   }
}
