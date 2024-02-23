<template>
	<div class="mod-ware__purchasedetail">
		<el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
			<el-form-item label="仓库">
				<ware-select v-model:ware-id="state.dataForm.wareId"></ware-select>
			</el-form-item>
			<el-form-item label="状态">
				<el-select v-model="state.dataForm.status" clearable placeholder="选择状态" size="large" style="width: 240px">
					<el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button @click="state.getDataList()">查询</el-button>
			</el-form-item>
			<el-form-item>
				<el-button v-if="state.hasPermission('ware:purchasedetail:save')" type="primary"
					@click="addOrUpdateHandle()">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-button v-if="state.hasPermission('ware:purchasedetail:delete')" type="danger"
					@click="state.deleteHandle()">删除</el-button>
			</el-form-item>
			<el-form-item>
				<el-popover placement="bottom" :width="100" trigger="click" >
					<template #reference>
						<el-button type="danger">批量操作 <el-icon style="margin-left: 5px;"><ArrowDownBold /></el-icon> </el-button>
					</template>
					<div style="text-align: center;">
						<div class="popover-item" @click="state.deleteHandle()" >删除</div>
						<div class="popover-item" @click="dialogFormVisible = true">合并采购</div>
					</div>
				</el-popover>
			</el-form-item>
		</el-form>
		<el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
			<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
			<el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="purchaseId" label="采购单id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="skuId" label="采购商品id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="skuNum" label="采购数量" header-align="center" align="center"></el-table-column>
			<el-table-column prop="skuPrice" label="采购金额" header-align="center" align="center"></el-table-column>
			<el-table-column prop="wareId" label="仓库id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="status" label="状态" header-align="center" align="center">
				<template #default="scope">
					<el-tag v-if="scope.row.status === 0" type="primary">新建</el-tag>
					<el-tag v-else-if="scope.row.status === 1" type="success">已分配</el-tag>
					<el-tag v-else-if="scope.row.status === 2" type="info">正在采购</el-tag>
					<el-tag v-else-if="scope.row.status === 3" type="warning">已完成</el-tag>
					<el-tag v-else type="danger">采购失败</el-tag>
				</template>
			</el-table-column>
			<el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
				<template v-slot="scope">
					<el-button v-if="state.hasPermission('ware:purchasedetail:update')" type="primary" link
						@click="addOrUpdateHandle(scope.row.id)">修改</el-button>
					<el-button v-if="state.hasPermission('ware:purchasedetail:delete')" type="primary" link
						@click="state.deleteHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit"
			:total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle"
			@current-change="state.pageCurrentChangeHandle"> </el-pagination>
		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update ref="addOrUpdateRef" :statusList="statusList" @refreshDataList="state.getDataList">确定</add-or-update>

		<el-dialog v-model="dialogFormVisible" title="合并到整单" width="500">
			<el-form>
				<el-form-item label="">
					<el-select
						v-model="purchaseId"
						class="m-2"
						placeholder="请选择采购单"
						size="large"
						style="width: 240px">
						<el-option
							v-for="item in unreceivePurchaseList"
							:key="item.id"
							:label="item.id"
							:value="item.id"
						/>
					</el-select>
				</el-form-item>
			</el-form>
			<template #footer>
				<div class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取消</el-button>
					<el-button type="primary" @click="mergePurchase">确定</el-button>
				</div>
			</template>
		</el-dialog>
	</div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs, onMounted } from "vue";
import AddOrUpdate from "./purchasedetail-add-or-update.vue";
import wareSelect from "../common/ware-select.vue"
import wareService from "@/service/wareService"
import { ElMessage } from "element-plus";

const view = reactive({
	deleteIsBatch: true,
	getDataListURL: "/ware/purchasedetail/page",
	getDataListIsPage: true,
	exportURL: "/ware/purchasedetail/export",
	deleteURL: "/ware/purchasedetail",
	dataForm: {
		wareId: null,
		status: null
	}
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
	addOrUpdateRef.value.init(id);
};

const statusList = ref<any>([])
const unreceivePurchaseList = ref()
const purchaseId = ref()
onMounted(() => {
	statusList.value.push({ label: '新建', value: 0 })
	statusList.value.push({ label: '已分配', value: 1 })
	statusList.value.push({ label: '正在采购', value: 2 })
	statusList.value.push({ label: '已完成', value: 3 })
	statusList.value.push({ label: '采购失败', value: 4 })

	wareService.get("/ware/purchase/unreceive/list")
	.then(({data})=>{
		unreceivePurchaseList.value = data
		console.log('unreceivePurchaseList', unreceivePurchaseList.value)
	})
})

const dialogFormVisible = ref(false)
const mergePurchase = () => {
	dialogFormVisible.value = true
	let params :{items: any, purchaseId: number | null} = { items: [], purchaseId: null}
	params.purchaseId = purchaseId.value
	state.dataListSelections?.forEach(item=>{
		params.items.push(item.id)
	})
	console.log('合并请求参数:',params)
	wareService.post("/ware/purchase/merge", params)
	.then((res)=>{
		if(res.code === 0) {
			ElMessage.success({message: '合并成功'})
		}else {
			ElMessage.error({message: '合并失败'})
		}
	}).finally(()=>{
		state.getDataList()
		dialogFormVisible.value = false
	})
}
</script>
<style lang="less">
.popover-item {
	&:hover {
		cursor: pointer;
		background-color: rgb(168, 171, 178);
	}
}
</style>