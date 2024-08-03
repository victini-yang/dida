package com.victiny.dida;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/30 9:55
 */
@SpringBootTest
public class RxJavaTest {

    @Test
    public void test() throws InterruptedException {
        // 创建数据流
        Flowable<Long> flowable = Flowable.interval(1, TimeUnit.SECONDS)
                .map(i -> i + 1)
                .subscribeOn(Schedulers.io());//制定执行操作用的线程池
        // 订阅 Flowable 流，并且打印出每个接收到的数字
        flowable
                .subscribeOn(Schedulers.io())
                .doOnNext(item -> System.out.println(item.toString()))
                .subscribe();
        // 主线程睡眠，以便观察到结果
        Thread.sleep(10000L);
    }
}
