<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false"
		:close-on-press-escape="false">
		<el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()"
			label-width="120px">
			<el-form-item v-if="dataForm.id" label="采购人id" prop="assigneeId">
				<el-input v-model="dataForm.assigneeId" placeholder="采购人id"></el-input>
			</el-form-item>
			<el-form-item v-if="dataForm.id" label="采购人名" prop="assigneeName">
				<el-input v-model="dataForm.assigneeName" placeholder="采购人名"></el-input>
			</el-form-item>
			<el-form-item v-if="dataForm.id" label="联系方式" prop="phone">
				<el-input v-model="dataForm.phone" placeholder="联系方式"></el-input>
			</el-form-item>
			<el-form-item label="优先级" prop="priority">
				<el-input-number :min="0" :max="999" v-model="dataForm.priority" placeholder="优先级"></el-input-number>
			</el-form-item>
			<el-form-item label="状态" prop="status">
				<el-select clearable v-model="dataForm.status" placeholder="选择状态" size="large" style="width: 240px">
					<el-option v-for="item in props.statusList" :key="item.value" :label="item.label" :value="item.value" />
				</el-select>
			</el-form-item>
			<el-form-item label="仓库" prop="wareId">
				<!-- <el-input v-model="dataForm.wareId" placeholder="仓库"></el-input> -->
				<ware-select v-model:ware-id="dataForm.wareId"></ware-select>
			</el-form-item>
			<el-form-item v-if="dataForm.id" label="总金额" prop="amount">
				<el-input v-model="dataForm.amount" placeholder="总金额"></el-input>
			</el-form-item>
			<!-- <el-form-item label="创建日期" prop="createTime">
				<el-input v-model="dataForm.createTime" placeholder="创建日期"></el-input>
			</el-form-item> -->
			<!-- <el-form-item label="更新日期" prop="updateTime">
				<el-input v-model="dataForm.updateTime" placeholder="更新日期"></el-input>
			</el-form-item> -->
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import baseService from "@/service/commonService";
import { ElMessage } from "element-plus";
import wareSelect from "../common/ware-select.vue";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
	id: '', assigneeId: '', assigneeName: '', phone: '', priority: '', status: '', wareId: '', amount: '', createTime: '', updateTime: ''
});

const rules = ref({
	// assigneeId: [
	// 	{ required: true, message: '必填项不能为空', trigger: 'blur' }
	// ],
	// assigneeName: [
	// 	{ required: true, message: '必填项不能为空', trigger: 'blur' }
	// ],
	// phone: [
	// 	{ required: true, message: '必填项不能为空', trigger: 'blur' }
	// ],
	priority: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	status: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	wareId: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	// amount: [
	// 	{ required: true, message: '必填项不能为空', trigger: 'blur' }
	// ],
	// createTime: [
	// 	{ required: true, message: '必填项不能为空', trigger: 'blur' }
	// ],
	// updateTime: [
	// 	{ required: true, message: '必填项不能为空', trigger: 'blur' }
	// ]
});

const init = (id?: number) => {
	visible.value = true;
	dataForm.id = "";

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields();
	}

	if (id) {
		getInfo(id);
	}
};

// 获取信息
const getInfo = (id: number) => {
	baseService.get("/ware/purchase/" + id).then((res) => {
		Object.assign(dataForm, res.data);
	});
};

// 表单提交
const dataFormSubmitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false;
		}
		(!dataForm.id ? baseService.post : baseService.put)("/ware/purchase", dataForm).then((res) => {
			ElMessage.success({
				message: '成功',
				duration: 500,
				onClose: () => {
					visible.value = false;
					emit("refreshDataList");
				}
			});
		});
	});
};

const props = defineProps<{statusList: any}>()


defineExpose({
	init
});
</script>
