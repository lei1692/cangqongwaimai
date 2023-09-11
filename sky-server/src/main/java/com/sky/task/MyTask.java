package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author leiwenfeng
 * Date: 2023/9/12 3:33
 */
@Component
@Slf4j
public class MyTask {
    //@Scheduled(cron = "0/5 * * * *  ?")
    public void executeTask(){
        log.info("定时任务执行：{}", LocalDateTime.now());
    }
}
