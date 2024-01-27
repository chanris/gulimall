package com.chanris.gulimall.coupon.controller;

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
import com.chanris.gulimall.coupon.dto.CouponHistoryDTO;
import com.chanris.gulimall.coupon.excel.CouponHistoryExcel;
import com.chanris.gulimall.coupon.service.CouponHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 优惠券领取历史记录
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@RestController
@RequestMapping("coupon/couponhistory")
@Api(tags="优惠券领取历史记录")
public class CouponHistoryController {
    @Autowired
    private CouponHistoryService couponHistoryService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("coupon:couponhistory:page")
    public Result<PageData<CouponHistoryDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<CouponHistoryDTO> page = couponHistoryService.page(params);

        return new Result<PageData<CouponHistoryDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("coupon:couponhistory:info")
    public Result<CouponHistoryDTO> get(@PathVariable("id") Long id){
        CouponHistoryDTO data = couponHistoryService.get(id);

        return new Result<CouponHistoryDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("coupon:couponhistory:save")
    public Result save(@RequestBody CouponHistoryDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        couponHistoryService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("coupon:couponhistory:update")
    public Result update(@RequestBody CouponHistoryDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        couponHistoryService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("coupon:couponhistory:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        couponHistoryService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("coupon:couponhistory:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<CouponHistoryDTO> list = couponHistoryService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "优惠券领取历史记录", list, CouponHistoryExcel.class);
    }

}