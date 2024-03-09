package com.chanris.gulimall.member.service.impl;

import com.alibaba.fastjson2.JSONObject;
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
import com.chanris.gulimall.member.util.GiteeHttpClient;
import com.chanris.gulimall.member.vo.MemberRegistVo;
import com.chanris.gulimall.member.vo.MemberUserLoginVo;
import com.chanris.gulimall.member.vo.SocialUser;
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

        // 设置其他信息
        entity.setNickname(registVo.getUserName());
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

    @Override
    public MemberEntity login(MemberUserLoginVo vo) {
        String loginacct = vo.getLoginacct();
        String password = vo.getPassword();

        //1、去数据库查询 SELECT * FROM ums_member WHERE username = ? OR mobile = ?
        MemberEntity memberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>()
                .eq("username", loginacct).or().eq("mobile", loginacct));

        if (memberEntity == null) {
            //登录失败
            return null;
        } else {
            //获取到数据库里的password
            String password1 = memberEntity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //进行密码匹配
            boolean matches = passwordEncoder.matches(password, password1);
            if (matches) {
                //登录成功
                return memberEntity;
            }
        }

        return null;
    }


    @Override
    public MemberEntity login(SocialUser socialUser) throws Exception {

        //具有登录和注册逻辑
        String uid = socialUser.getUid();

        //1、判断当前社交用户是否已经登录过系统
        MemberEntity memberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));

        if (memberEntity != null) {
            //这个用户已经注册过
            //更新用户的访问令牌的时间和access_token
            MemberEntity update = new MemberEntity();
            update.setId(memberEntity.getId());
            update.setAccessToken(socialUser.getAccess_token());
            update.setExpiresIn(socialUser.getExpires_in());
            memberDao.updateById(update);

            memberEntity.setAccessToken(socialUser.getAccess_token());
            memberEntity.setExpiresIn(socialUser.getExpires_in());
            return memberEntity;
        } else {
            //2、没有查到当前社交用户对应的记录我们就需要注册一个
            MemberEntity register = new MemberEntity();
            String url = "https://gitee.com/api/v5/user?access_token=" + socialUser.getAccess_token();
            JSONObject jsonObject = GiteeHttpClient.getUserInfo(url);

            if(jsonObject != null) {
                //查询成功
                String name = jsonObject.getString("name");
                String profileImageUrl = jsonObject.getString("avatar_url");

                register.setNickname(name);
                register.setHeader(profileImageUrl);
                register.setSocialUid(socialUser.getUid());
                register.setAccessToken(socialUser.getAccess_token());
                register.setExpiresIn(socialUser.getExpires_in());
            }

            //把用户信息插入到数据库中
            memberDao.insert(register);

            return register;
        }

    }
}