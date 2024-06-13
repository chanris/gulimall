<template>
  <div class="mod-coupon__coupon">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button v-if="state.hasPermission('coupon:coupon:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('coupon:coupon:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
              <el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="couponType" label="优惠卷类型[0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券]" header-align="center" align="center"></el-table-column>
              <el-table-column prop="couponImg" label="优惠券图片" header-align="center" align="center"></el-table-column>
              <el-table-column prop="couponName" label="优惠卷名字" header-align="center" align="center"></el-table-column>
              <el-table-column prop="num" label="数量" header-align="center" align="center"></el-table-column>
              <el-table-column prop="amount" label="金额" header-align="center" align="center"></el-table-column>
              <el-table-column prop="perLimit" label="每人限领张数" header-align="center" align="center"></el-table-column>
              <el-table-column prop="minPoint" label="使用门槛" header-align="center" align="center"></el-table-column>
              <el-table-column prop="startTime" label="开始时间" header-align="center" align="center"></el-table-column>
              <el-table-column prop="endTime" label="结束时间" header-align="center" align="center"></el-table-column>
              <el-table-column prop="useType" label="使用类型[0->全场通用；1->指定分类；2->指定商品]" header-align="center" align="center"></el-table-column>
              <el-table-column prop="note" label="备注" header-align="center" align="center"></el-table-column>
              <el-table-column prop="publishCount" label="发行数量" header-align="center" align="center"></el-table-column>
              <el-table-column prop="useCount" label="已使用数量" header-align="center" align="center"></el-table-column>
              <el-table-column prop="receiveCount" label="领取数量" header-align="center" align="center"></el-table-column>
              <el-table-column prop="enableStartTime" label="可以领取的开始日期" header-align="center" align="center"></el-table-column>
              <el-table-column prop="enableEndTime" label="可以领取的结束日期" header-align="center" align="center"></el-table-column>
              <el-table-column prop="code" label="优惠码" header-align="center" align="center"></el-table-column>
              <el-table-column prop="memberLevel" label="可以领取的会员等级[0->不限等级，其他-对应等级]" header-align="center" align="center"></el-table-column>
              <el-table-column prop="publish" label="发布状态[0-未发布，1-已发布]" header-align="center" align="center"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('coupon:coupon:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button v-if="state.hasPermission('coupon:coupon:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList">确定</add-or-update>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./coupon-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/coupon/coupon/page",
  getDataListIsPage: true,
  exportURL: "/coupon/coupon/export",
  deleteURL: "/coupon/coupon"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
