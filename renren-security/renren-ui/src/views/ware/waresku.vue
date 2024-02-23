<template>
	<div class="mod-ware__waresku">
		<el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
			<el-form-item>
				<el-button v-if="state.hasPermission('ware:waresku:save')" type="primary"
					@click="addOrUpdateHandle()">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-button v-if="state.hasPermission('ware:waresku:delete')" type="danger"
					@click="state.deleteHandle()">删除</el-button>
			</el-form-item>
		</el-form>
		<el-table v-loading="state.dataListLoading" :data="state.dataList" border
			@selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
			<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
			<el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="skuId" label="sku_id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="wareId" label="仓库id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="stock" label="库存数" header-align="center" align="center"></el-table-column>
			<el-table-column prop="skuName" label="商品名称" header-align="center" align="center"></el-table-column>
			<el-table-column prop="stockLocked" label="锁定库存" header-align="center" align="center">
				<template #default="scope">
					<el-tag v-if="scope.row.stockLocked === 0" type="primary">未锁定</el-tag>
					<el-tag v-else type="danger">锁定</el-tag>
				</template>
			</el-table-column>
			<el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
				<template v-slot="scope">
					<el-button v-if="state.hasPermission('ware:waresku:update')" type="primary" link
						@click="addOrUpdateHandle(scope.row.id)">修改</el-button>
					<el-button v-if="state.hasPermission('ware:waresku:delete')" type="primary" link
						@click="state.deleteHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit"
			:total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle"
			@current-change="state.pageCurrentChangeHandle"> </el-pagination>
		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList">确定</add-or-update>
	</div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./waresku-add-or-update.vue";

const view = reactive({
	deleteIsBatch: true,
	getDataListURL: "/ware/waresku/page",
	getDataListIsPage: true,
	exportURL: "/ware/waresku/export",
	deleteURL: "/ware/waresku"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
	addOrUpdateRef.value.init(id);
};
</script>
