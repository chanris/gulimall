package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.utils.ConvertUtils;
import com.chanris.gulimall.coupon.dao.SeckillSessionDao;
import com.chanris.gulimall.coupon.dao.SeckillSkuRelationDao;
import com.chanris.gulimall.coupon.dto.SeckillSessionDTO;
import com.chanris.gulimall.coupon.entity.SeckillSkuRelationEntity;
import com.chanris.gulimall.coupon.service.SeckillSessionService;
import com.chanris.gulimall.coupon.entity.SeckillSessionEntity;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.coupon.service.SeckillSkuRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 秒杀活动场次
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class SeckillSessionServiceImpl extends CrudServiceImpl<SeckillSessionDao, SeckillSessionEntity, SeckillSessionDTO> implements SeckillSessionService {

    @Resource
    private SeckillSessionDao seckillSessionDao;
    @Resource
    private SeckillSkuRelationDao seckillSkuRelationDao;

    @Override
    public QueryWrapper<SeckillSessionEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SeckillSessionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<SeckillSessionEntity> getLatest3DaySession() {
        List<SeckillSessionEntity> list = seckillSessionDao.selectList(new QueryWrapper<SeckillSessionEntity>().between("start_time", startTime(), endTime()));
        if (list != null && !list.isEmpty()) {
            list.forEach(session -> {
                Long id = session.getId();
                List<SeckillSkuRelationEntity> relationSkuRelation = seckillSkuRelationDao.selectList(new QueryWrapper<SeckillSkuRelationEntity>().eq("promotion_session_id", id));
                session.setRelationSkus(relationSkuRelation);
            });
        }
        return list;
    }

    private String startTime() {
        LocalDate now = LocalDate.now();
        LocalTime min = LocalTime.MIN;
        LocalDateTime startTime = LocalDateTime.of(now, min);
        String format = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return format;
    }

    private String endTime() {
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(2);
        LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
        String format = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return format;
    }
}