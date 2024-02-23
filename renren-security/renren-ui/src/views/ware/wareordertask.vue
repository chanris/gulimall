<template>
  <div class="mod-ware__wareordertask">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button v-if="state.hasPermission('ware:wareordertask:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('ware:wareordertask:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
              <el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="orderId" label="order_id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="orderSn" label="order_sn" header-align="center" align="center"></el-table-column>
              <el-table-column prop="consignee" label="收货人" header-align="center" align="center"></el-table-column>
              <el-table-column prop="consigneeTel" label="收货人电话" header-align="center" align="center"></el-table-column>
              <el-table-column prop="deliveryAddress" label="配送地址" header-align="center" align="center"></el-table-column>
              <el-table-column prop="orderComment" label="订单备注" header-align="center" align="center"></el-table-column>
              <el-table-column prop="paymentWay" label="付款方式【 1:在线付款 2:货到付款】" header-align="center" align="center"></el-table-column>
              <el-table-column prop="taskStatus" label="任务状态" header-align="center" align="center"></el-table-column>
              <el-table-column prop="orderBody" label="订单描述" header-align="center" align="center"></el-table-column>
              <el-table-column prop="trackingNo" label="物流单号" header-align="center" align="center"></el-table-column>
              <el-table-column prop="createTime" label="create_time" header-align="center" align="center"></el-table-column>
              <el-table-column prop="wareId" label="仓库id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="taskComment" label="工作单备注" header-align="center" align="center"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('ware:wareordertask:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button v-if="state.hasPermission('ware:wareordertask:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
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
import AddOrUpdate from "./wareordertask-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/ware/wareordertask/page",
  getDataListIsPage: true,
  exportURL: "/ware/wareordertask/export",
  deleteURL: "/ware/wareordertask"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
