package com.zerobase.StockDividend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    // 여러개의 쓰레드를 관리
    // 하나의 쓰레드를 쓰니까 스케쥴에 이슈가 생김 
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();

        int n = Runtime.getRuntime().availableProcessors();
        threadPool.setPoolSize(n);
        threadPool.initialize();

        taskRegistrar.setTaskScheduler(threadPool);
    }
}
