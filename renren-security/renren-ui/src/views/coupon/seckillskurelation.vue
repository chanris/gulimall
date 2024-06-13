<template>
	<el-dialog v-model="visible" title="关联商品" :close-on-click-modal="false" :close-on-press-escape="false">
		<div class="mod-coupon__seckillskurelation">
			<el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
			<el-form-item>
				<el-button v-if="state.hasPermission('coupon:seckillskurelation:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-button v-if="state.hasPermission('coupon:seckillskurelation:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
			</el-form-item>
			</el-form>
			<el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
			<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
					<el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
					<el-table-column prop="promotionId" label="活动id" header-align="center" align="center"></el-table-column>
					<el-table-column prop="promotionSessionId" label="活动场次id" header-align="center" align="center"></el-table-column>
					<el-table-column prop="skuId" label="商品id" header-align="center" align="center"></el-table-column>
					<el-table-column prop="seckillPrice" label="秒杀价格" header-align="center" align="center"></el-table-column>
					<el-table-column prop="seckillCount" label="秒杀总量" header-align="center" align="center"></el-table-column>
					<el-table-column prop="seckillLimit" label="每人限购数量" header-align="center" align="center"></el-table-column>
					<el-table-column prop="seckillSort" label="排序" header-align="center" align="center"></el-table-column>
					<el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
				<template v-slot="scope">
				<el-button v-if="state.hasPermission('coupon:seckillskurelation:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
				<el-button v-if="state.hasPermission('coupon:seckillskurelation:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList">确定</add-or-update>
  </div>
</el-dialog>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./seckillskurelation-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/coupon/seckillskurelation/page",
  getDataListIsPage: true,
  exportURL: "/coupon/seckillskurelation/export",
  deleteURL: "/coupon/seckillskurelation"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(fixedPromotionSessionId.value, id);
};

const visible = ref(false)
const fixedPromotionSessionId = ref()
const init = (promotionSessionId: number) => {
  visible.value = true
  state.dataForm.promotionSessionId = '' + promotionSessionId // 查询条件: 根据场次id查询关联商品
  fixedPromotionSessionId.value = '' + promotionSessionId
  state.getDataList()
}

// 暴露方法
defineExpose({
  init
})
</script>
