package com.maple.server.run;

import com.maple.common.builder.Results;
import com.maple.common.entity.Result;
import com.maple.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * jdk8 多线程测试
 *
 * @author maple
 * @version 1.0
 * @since 2020-04-21 15:21
 */
@Slf4j
public class CompletableFutureTest {
    // 此executor线程池如果不传,CompletableFuture默认按cpu核心数判断启动线程数
    private ExecutorService executor = Executors.newFixedThreadPool(15);

    @Test
    public void ex() {
        long start = System.currentTimeMillis();
        //参数
        List<String> webPageLinks = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H");

        CompletableFuture<Result<String>> allFutures = null;
        for (String item : webPageLinks) {
            if (Objects.isNull(allFutures)) {
                allFutures = handle(item);
                continue;
            }
            // 存在失败的就返回失败 否则返回下一个结果
            allFutures = allFutures.thenCombine(handle(item),
                    (r1, r2) -> r1.isFail() ? r1 : r2);
        }
        Objects.requireNonNull(allFutures);
        allFutures.join();
        log.info("所有线程已执行完[{}]", allFutures.isDone());
        allFutures.whenComplete((aVoid, throwable) -> {
            log.info("执行最后一步操作");
            long end = System.currentTimeMillis();
            log.info("耗时:" + (end - start) / 1000L);
        });
    }

    //executor 可不传入,则默认最多3个线程
    private CompletableFuture<Result<String>> handle(String pageLink) {
        //捕捉异常,不会导致整个流程中断
        return CompletableFuture.supplyAsync(() -> {
            log.info("执行任务" + pageLink);
            if (StringUtils.equalsIgnoreCase(pageLink, "A")) {
                throw new RuntimeException("错误了");
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Results.success(pageLink + "成功");
        }, executor).exceptionally(throwable -> {
            log.info("线程[{}]发生了异常, 继续执行其他线程,错误详情[{}]", Thread.currentThread().getName(), throwable.getMessage());
            return Results.failure(new ServiceException("执行异常"));
        });
    }
}
