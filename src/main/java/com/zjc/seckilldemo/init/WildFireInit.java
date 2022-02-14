package com.zjc.seckilldemo.init;

import cn.wildfirechat.sdk.AdminConfig;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class WildFireInit implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        AdminConfig.initAdmin("http://localhost:18080","123465");
        System.out.println("启动IM能力");
    }
}
