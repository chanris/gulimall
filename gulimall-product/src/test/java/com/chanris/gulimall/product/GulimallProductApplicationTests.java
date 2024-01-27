package com.chanris.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.product.dto.BrandDTO;
import com.chanris.gulimall.product.entity.BrandEntity;
import com.chanris.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class GulimallProductApplicationTests {

    @Resource
    BrandService brandService;

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

}
