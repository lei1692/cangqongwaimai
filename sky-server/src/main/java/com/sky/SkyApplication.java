package com.sky;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
@EnableCaching

public class SkyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkyApplication.class, args);
        log.info("server started");
    }
}
