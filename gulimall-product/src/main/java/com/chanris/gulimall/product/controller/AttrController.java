package com.chanris.gulimall.product.controller;

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
import com.chanris.gulimall.product.dto.AttrDTO;
import com.chanris.gulimall.product.excel.AttrExcel;
import com.chanris.gulimall.product.service.AttrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 商品属性
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@RestController
@RequestMapping("product/attr")
@Api(tags="商品属性")
public class AttrController {
    @Resource
    private AttrService attrService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("product:attr:page")
    public Result<PageData<AttrDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        //PageData<AttrDTO> page = attrService.page(params);
        PageData<AttrDTO> page = attrService.pageWithOtherInfo(params);
        return new Result<PageData<AttrDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("product:attr:info")
    public Result<AttrDTO> get(@PathVariable("id") Long id){
//        AttrDTO data = attrService.get(id);
        AttrDTO data = attrService.getWithAttrGroupId(id);

        return new Result<AttrDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("product:attr:save")
    public Result save(@RequestBody AttrDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

//        attrService.save(dto);
        attrService.saveWithAttrGroupRelation(dto);
        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("product:attr:update")
    public Result update(@RequestBody AttrDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

//        attrService.update(dto);
        attrService.updateWithAttrGroupRelation(dto);
        return new Result();
    }



    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("product:attr:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
//        attrService.delete(ids);

        // 删除attr以及attr和 attrgroup 的关联
        attrService.deleteWithRelation(ids);
        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("product:attr:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<AttrDTO> list = attrService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "商品属性", list, AttrExcel.class);
    }

}