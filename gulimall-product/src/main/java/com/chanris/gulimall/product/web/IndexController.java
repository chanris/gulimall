package com.chanris.gulimall.product.web;

import com.chanris.gulimall.product.entity.CategoryEntity;
import com.chanris.gulimall.product.service.CategoryService;
import com.chanris.gulimall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author chenyue7@foxmail.com
 * @date 28/2/2024
 * @description
 */
@Slf4j
@Controller
public class IndexController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = {"/", "/index.html"})
    public String index(Model model) {
        // 1.查出所有的一级分类
        List<CategoryEntity> categoryEntityList = categoryService.getTopLevelCategoryList();
        // 视图解析器进行拼接： classpath:/templates/ + 返回值 + .html
        model.addAttribute("categories", categoryEntityList);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogView() {
        Map<String, List<Catelog2Vo>> catalogView = categoryService.getCatalogView();
        return catalogView;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        // 1. 获得锁
        RLock lock = redissonClient.getLock("my-lock");

        // 2. 加锁
        lock.lock(30, TimeUnit.SECONDS); // 阻塞式等待, 指定自动释放锁的时间
        // 1. 锁的自动续期，默认30s
        // 2. 加锁完成只要运行完成，就不会自动续期
        try {
            System.out.println("加锁成功");
            TimeUnit.MILLISECONDS.sleep(3000);
        }catch (Exception exception ) {}
        finally {
            lock.unlock();
        }
        return "hello";
    }

    /**
     * 读写锁： 写锁是一个排他锁，读锁是一个共享锁
     * 读 + 读： 相当于 无锁
     * 写 + 读： 等待写锁释放
     * 写 + 写： 等待写锁释放
     * 读 + 写： 等待读锁释放
     * @return
     */
    @ResponseBody
    @GetMapping("/write")
    public String write() {
        // 获得读写锁
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        String s = "";
        // 获得写锁
        RLock rLock = lock.writeLock();
        try {
            // 写上锁
            rLock.lock(); // 如果读 上锁了，也会等待读锁的释放，再写上锁
            s = UUID.randomUUID().toString();
            TimeUnit.MILLISECONDS.sleep(30000);
            stringRedisTemplate.opsForValue().set("writeValue", s);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            rLock.unlock();
        }
        return s;
    }


    @ResponseBody
    @GetMapping("/read")
    public String read() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        String s = "";
        // 获得读锁
        RLock rLock = lock.readLock();
        try {
            // 读上锁
            rLock.lock(); // 如果写上锁了，会等待写锁 释放
            TimeUnit.MILLISECONDS.sleep(30000);
            s = stringRedisTemplate.opsForValue().get("writeValue");
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }

    /**
     *
     * 信号量
     * 车库停车
     * 限量
     */
    public String park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.acquire(); // 阻塞式获得 一个车位
        boolean b = park.tryAcquire();//  非阻塞式 获得，返回获取结果
        if(b) {
            //执行业务
        }else {
            // 报错
        }

        return null;
    }

    public String go() {
        RSemaphore go = redissonClient.getSemaphore("park");
        go.release(); //释放一个车位
        return null;
    }

    // 闭锁 CountDownLock
    // 应用场景： 放假，锁门： 等所有人都走完了才锁门
    @ResponseBody
    @GetMapping("/lockDoor")
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);
        door.await(); // 等待闭锁都完毕
      return "放假了";
    }

    @ResponseBody
    @GetMapping("/gogo/{classId}")
    public String gogo(@PathVariable Integer classId) {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown(); // 计数减一
        return  classId + "走完了";
    }
}
