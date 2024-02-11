package com.chanris.gulimall.product;

import com.chanris.gulimall.product.dao.AttrGroupDao;
import com.chanris.gulimall.product.dto.AttrGroupDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 11/2/2024
 * @description 测试 dao层 接口
 */
@SpringBootTest
public class SQLTests {
    
    @Resource
    AttrGroupDao attrGroupDao;
    
    @Test
    void test01() {
        List<AttrGroupDTO> list = attrGroupDao.getAttrGroupListWithAttr(null);
        System.out.println(Arrays.toString(list.toArray()));
    }
}
