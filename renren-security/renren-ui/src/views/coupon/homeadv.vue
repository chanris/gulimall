<template>
  <div class="mod-coupon__homeadv">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button v-if="state.hasPermission('coupon:homeadv:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('coupon:homeadv:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
              <el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="name" label="名字" header-align="center" align="center"></el-table-column>
              <el-table-column prop="pic" label="图片地址" header-align="center" align="center"></el-table-column>
              <el-table-column prop="startTime" label="开始时间" header-align="center" align="center"></el-table-column>
              <el-table-column prop="endTime" label="结束时间" header-align="center" align="center"></el-table-column>
              <el-table-column prop="status" label="状态" header-align="center" align="center"></el-table-column>
              <el-table-column prop="clickCount" label="点击数" header-align="center" align="center"></el-table-column>
              <el-table-column prop="url" label="广告详情连接地址" header-align="center" align="center"></el-table-column>
              <el-table-column prop="note" label="备注" header-align="center" align="center"></el-table-column>
              <el-table-column prop="sort" label="排序" header-align="center" align="center"></el-table-column>
              <el-table-column prop="publisherId" label="发布者" header-align="center" align="center"></el-table-column>
              <el-table-column prop="authId" label="审核者" header-align="center" align="center"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('coupon:homeadv:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button v-if="state.hasPermission('coupon:homeadv:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
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
import AddOrUpdate from "./homeadv-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/coupon/homeadv/page",
  getDataListIsPage: true,
  exportURL: "/coupon/homeadv/export",
  deleteURL: "/coupon/homeadv"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
