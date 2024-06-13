<template>
  <div class="mod-coupon__seckillskunotice">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button v-if="state.hasPermission('coupon:seckillskunotice:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('coupon:seckillskunotice:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
              <el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="memberId" label="member_id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="skuId" label="sku_id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="sessionId" label="活动场次id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="subcribeTime" label="订阅时间" header-align="center" align="center"></el-table-column>
              <el-table-column prop="sendTime" label="发送时间" header-align="center" align="center"></el-table-column>
              <el-table-column prop="noticeType" label="通知方式[0-短信，1-邮件]" header-align="center" align="center"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('coupon:seckillskunotice:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button v-if="state.hasPermission('coupon:seckillskunotice:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
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
import AddOrUpdate from "./seckillskunotice-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/coupon/seckillskunotice/page",
  getDataListIsPage: true,
  exportURL: "/coupon/seckillskunotice/export",
  deleteURL: "/coupon/seckillskunotice"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
