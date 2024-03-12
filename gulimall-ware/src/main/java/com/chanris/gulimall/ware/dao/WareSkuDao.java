package com.chanris.gulimall.ware.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.ware.entity.WareSkuEntity;
import com.chanris.gulimall.ware.vo.SkuHasStockVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface WareSkuDao extends BaseDao<WareSkuEntity> {

    List<SkuHasStockVo> getSkuHasStock(@Param("skuIds") List<Long> skuIds);

    List<Long> listWareIdHasSkuStock(Long skuId);

    long lockSkuStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num);
}