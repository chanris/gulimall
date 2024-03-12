package com.chanris.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.to.member.MemberReceiveAddressTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.ware.dao.WareInfoDao;
import com.chanris.gulimall.ware.dto.WareInfoDTO;
import com.chanris.gulimall.ware.entity.WareInfoEntity;
import com.chanris.gulimall.ware.feign.MemberFeignService;
import com.chanris.gulimall.ware.service.WareInfoService;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.ware.vo.FareVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 仓库信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class WareInfoServiceImpl extends CrudServiceImpl<WareInfoDao, WareInfoEntity, WareInfoDTO> implements WareInfoService {

    @Resource
    private MemberFeignService memberFeignService;

    @Override
    public QueryWrapper<WareInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 根据用户的收货地址，计算运费
     * @param addrId
     * @return
     */
    @Override
    public FareVo getFare(Long addrId) {
        Result<MemberReceiveAddressTo> r = memberFeignService.get(addrId);
        if (r.success()) {
            FareVo fareVo = new FareVo();
            MemberReceiveAddressTo data = r.getData();
            String phone = data.getPhone();
            String subs = phone.substring(phone.length() - 1);
            BigDecimal decimal = new BigDecimal(subs);
            fareVo.setFare(decimal);
            fareVo.setAddress(data);
            return fareVo;
        }
        return null;
    }
}