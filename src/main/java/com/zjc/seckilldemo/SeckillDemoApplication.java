package com.zjc.seckilldemo;

import cn.wildfirechat.sdk.AdminConfig;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zjc.seckilldemo.mapper")
public class SeckillDemoApplication  {
    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoApplication.class, args);
    }
}
