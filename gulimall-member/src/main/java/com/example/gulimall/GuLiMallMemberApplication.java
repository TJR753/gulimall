package com.example.gulimall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com/example/gulimall/member/dao")
@EnableDiscoveryClient
public class GuLiMallMemberApplication {
   public static void main(String[] args) {
       SpringApplication.run(GuLiMallMemberApplication.class, args);
   }
}
