<template>
  <div class="mod-coupon__seckillsession">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button v-if="state.hasPermission('coupon:seckillsession:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('coupon:seckillsession:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
              <el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="name" label="场次名称" header-align="center" align="center"></el-table-column>
              <el-table-column prop="startTime" label="每日开始时间" header-align="center" align="center"></el-table-column>
              <el-table-column prop="endTime" label="每日结束时间" header-align="center" align="center"></el-table-column>
              <el-table-column prop="status" label="启用状态" header-align="center" align="center"></el-table-column>
              <el-table-column prop="createTime" label="创建时间" header-align="center" align="center"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="200">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('coupon:seckillsession:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button v-if="state.hasPermission('coupon:seckillsession:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
          <el-button v-if="state.hasPermission('coupon:seckillpromotion:delete')" type="primary" link @click="skuRelationHandle(scope.row.id)">关联商品</el-button>
		</template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList">确定</add-or-update>
	<sku-relation ref="skuRelationRef"></sku-relation>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./seckillsession-add-or-update.vue";
import SkuRelation from "./seckillskurelation.vue"

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/coupon/seckillsession/page",
  getDataListIsPage: true,
  exportURL: "/coupon/seckillsession/export",
  deleteURL: "/coupon/seckillsession"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};

const skuRelationRef = ref();
// 打开 关联商品
const skuRelationHandle = (promotionSessionId) => {
	skuRelationRef.value.init(promotionSessionId);
}


</script>
