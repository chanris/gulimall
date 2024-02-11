package com.chanris.gulimall.member;

import com.chanris.gulimall.common.validator.ValidatorUtils;
import com.chanris.gulimall.common.validator.group.AddGroup;
import com.chanris.gulimall.common.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author chenyue7@foxmail.com
 * @date 8/2/2024
 * @description <a href="https://blog.csdn.net/zhouzhiwengang/article/details/128208496">hibernate validation constraint 介绍</a>
 */
@SpringBootTest
public class HibernateValidationConstraintTests {

    @Test
    void test01() {
        JavaBean javaBean = new JavaBean();
        // save operation
//        javaBean.setPassword("1234257");
//        javaBean.setLevelId(2L);
//        ValidatorUtils.validateEntity(javaBean, AddGroup.class);


        // update operation
        javaBean.setPassword("123123");
        javaBean.setLevelId(-1L);
        ValidatorUtils.validateEntity(javaBean, UpdateGroup.class);
    }

    static class JavaBean {
        @NotNull(groups = {AddGroup.class}) // 对象不能为空
        @Min(value = 0L, groups = {AddGroup.class, UpdateGroup.class}) // 最小值 == 0L
        private Long levelId;

        @NotBlank(groups = {AddGroup.class}) // 字符串 非null和非空串
//        @Range(min = 6, max = 20, groups = {AddGroup.class, UpdateGroup.class}) // 整数取值范围 == [6, 20]
        @Length(min = 6, max = 20, groups = {AddGroup.class, UpdateGroup.class}) // 字符串长度 == [6, 20]
        private String password;


        public Long getLevelId() {
            return levelId;
        }

        public void setLevelId(Long levelId) {
            this.levelId = levelId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
