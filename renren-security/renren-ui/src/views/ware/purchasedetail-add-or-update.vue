<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false"
		:close-on-press-escape="false">
		<el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()"
			label-width="120px">
			<el-form-item v-if="dataForm.id" label="采购单id" prop="purchaseId">
				<el-input v-model="dataForm.purchaseId" placeholder="采购单id"></el-input>
			</el-form-item>
			<el-form-item label="采购商品id" prop="skuId">
				<el-input v-model="dataForm.skuId" placeholder="采购商品id"></el-input>
			</el-form-item>
			<el-form-item label="采购数量" prop="skuNum">
				<el-input v-model="dataForm.skuNum" placeholder="采购数量"></el-input>
			</el-form-item>
			<el-form-item v-if="dataForm.id" label="采购金额" prop="skuPrice">
				<el-input v-model="dataForm.skuPrice" placeholder="采购金额"></el-input>
			</el-form-item>
			<el-form-item label="采购仓库" prop="wareId">
				<!-- <el-input v-model="dataForm.wareId" placeholder="仓库id"></el-input> -->
				<ware-select v-model:ware-id="dataForm.wareId"></ware-select>
			</el-form-item>
			<el-form-item v-if="dataForm.id" label="状态" prop="status">
				<!-- <el-input v-model="dataForm.status" placeholder="状态"></el-input> -->
				<el-select
					v-model="dataForm.status"
					placeholder="选择状态"
					size="large"
					style="width: 240px">
					<el-option
					v-for="item in props.statusList"
					:key="item.value"
					:label="item.label"
					:value="item.value"
					/>
				</el-select>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref, onMounted } from "vue";
import baseService from "@/service/commonService";
import { ElMessage } from "element-plus";
import wareSelect from "../common/ware-select.vue"
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
	id: '', purchaseId: '', skuId: '', skuNum: '', skuPrice: '', wareId: '', status: 0
});

const rules = ref({
	purchaseId: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	skuId: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	skuNum: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	skuPrice: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	wareId: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	status: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	]
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
	baseService.get("/ware/purchasedetail/" + id).then((res) => {
		Object.assign(dataForm, res.data);
	});
};

// 表单提交
const dataFormSubmitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false;
		}
		(!dataForm.id ? baseService.post : baseService.put)("/ware/purchasedetail", dataForm).then((res) => {
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
