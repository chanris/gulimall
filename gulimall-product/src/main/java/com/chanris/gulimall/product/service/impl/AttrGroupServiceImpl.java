package com.chanris.gulimall.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.chanris.gulimall.product.dao.AttrDao;
import com.chanris.gulimall.product.dao.AttrGroupDao;
import com.chanris.gulimall.product.dto.AttrDTO;
import com.chanris.gulimall.product.dto.AttrGroupDTO;
import com.chanris.gulimall.product.entity.AttrGroupEntity;
import com.chanris.gulimall.product.service.AttrGroupService;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.product.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Slf4j
@Service
public class AttrGroupServiceImpl extends CrudServiceImpl<AttrGroupDao, AttrGroupEntity, AttrGroupDTO> implements AttrGroupService {

    @Resource
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Resource
    private AttrGroupDao attrGroupDao;

    @Resource
    private AttrDao attrDao;


    /**
     * AttrGroupServiceImpl调用的page 的所有QueryWrapper查询条件定义在这里
     * @param params
     * @return
     */
    @Override
    public QueryWrapper<AttrGroupEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        String attrGroupName = (String)params.get("attrGroupName");

        Object cid = params.get("catelogId");
        Long catelogId = null;
        if (cid instanceof String && ! cid.equals("")) {
            catelogId = Long.parseLong((String) cid);
        }else if(cid instanceof Number) {
            catelogId = (Long) cid;
        }
        log.info("通用 page 查询， QueryWrapper: attrGroupName: {}", attrGroupName);
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        // wrapper.eq(boolean condition, string column, object value);
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);
        wrapper.like(StrUtil.isNotBlank(attrGroupName), "attr_group_name", attrGroupName);
        wrapper.eq(ObjectUtil.isNotNull(catelogId), "catelog_id", catelogId);
        return wrapper;
    }

    @Override
    @Transactional
    public void deleteWithRelation(Long[] attrGroupIds) {
        this.delete(attrGroupIds);
        // 删除attrgroup 与 attr的关联
        attrAttrgroupRelationDao.deleteByAttrGroupIds(List.of(attrGroupIds));
    }

    @Override
    public List<AttrGroupDTO> attrGroupWithAttr(Long catelogId) {
        List<AttrGroupDTO> list = attrGroupDao.getAttrGroupListWithAttr(catelogId);
        return list;
    }

    @Override
    public List<AttrDTO> attrListWithoutRelation(Long attrgroupId) {
        return attrDao.attrListWithoutRelation(attrgroupId);
    }

    /**
     * 查询当前spu对应的所有属性的分组信息以及当前分组下的所有属性对应的值
     * @param spuId
     * @param catalogId
     * @return
     */
    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        List<SpuItemAttrGroupVo> vos = attrGroupDao.getAttrGroupListWithSpuIdAndCatalogId(spuId, catalogId);
        return vos;
    }
}