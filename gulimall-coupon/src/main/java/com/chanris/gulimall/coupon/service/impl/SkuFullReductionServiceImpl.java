package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.to.MemberPrice;
import com.chanris.gulimall.common.to.SkuReductionTo;
import com.chanris.gulimall.coupon.dao.SkuFullReductionDao;
import com.chanris.gulimall.coupon.dto.SkuFullReductionDTO;
import com.chanris.gulimall.coupon.entity.MemberPriceEntity;
import com.chanris.gulimall.coupon.entity.SkuLadderEntity;
import com.chanris.gulimall.coupon.service.MemberPriceService;
import com.chanris.gulimall.coupon.service.SkuFullReductionService;
import com.chanris.gulimall.coupon.entity.SkuFullReductionEntity;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品满减信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class SkuFullReductionServiceImpl extends CrudServiceImpl<SkuFullReductionDao, SkuFullReductionEntity, SkuFullReductionDTO> implements SkuFullReductionService {

    @Resource
    private SkuLadderService skuLadderService;
    @Resource
    private MemberPriceService memberPriceService;

    @Override
    public QueryWrapper<SkuFullReductionEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SkuFullReductionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        // 保存满减打折、会员价
        // sku的优惠、满减等信息 gulimall_sms --> sms_sku_ladder、sms_sku_full_reduction、sms_member_price
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionTo, skuLadderEntity);
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());

        if(skuReductionTo.getFullCount() > 0) {
            skuLadderService.insert(skuLadderEntity);
        }

        // sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        if(skuFullReductionEntity.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
            this.insert(skuFullReductionEntity);
        }

        // sms_member_price
        List<MemberPrice> memberPriceList = skuReductionTo.getMemberPrice();

        List<MemberPriceEntity> collect = memberPriceList.stream().map(mem -> {
            MemberPriceEntity priceEntity = new MemberPriceEntity();
            priceEntity.setSkuId(skuReductionTo.getSkuId());
            priceEntity.setMemberLevelId(mem.getId());
            priceEntity.setMemberLevelName(mem.getName());
            priceEntity.setMemberPrice(mem.getPrice());
            priceEntity.setAddOther(1);
            return priceEntity;
        }).filter(item -> item.getMemberPrice().compareTo(BigDecimal.ZERO) > 0).toList();

        memberPriceService.insertBatch(collect);
    }
}