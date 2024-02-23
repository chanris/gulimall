package com.chanris.gulimall.ware.controller;

import com.chanris.gulimall.common.annotation.LogOperation;
import com.chanris.gulimall.common.constant.Constant;
import com.chanris.gulimall.common.page.PageData;
import com.chanris.gulimall.common.to.SysUserTo;
import com.chanris.gulimall.common.utils.ExcelUtils;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.common.validator.AssertUtils;
import com.chanris.gulimall.common.validator.ValidatorUtils;
import com.chanris.gulimall.common.validator.group.AddGroup;
import com.chanris.gulimall.common.validator.group.DefaultGroup;
import com.chanris.gulimall.common.validator.group.UpdateGroup;
import com.chanris.gulimall.ware.dto.PurchaseDTO;
import com.chanris.gulimall.ware.excel.PurchaseExcel;
import com.chanris.gulimall.ware.feign.AdminFeignService;
import com.chanris.gulimall.ware.service.PurchaseService;
import com.chanris.gulimall.ware.vo.MergeVo;
import com.chanris.gulimall.ware.vo.PurchaseDoneVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 采购信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@RestController
@RequestMapping("ware/purchase")
@Api(tags="采购信息")
@Validated
@Slf4j
public class PurchaseController {
    @Resource
    private PurchaseService purchaseService;
    @Resource
    private AdminFeignService adminFeignService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("ware:purchase:page")
    public Result<PageData<PurchaseDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<PurchaseDTO> page = purchaseService.page(params);

        return new Result<PageData<PurchaseDTO>>().ok(page);
    }

    /**
     * 获得未分配的采购单信息
     * @return
     */
    @GetMapping("unreceive/list")
    public Result<List<PurchaseDTO>> unreceiveList() {
        List<PurchaseDTO> unreceiveList = purchaseService.getUnreceiveList();
        return new Result<List<PurchaseDTO>>().ok(unreceiveList);
    }

    /**
     * 合并 采购项 到 采购单 中
     * @param mergeVo
     * @return
     */
    @PostMapping("merge")
    public Result<?> merge(@RequestBody MergeVo mergeVo) {
        ValidatorUtils.validateEntity(mergeVo, DefaultGroup.class);
        purchaseService.mergePurchaseDetail(mergeVo);
        return new Result<>();
    }

    /**
     * 领取采购单
     * @param purchaseIds
     * @return
     */
    @PostMapping("received")
    public Result<?> received(@RequestBody @NotEmpty  List<Long> purchaseIds, HttpServletRequest request) {
        Result<SysUserTo> r = adminFeignService.info(request.getHeader("Token"));
        if(r.getCode() == 0) {
            purchaseService.receiveByUserId(purchaseIds, r.getData().getId());
        }else {
            log.warn("远程获得登录用户信息失败");
            return new Result<>().error(r.getCode(), r.getMsg());
        }
        return new Result<>();
    }

    @PostMapping("done")
    public Result finish(@RequestBody PurchaseDoneVo doneVo) {
        purchaseService.done(doneVo);
        return new Result();
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @RequiresPermissions("ware:purchase:info")
    public Result<PurchaseDTO> get(@PathVariable("id") Long id){
        PurchaseDTO data = purchaseService.get(id);

        return new Result<PurchaseDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
//    @RequiresPermissions("ware:purchase:save")
    public Result save(@RequestBody PurchaseDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        purchaseService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("ware:purchase:update")
    public Result update(@RequestBody PurchaseDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        purchaseService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
//    @RequiresPermissions("ware:purchase:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        purchaseService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
//    @RequiresPermissions("ware:purchase:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<PurchaseDTO> list = purchaseService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "采购信息", list, PurchaseExcel.class);
    }

}