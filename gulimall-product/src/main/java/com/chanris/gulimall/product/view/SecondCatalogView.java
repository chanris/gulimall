package com.chanris.gulimall.product.view;

import lombok.Data;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 28/2/2024
 * @description 第二层（级）分类视图对象
 */
@Data
public class SecondCatalogView {
    private String topCatalogId;
    private List<ThirdCatalogView> thirdCatalogViewList;
    private String id;
    private String name;
}
