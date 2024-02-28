package com.chanris.gulimall.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chanris.gulimall.common.constant.Constant;
import com.chanris.gulimall.common.page.PageData;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.utils.ObjectConvert;
import com.chanris.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.chanris.gulimall.product.dao.AttrDao;
import com.chanris.gulimall.product.dao.ProductAttrValueDao;
import com.chanris.gulimall.product.dto.AttrAttrgroupRelationDTO;
import com.chanris.gulimall.product.dto.AttrDTO;
import com.chanris.gulimall.product.dto.ProductAttrValueDTO;
import com.chanris.gulimall.product.entity.AttrEntity;
import com.chanris.gulimall.product.service.AttrAttrgroupRelationService;
import com.chanris.gulimall.product.service.AttrService;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.product.service.ProductAttrValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class AttrServiceImpl extends CrudServiceImpl<AttrDao, AttrEntity, AttrDTO> implements AttrService {

    @Resource
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Resource
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Resource
    private AttrDao attrDao;

    @Resource
    private ProductAttrValueDao productAttrValueDao;

    @Resource
    private ProductAttrValueService productAttrValueService;

    @Override
    public QueryWrapper<AttrEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        Long attrGroupId = ObjectConvert.toLong(params.get("attrGroupId"));
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);
        wrapper.eq(ObjectUtil.isNotNull(attrGroupId), "attr_group_id", attrGroupId);
        return wrapper;
    }

    /**
     * 新增attr 和 attr_attrgroup_relation 记录
     * @param attrDTO
     */
    @Override
    @Transactional
    public void saveWithAttrGroupRelation(AttrDTO attrDTO) {
        save(attrDTO);
        Long attrGroupId = attrDTO.getAttrGroupId();
        AttrAttrgroupRelationDTO attrAttrgroupRelationDTO = new AttrAttrgroupRelationDTO();
        attrAttrgroupRelationDTO.setAttrGroupId(attrGroupId);
        attrAttrgroupRelationDTO.setAttrId(attrDTO.getAttrId());
        attrAttrgroupRelationService.save(attrAttrgroupRelationDTO);
    }

    @Override
    public void updateWithAttrGroupRelation(AttrDTO attrDTO) {
        update(attrDTO);
        // 得到attrId 直接修改 attr_attrgroup_relation.attr_group_id
        attrAttrgroupRelationDao.updateByAttrId(attrDTO);
    }

    /**
     * attrService page方法 带有AttrDTO.AttrGroupId
     * @param params
     * @return
     */
    @Override
    public PageData<AttrDTO> pageWithOtherInfo(Map<String, Object> params) {
        long curPage = 1;
        long limit = -1; // 没有传page参数，不分页
        if (params.get(Constant.PAGE) != null) {
            curPage = Long.parseLong((String) params.get(Constant.PAGE));
        }
        if (params.get(Constant.LIMIT) != null) {
            limit = Long.parseLong((String) params.get(Constant.LIMIT));
        }
        IPage<?> ipage = new Page<>(curPage, limit);
        Long catelogId = ObjectConvert.toLong(params.get("catelogId")); // can return null
        Integer attrType = ObjectConvert.toInteger(params.get("attrType"));
        IPage<AttrDTO> page = attrDao.pageWithOtherInfo(ipage, catelogId, attrType);
        return getPageData(page, AttrDTO.class);
    }

    @Override
    public AttrDTO getWithAttrGroupId(Long attrId) {
        return attrDao.getWithAttrGroupId(attrId);
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void deleteWithRelation(Long[] attrIds) {
        this.delete(attrIds);
        attrAttrgroupRelationDao.deleteByAttrIds(List.of(attrIds));
    }

    @Override
    public List<ProductAttrValueDTO> listforspu(Long spuId) {
        Map<String, Object> wrapper = new HashMap<>(1);
        wrapper.put("spuId", spuId);
        List<ProductAttrValueDTO> list = productAttrValueService.list(wrapper);
        return list;
    }

    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {

        return attrDao.selectSearchAttrs(attrIds);
    }
}