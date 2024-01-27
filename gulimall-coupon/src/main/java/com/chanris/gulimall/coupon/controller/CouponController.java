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
import com.chanris.gulimall.coupon.dto.CouponDTO;
import com.chanris.gulimall.coupon.excel.CouponExcel;
import com.chanris.gulimall.coupon.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 优惠券信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 *
 * 配置使用 nacos config
 * 1.引入 nacos-config 和 cloud-bootstrap
 * 2. 配置 config 的地址 信息
 * 3.创建一个bootstrap.properties文件
 * 4.创建配置项，并使用 实时刷新配置项值 在对应的controller上加@RefreshScope
 *
 * !!!!!!!!!!!!!注意!!!!!!!!!!!!!
 * spring boot 2.4.x 以上版本 禁用了bootstrap 需要配置环境变量 spring.cloud.bootstrap.enabled=true
 * 再在bootstrap.properties里面写spring.application.name=gulimall-coupon， 这样才能动态刷新配置
 *
 * 命名空间：
 * 默认 public： 默认新增的所有的配置都写在public空间下，
 * 1.需要哪一个空间下的配置文件，配置 spring.cloud.nacos.config.namespace=命名空间的ID
 * 2.每一个微服务之间相互隔离配置，每个微服务都创建自己的命名空间，只加载自己命名空间下的所有配置
 */
@RestController
@RequestMapping("coupon/coupon")
@Api(tags="优惠券信息")
@RefreshScope  // nacos config 配置属性自动刷新
public class CouponController {
    @Resource
    private CouponService couponService;
    @Value("${coupon.user.name}")
    private String username;
    @Value("${coupon.user.age}")
    private Long age;

    @RequestMapping("/member/list")
    public Result memebercoupons() {
        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setCouponName("满100减10");
        return new Result().ok(List.of(couponDTO));
    }

    @RequestMapping("/test")
    public Result test() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("coupon.user.name", username);
        map.put("coupon.user.age", age);
        return new Result().ok(map);
    }

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    //@RequiresPermissions("coupon:coupon:page")
    public Result<PageData<CouponDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<CouponDTO> page = couponService.page(params);

        return new Result<PageData<CouponDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    //@RequiresPermissions("coupon:coupon:info")
    public Result<CouponDTO> get(@PathVariable("id") Long id){
        CouponDTO data = couponService.get(id);

        return new Result<CouponDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    //@RequiresPermissions("coupon:coupon:save")
    public Result save(@RequestBody CouponDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        couponService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    //@RequiresPermissions("coupon:coupon:update")
    public Result update(@RequestBody CouponDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        couponService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    //@RequiresPermissions("coupon:coupon:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        couponService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    //@RequiresPermissions("coupon:coupon:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<CouponDTO> list = couponService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "优惠券信息", list, CouponExcel.class);
    }

}