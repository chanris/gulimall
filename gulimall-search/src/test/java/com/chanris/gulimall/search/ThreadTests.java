package com.chanris.gulimall.search;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

/**
 * @author chenyue7@foxmail.com
 * @date 5/3/2024
 * @description 线程池: [ExecutorService]
 * 给线程池直接提交任务。
 * service.execute(new Runnable01())
 * 1.创建线程池
 * 1）、Executors
 * 2）、new ThreadPoolExecutor
 * 创建线程池的七大参数:
 * corePoolSize – the number of threads to keep in the pool, even if they are idle, unless allowCoreThreadTimeOut is set
 * maximumPoolSize – the maximum number of threads to allow in the pool
 * keepAliveTime – when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating.
 * TimeUnit unit – the time unit for the keepAliveTime argument
 * BlockingQueue<Runnable> workQueue – 阻塞队列，用于放置任务
 * ThreadFactory 创建线程的工厂
 * RejectedExecutionHandler 拒绝策略：如果线程数满了，拒绝任务
 * <p>
 * 运行流程：
 * 1. 线程池创建，准备好core数量的核心线程，准备接受任务
 * 2. 新的任务进来，用core准备好空闲的线程执行
 * 1）cor满了，就将再进来的任务放入阻塞队列。空闲的core就会自己去阻塞队列获得任务
 * 2）阻塞队列满了，就直接开新线程执行，最大只能开到max指定的数量
 * 3）max都执行好了。max-core数量空闲的线程就会在keepAliveTime指定的时间后自动销毁。最终保存到core的大小
 * 4）如果线程数开到了max的数量，还有新任务进来，就会使用reject指定的拒绝策略进行处理
 * 3. 所有的线程创建都是由指定的factory创建的。
 */
@SpringBootTest
public class ThreadTests {

    @Test
    void test01() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                200, 10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }

    @Test
    void test02() throws ExecutionException, InterruptedException {
        System.out.println("main...start...");
        ExecutorService executor = Executors.newFixedThreadPool(5);
     /*   CompletableFuture<Integer> future = CompletableFuture.supplyAsync(()->{
            System.out.println("当前线程: " + Thread.currentThread().getId());
            int i = 10 /2;
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("运行结果: " + i);
            return i;
        }, executor).whenComplete((res, exception)-> {
            System.out.println("异步任务完成了....结果是：" + res + ";异常是：" + exception);
        }).exceptionally((throwable)-> {
            // 如果出现异常，返回默认值
            return 10;
        });*/
//        Integer res = future.get();


        /**
         * handle方法: 任务完成的处理
         */
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程: " + Thread.currentThread().getId());
            int i = 10 / 0;
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("运行结果: " + i);
            return i;
        }, executor).handle((res, thr)->{
            if(thr != null) {
                return 0;
            }
            return res;
        });
        TimeUnit.MILLISECONDS.sleep(10000);
        System.out.println("main...end...");
    }


    @Test
    void test03() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(()->{
            System.out.println("查询商品图片信息");
            return "hello.jpg";
        }, executor);
        CompletableFuture<String> futureAttrs = CompletableFuture.supplyAsync(()->{
            System.out.println("查询商品属性");
            return "黑色+256g";
        }, executor);
        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(()->{
            System.out.println("查询商品图片信息");
            return "华为";
        }, executor);
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureImg, futureDesc, futureAttrs);
        Void unused = allOf.get();//注释
        System.out.println("main...end...");
    }
}
