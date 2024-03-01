package com.chanris.gulimall.product.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.product.dao.CategoryDao;
import com.chanris.gulimall.product.dto.CategoryDTO;
import com.chanris.gulimall.product.entity.CategoryEntity;
import com.chanris.gulimall.product.service.CategoryService;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商品三级分类
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class CategoryServiceImpl extends CrudServiceImpl<CategoryDao, CategoryEntity, CategoryDTO> implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public QueryWrapper<CategoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public List<CategoryDTO> listWithTree() {
        //1. 查出所有分类
        List<CategoryDTO> dtos = this.list(new HashMap<>(0));
        //2. 组装成树结构
        List<CategoryDTO> level1Menus = dtos.stream().filter( menu -> menu.getParentCid() == 0).map(menu -> {
            menu.setChildren(getChildren(menu, dtos));
            return menu;
        }).sorted(Comparator.comparingInt(CategoryDTO::getSort)).collect(Collectors.toList());
        return level1Menus;
    }

    /**
     * 1. 每一个需要缓存的数据我们都指定要放在哪个名称下的缓存【缓存分区（按照业务类型来分）】
     * 2. @Cacheable({"category"})
     *      代表当前方法的结果需要缓存，如果缓存中有，方法不用调用。
     *      如果缓存中没有，会调用方法，最后将方法的结果放入缓存
     * 3. 默认行为
     *      1. 如果缓存中有，方法不用调用
     *      2. key默认会自动生产，缓存的名字::SimpleKey[]
     *      3. 缓存的值。默认使用jdk序列化机制。将序列化后的数据存到redis
     *    自定义
     *      1. 指定生成的缓存使用的key: key属性指定，接收一个SpEL
     *      2. 指定缓存的数据的存活时间
     *      3. 将数据保存为json格式
     *   Spring-Cache
     *   1. 读模式
     *      缓存穿透
     *      缓存击穿  加锁： ?默认不加锁
     *      缓存雪崩： 大量key同时过期： 解决：加随机时间
     *   2. 写模式 （缓存与数据库一致)
     *      1. 读写加锁
     *      2. 引入cancal，感知mysql的更新日志，同步到缓存中
     *      3. 读多写多，直接去数据库查询
     *
     *
     *
     * @return
     */
    @Cacheable(cacheNames = {"category"}, key = "#root.method.name", sync = true) // sync 是本地锁
    @Override
    public List<CategoryEntity> getTopLevelCategoryList() {
        System.out.println("getTopLevelCategoryList...");
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_cid", 0L);
        return  categoryDao.selectList(queryWrapper);
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatalogViewFromDB() {
        synchronized (this) {
            // 双重检查： 得到锁后，再查一次缓存，如果没有再去查数据库
            String cached = stringRedisTemplate.opsForValue().get("catalogJSON");
            if(StrUtil.isBlank(cached)) {
                return JSONObject.parseObject(cached, new TypeReference<Map<String, List<Catelog2Vo>>>() {
                    @Override
                    public Map<String, List<Catelog2Vo>> parseObject(String text) {
                        return super.parseObject(text);
                    }
                });
            }

            /*
            * 业务优化
            * 1. 将数据的多次查询变为一次
            * */
            List<CategoryEntity> categoryEntities = categoryDao.selectList(null);

            List<CategoryEntity> categoryEntityList = getParent_cid(categoryEntities, 0L);

            // 1.查出所有一级分类
            //List<CategoryEntity> categoryEntityList = this.getTopLevelCategoryList();

            // 2.封装数据
            Map<String, List<Catelog2Vo>> map = categoryEntityList.stream().collect(Collectors.toMap(k-> k.getCatId().toString(), v -> {
    //            List<CategoryEntity> entities = categoryDao.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
                List<CategoryEntity> entities = getParent_cid(categoryEntities, v.getCatId());
                List<Catelog2Vo> parentCid = null;
                if (entities != null) {
                    parentCid = entities.stream().map(item -> {
                        Catelog2Vo vo = new Catelog2Vo(v.getCatId().toString(), null, "" + item.getCatId(), item.getName());
                        // 给当前二级分类找三级分类list
                        //List<CategoryEntity> level3Catalog = categoryDao.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", item.getCatId()));
                        List<CategoryEntity> level3Catalog = getParent_cid(categoryEntities, item.getCatId());
                        if (level3Catalog.size() != 0) {
                            List<Catelog2Vo.Category3Vo> vos = level3Catalog.stream().map(c3 -> {
                                Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo("" + item.getCatId(), "" + c3.getCatId(), c3.getName());
                                return category3Vo;
                            }).toList();
                            vo.setCatalog3List(vos);
                        }
                        return vo;
                    }).toList();
                }else {
                    parentCid = new ArrayList<>(0);
                }
                return parentCid;
            }));

            // 存缓存
            String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
            if(StrUtil.isBlank(catalogJSON)) {
                String cache = JSON.toJSONString(map);
                stringRedisTemplate.opsForValue().set("catalogJSON", cache);
            }
            return map;
        }
    }


    @Override
    public Map<String, List<Catelog2Vo>> getCatalogView() {
        /**
         * 1. 空结果缓存：解决缓存穿透
         * 2. 设置过期时间（加随机值）：阶级缓存崩溃
         * 3. 加锁：解决缓存击穿
         */
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        Map<String, List<Catelog2Vo>> result;
        if(StrUtil.isBlank(catalogJSON)) {
            return getCatalogViewFromDB();
        }
        result = JSONObject.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            @Override
            public Map<String, List<Catelog2Vo>> parseObject(String text) {
                return super.parseObject(text);
            }
        });
        return result;
    }

    /**
     * 获得分类树，使用分布式锁
     * 阶段一
     * 获得锁，setnx("lock", "111")
     * 当某个线程获得锁后=>执行业务=>删除锁， 其他没有获得锁的线程，等待100ms重试（自旋），直到获得锁；
     * 这样做会有一个问题：
     * 1. setnx占好了位置，业务代码异常退出或者服务器断电造成的 程序中断 可能会导致 当前获得锁的线程没有释放锁，这就造成了死锁。
     * 2. 解决 设置锁的自动过期，即使没有删除，会自动删除
     *
     * 阶段二
     * 获得锁，setnx("lock", "111") setExpire("lock", 300s)
     * 问题：加锁 和设置过期时间 是非原子性的，也会有问题
     *
     * 阶段三
     * 获得锁，set ex 300  nx lock 111, 将两条语句放在一条语句中执行
     * 问题：业务超时，删除锁，有问题
     *
     * 阶段四
     * 获得锁，set ex 300  nx lock UUID, 将 锁的值 设为 UUID
     *
     * @return
     */
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogViewWithLock() {
//        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", "111");
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if(Boolean.TRUE.equals(lock)) {
            Map<String, List<Catelog2Vo>> result = null;
            try {
                String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
                if(StrUtil.isBlank(catalogJSON)) {
                    result =  getCatalogViewFromDB();
                }else {
                    result = JSONObject.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
                        @Override
                        public Map<String, List<Catelog2Vo>> parseObject(String text) {
                            return super.parseObject(text);
                        }
                    });
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                // 释放分布式锁 条件： 有锁 && 锁的 值 为 自己设置的 UUID 字符串
                // TODO 这样删除锁 也有并发问题 必须要把 判断和删除操作 设置为 原子操作
               /* String lockVal = stringRedisTemplate.opsForValue().get("lock");
                if(lockVal != null && lockVal.equals(uuid)) {
                    stringRedisTemplate.delete("lock");
                }*/
                // DONE 使用 lua 脚本 将 redis 多个命令 写到一个事务中，这样判断和删除 就是原子操作
                String releaseLockScript = "if redis.call('get', KEYS[1] == ARGV[1] then return redis.call('del', KEYS[1])) else return 0 end";
                Integer lock1 = stringRedisTemplate.execute(new DefaultRedisScript<Integer>(releaseLockScript, Integer.class), List.of("lock"), uuid);
            }
            return result;
        }else {
            // 加锁失败，重试 《自旋》
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                // throw new RuntimeException(e);
            }
            return getCatalogViewWithLock();
        }
    }

    // @Caching 同时进行多种缓存操作
    @Caching(evict = {@CacheEvict(value = "category", key = "'getLevel1Categorys'"),
            @CacheEvict(value = "category", key = "getCatalogJson")})
    @Override
    public void updateCascade(CategoryEntity categoryEntity) {
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(categoryEntity, categoryDTO);
        this.update(categoryDTO);

    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> entities, Long cid) {
        List<CategoryEntity> result = null;
        if (entities == null || entities.size() == 0)  {
            result = new ArrayList<>(0);
            return result;
        }
        result = entities.stream().filter(item -> item.getParentCid().equals(cid)).toList();
        return result;
    }

    private List<CategoryDTO> getChildren(CategoryDTO root, List<CategoryDTO> all) {
        List<CategoryDTO> menus = all.stream().filter(dto -> dto.getParentCid().equals(root.getCatId())).map(dto -> {
            // 递归找到所有的分类的子分类列表
            dto.setChildren(getChildren(dto, all));
            return dto;
        }).sorted(Comparator.comparingInt(CategoryDTO::getSort)).collect(Collectors.toList());
        return menus;
    }
}