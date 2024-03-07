package com.chanris.gulimall.member.controller;

import com.chanris.gulimall.common.annotation.LogOperation;
import com.chanris.gulimall.common.constant.Constant;
import com.chanris.gulimall.common.exception.CodeEnum;
import com.chanris.gulimall.common.page.PageData;
import com.chanris.gulimall.common.utils.ExcelUtils;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.common.validator.AssertUtils;
import com.chanris.gulimall.common.validator.ValidatorUtils;
import com.chanris.gulimall.common.validator.group.AddGroup;
import com.chanris.gulimall.common.validator.group.DefaultGroup;
import com.chanris.gulimall.common.validator.group.UpdateGroup;
import com.chanris.gulimall.member.dto.MemberDTO;
import com.chanris.gulimall.member.excel.MemberExcel;
import com.chanris.gulimall.member.exception.PhoneExistException;
import com.chanris.gulimall.member.exception.UsernameExistException;
import com.chanris.gulimall.member.feign.CouponFeignService;
import com.chanris.gulimall.member.service.MemberService;
import com.chanris.gulimall.member.vo.MemberRegistVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 会员
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@RestController
@RequestMapping("member/member")
@Api(tags="会员")
public class MemberController {
    @Resource
    private MemberService memberService;
    @Resource
    private CouponFeignService couponFeignService;

    @RequestMapping("/coupons")
    public Result test() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setNickname("张三");
        Result res = couponFeignService.memebercoupons();
        if(res.success()) {
            if (res.getData() instanceof List) {
                ArrayList data = (ArrayList) res.getData();
                System.out.println(Arrays.toString(data.toArray()));
            }
        }
        return new Result().ok(memberDTO);
    }

    @PostMapping("/register")
    public Result<?> register(MemberRegistVo registVo) {
        try {
            memberService.regist(registVo);
        }catch (PhoneExistException phoneExistException) {
            return new Result<>().error(CodeEnum.PHONE_EXIST_EXCEPTION.code, CodeEnum.PHONE_EXIST_EXCEPTION.msg);
        }catch (UsernameExistException usernameExistException) {
            return new Result<>().error(CodeEnum.USER_EXIST_EXCEPTION.code, CodeEnum.USER_EXIST_EXCEPTION.msg);
        }
        return new Result<>();
    }

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("member:member:page")
    public Result<PageData<MemberDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<MemberDTO> page = memberService.page(params);

        return new Result<PageData<MemberDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("member:member:info")
    public Result<MemberDTO> get(@PathVariable("id") Long id){
        MemberDTO data = memberService.get(id);

        return new Result<MemberDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("member:member:save")
    public Result save(@RequestBody MemberDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        memberService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("member:member:update")
    public Result update(@RequestBody MemberDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        memberService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("member:member:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        memberService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("member:member:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<MemberDTO> list = memberService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "会员", list, MemberExcel.class);
    }

}