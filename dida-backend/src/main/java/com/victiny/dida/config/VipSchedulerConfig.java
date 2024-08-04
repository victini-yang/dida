package com.victiny.dida.config;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/8/3 10:56
 */
@Configuration
@Data
public class VipSchedulerConfig {

    // 使用 AtomicInteger 确保线程编号是线程安全的
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * 配置一个 Scheduler Bean，用于 VIP 线程池调度。
     *
     * @return 配置的 Scheduler 实例
     */
    @Bean
    public Scheduler vipScheduler() {
        // 创建一个自定义的 ThreadFactory
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(@NotNull Runnable r) {
                // 创建一个新的线程，线程名为 "VIPThreadPool-" 后加上当前线程编号
                Thread thread = new Thread(r, "VIPThreadPool-" + threadNumber.getAndIncrement());
                thread.setDaemon(false); // 设置线程为非守护线程
                return thread;
            }
        };
        // 创建一个拥有 10 个线程的 ScheduledExecutorService
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10, threadFactory);
        // 将 ScheduledExecutorService 转换为 Scheduler 并返回
        return Schedulers.from(scheduledExecutorService);
    }
}

