package com.zjc.seckilldemo;

import cn.wildfirechat.sdk.AdminConfig;
import com.zjc.seckilldemo.util.UserUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zjc.seckilldemo.mapper")
public class SeckillDemoApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("启动IM能力");
        AdminConfig.initAdmin("http://localhost:18080","123465");
    }
}
