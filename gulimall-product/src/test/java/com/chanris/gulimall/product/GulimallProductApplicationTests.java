package com.chanris.gulimall.product;

import com.chanris.gulimall.product.dto.BrandDTO;
import com.chanris.gulimall.product.dto.CategoryDTO;
import com.chanris.gulimall.product.service.BrandService;
import com.chanris.gulimall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class GulimallProductApplicationTests {

    @Resource
    BrandService brandService;

    @Resource
    CategoryService categoryService;

    @Resource
    DataSource dataSource;

    @Test
    void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("HUAWEI");
//        BrandDTO brandDTO = new BrandDTO();
//        brandDTO.setName("HUAWEI");
//        brandService.save(brandDTO);
        HashMap<String, Object> query = new HashMap<>();
        query.put("bran_id", 1L);
        List<BrandDTO> list = brandService.list(query);
        System.out.println(Arrays.toString(list.toArray()));
    }

    /**
     * // TODO: 24/2/1 fix bug
     * 版本：mybatis-plus-boot-starter:3.5.3.2
     * 描述：测试 categoryService.listWithTree()方法
     * 当 CategoryEntity.showStatus 字段 加上 @TableLogic 后 查不出数据，即 为空，但不报错，
     * 删除 @TableLogic 就可以查出数据
     *
     * // FIXED: 24/2/1  原因：表字段show_status 默认为null，生成的记录 show_status字段为空，
     * 每次查询 sql 都会 加上 where show_status = 1， 故而 查不到
     */
    @Test
    void testCateTree() {
        List<CategoryDTO> dtos = categoryService.listWithTree();
        System.out.println(Arrays.toString(dtos.toArray()));
    }

    @Test
    void checkDatasource() {
        System.out.println(dataSource.getClass().getCanonicalName());
    }

}
