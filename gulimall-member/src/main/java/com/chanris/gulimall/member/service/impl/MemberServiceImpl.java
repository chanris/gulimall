package com.chanris.gulimall.member.service.impl;

import com.alibaba.nacos.common.utils.MD5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.member.dao.MemberLevelDao;
import com.chanris.gulimall.member.entity.MemberLevelEntity;
import com.chanris.gulimall.member.exception.PhoneExistException;
import com.chanris.gulimall.member.exception.UsernameExistException;
import com.chanris.gulimall.member.service.MemberService;
import com.chanris.gulimall.member.dao.MemberDao;
import com.chanris.gulimall.member.dto.MemberDTO;
import com.chanris.gulimall.member.entity.MemberEntity;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.member.vo.MemberRegistVo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 会员
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class MemberServiceImpl extends CrudServiceImpl<MemberDao, MemberEntity, MemberDTO> implements MemberService {

    @Resource
    private MemberDao memberDao;
    @Resource
    private MemberLevelDao memberLevelDao;

    @Override
    public QueryWrapper<MemberEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MemberEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public void regist(MemberRegistVo registVo) throws PhoneExistException, UsernameExistException{
        MemberEntity entity = new MemberEntity();

        //设置默认等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());

        // 检查手机号 & 用户名 是否唯一
        checkPhoneUnique(registVo.getPhone());
        checkUsernameUnique(registVo.getUserName());
        entity.setMobile(registVo.getPhone());
        entity.setUsername(registVo.getUserName());

        //设置密码 使用MD5 摘要算法  MD5( 文本 + salt（一段固定的随机数）) ==> 摘要

        // spring security MD5 + salt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(registVo.getPassword());
        entity.setPassword(encode);

        /**
         * 验证
         * encoder.matches(registVo.getPassword(), encode);
         */

        memberDao.insert(entity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException {
        Long count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if(count > 0) {
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUsernameUnique(String username) throws UsernameExistException {
        Long count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if(count > 0) {
            throw new PhoneExistException();
        }
    }
}