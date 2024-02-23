<template>
	<div class="mod-ware__purchase">
		<el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
			<el-form-item label="状态">
				<el-select clearable v-model="state.dataForm.status" placeholder="选择状态" size="large" style="width: 240px">
					<el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button color="#626aef" @click.stop="state.getDataList()">查询</el-button>
			</el-form-item>
			<el-form-item>
				<el-button v-if="state.hasPermission('ware:purchase:save')" type="primary"
					@click="addOrUpdateHandle()">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-button v-if="state.hasPermission('ware:purchase:delete')" type="danger"
					@click="state.deleteHandle()">删除</el-button>
			</el-form-item>
		</el-form>
		<el-table v-loading="state.dataListLoading" :data="state.dataList" border
			@selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
			<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
			<el-table-column prop="id" label="采购单id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="assigneeId" label="采购人id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="assigneeName" label="采购人名" header-align="center" align="center"></el-table-column>
			<el-table-column prop="phone" label="联系方式" header-align="center" align="center"></el-table-column>
			<el-table-column prop="priority" label="优先级" header-align="center" align="center"></el-table-column>
			<el-table-column prop="status" label="状态" header-align="center" align="center">
				<template #default="scope">
					<el-tag v-if="scope.row.status === 0" type="primary">新建</el-tag>
					<el-tag v-else-if="scope.row.status === 1" type="success">已分配</el-tag>
					<el-tag v-else-if="scope.row.status === 2" type="info">已领取</el-tag>
					<el-tag v-else-if="scope.row.status === 3" type="warning">已完成</el-tag>
					<el-tag v-else type="danger">有异常</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="wareId" label="仓库id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="amount" label="总金额" header-align="center" align="center"></el-table-column>
			<el-table-column prop="createTime" label="创建日期" header-align="center" align="center" min-width="110"></el-table-column>
			<el-table-column prop="updateTime" label="更新日期" header-align="center" align="center" min-width="110"></el-table-column>
			<el-table-column label="操作" fixed="right" header-align="center" align="center" width="180">
				<template v-slot="scope">
					<el-button v-if="state.hasPermission('ware:purchase:delete')" type="primary" link
						@click="assignPurchasingStaff(scope.row)">分配</el-button>
					<el-button v-if="state.hasPermission('ware:purchase:update')" type="primary" link
						@click="addOrUpdateHandle(scope.row.id)">修改</el-button>
					<el-button v-if="state.hasPermission('ware:purchase:delete')" type="primary" link
						@click="state.deleteHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit"
			:total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle"
			@current-change="state.pageCurrentChangeHandle"> </el-pagination>
		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update ref="addOrUpdateRef" :statusList="statusList"  @refreshDataList="state.getDataList">确定</add-or-update>

		<el-dialog v-model="dialogFormVisible" title="分配采购人员" width="500">
			<el-form>
				<el-form-item label="">
					<el-select
						clearable
						v-model="userInfo"
						placeholder="请选择采购单"
						size="large"
						style="width: 240px">
						<el-option
							v-for="(item, index) in userInfoList"
							:key="item.id"
							:label="item.realName"
							:value="item"
						/>
					</el-select>
				</el-form-item>
			</el-form>
			<template #footer>
				<div class="dialog-footer">
					<el-button @click="dialogFormVisible = false;">取消</el-button>
					<el-button type="primary" @click="submitAssignStaff">确定</el-button>
				</div>
			</template>
		</el-dialog>
	</div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs, onMounted, watch } from "vue";
import AddOrUpdate from "./purchase-add-or-update.vue";
import baseService from "@/service/baseService";
import wareService from "@/service/wareService";
import { ElMessage } from "element-plus";
const view = reactive({
	deleteIsBatch: true,
	getDataListURL: "/ware/purchase/page",
	getDataListIsPage: true,
	exportURL: "/ware/purchase/export",
	deleteURL: "/ware/purchase",
	dataForm: {
		status: null
	}
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
	addOrUpdateRef.value.init(id);
};

const statusList = ref<any>([])
const userInfoList = ref<any>([])
const userInfo = ref<any>()
onMounted(() => {
	statusList.value.push({ label: '新建', value: 0 })
	statusList.value.push({ label: '已分配', value: 1 })
	statusList.value.push({ label: '已领取', value: 2 })
	statusList.value.push({ label: '已完成', value: 3 })
	statusList.value.push({ label: '有异常', value: 4 })

	baseService.get("/sys/user/page")
	.then(({data})=>{
		userInfoList.value = data.list
	})
})
const row = ref()
const dialogFormVisible = ref(false)
/// 分配采购人员
const assignPurchasingStaff = (_row: any) => {
	if(_row.status > 1) {
		ElMessage.error({message: '采购单已领取，无法分配'})
		return
	}
	dialogFormVisible.value = true
	row.value = _row
	
}
const submitAssignStaff = () => {
	dialogFormVisible.value = false
	let params = {...row.value}
	params.assigneeId = userInfo.value.id
	params.assigneeName = userInfo.value.username
	params.phone = userInfo.value.mobile
	params.status = 1 // 设置 采购单已分配
	wareService.put("/ware/purchase", params)
	.then((res)=>{
		if(res.code === 0) {
			ElMessage.success({message: '分配成功'})
			state.getDataList()
		}else {
			ElMessage.error({message: '分配失败'})
		}
	})
}
</script>
