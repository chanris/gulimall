package com.chanris.gulimall.product.app;

import com.chanris.gulimall.common.annotation.LogOperation;
import com.chanris.gulimall.common.constant.Constant;
import com.chanris.gulimall.common.page.PageData;
import com.chanris.gulimall.common.utils.ExcelUtils;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.common.validator.AssertUtils;
import com.chanris.gulimall.common.validator.ValidatorUtils;
import com.chanris.gulimall.common.validator.group.AddGroup;
import com.chanris.gulimall.common.validator.group.DefaultGroup;
import com.chanris.gulimall.common.validator.group.UpdateGroup;
import com.chanris.gulimall.product.dto.SkuInfoDTO;
import com.chanris.gulimall.product.entity.SkuInfoEntity;
import com.chanris.gulimall.product.excel.SkuInfoExcel;
import com.chanris.gulimall.product.service.SkuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * sku信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@RestController
@RequestMapping("product/skuinfo")
@Api(tags="sku信息")
public class SkuInfoController {
    @Resource
    private SkuInfoService skuInfoService;

    /**
     * 根据skuId查询当前商品的价格
     * @param skuId
     * @return
     */
    @GetMapping(value = "/{skuId}/price")
    public BigDecimal getPrice(@PathVariable("skuId") Long skuId) {

        //获取当前商品的信息
        SkuInfoDTO skuInfoDTO = skuInfoService.get(skuId);

        //获取商品的价格
        return skuInfoDTO.getPrice();
    }

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("product:skuinfo:page")
    public Result<PageData<SkuInfoDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<SkuInfoDTO> page = skuInfoService.page(params);

        return new Result<PageData<SkuInfoDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("product:skuinfo:info")
    public Result<SkuInfoDTO> get(@PathVariable("id") Long id){
        SkuInfoDTO data = skuInfoService.get(id);

        return new Result<SkuInfoDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
//    @RequiresPermissions("product:skuinfo:save")
    public Result save(@RequestBody SkuInfoDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        skuInfoService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("product:skuinfo:update")
    public Result update(@RequestBody SkuInfoDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        skuInfoService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
//    @RequiresPermissions("product:skuinfo:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        skuInfoService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
//    @RequiresPermissions("product:skuinfo:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SkuInfoDTO> list = skuInfoService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "sku信息", list, SkuInfoExcel.class);
    }

}