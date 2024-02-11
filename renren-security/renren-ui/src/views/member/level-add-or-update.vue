<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false"
		:close-on-press-escape="false">
		<el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()"
			label-width="170px">
			<el-form-item label="等级名称" prop="name">
				<el-input v-model="dataForm.name" placeholder="等级名称"></el-input>
			</el-form-item>
			<el-form-item label="等级需要的成长值" prop="growthPoint">
				<!-- <el-input type="number" v-model="" placeholder="等级需要的成长值"></el-input> -->
				<el-input-number v-model="dataForm.growthPoint" :min="0" :max="100000" />
			</el-form-item>
			<el-form-item label="是否为默认等级" prop="defaultStatus">
				<!-- <el-input v-model="dataForm.defaultStatus" placeholder="是否为默认等级[0->不是；1->是]"></el-input> -->
				<el-checkbox v-model="dataForm.defaultStatus" :true-label="1" :false-label="0" size="large" />
			</el-form-item>
			<el-form-item label="免运费标准" prop="freeFreightPoint">
				<!-- <el-input type="number" v-model="dataForm.freeFreightPoint" placeholder="免运费标准" ></el-input> -->
				<el-input-number v-model="dataForm.freeFreightPoint" :min="0" :max="100000" />
			</el-form-item>
			<el-form-item label="每次评价获取的成长值" prop="commentGrowthPoint">
				<!-- <el-input type="number" v-model="dataForm.commentGrowthPoint" placeholder="每次评价获取的成长值"></el-input> -->
				<el-input-number v-model="dataForm.commentGrowthPoint" :min="0" :max="100000" />
			</el-form-item>
			<el-form-item label="是否有免邮特权" prop="priviledgeFreeFreight">
				<!-- <el-input v-model="dataForm.priviledgeFreeFreight" placeholder="是否有免邮特权"></el-input> -->
				<el-checkbox v-model="dataForm.priviledgeFreeFreight" :true-label="1" :false-label="0" size="large" />
			</el-form-item>
			<el-form-item label="是否有会员价格特权" prop="priviledgeMemberPrice">
				<!-- <el-input v-model="dataForm.priviledgeMemberPrice" placeholder="是否有会员价格特权"></el-input> -->
				<el-checkbox v-model="dataForm.priviledgeMemberPrice" :true-label="1" :false-label="0" size="large" />
			</el-form-item>
			<el-form-item label="是否有生日特权" prop="priviledgeBirthday">
				<!-- <el-input v-model="dataForm.priviledgeBirthday" placeholder="是否有生日特权"></el-input> -->
				<el-checkbox v-model="dataForm.priviledgeBirthday" :true-label="1" :false-label="0" size="large" />
			</el-form-item>
			<el-form-item label="备注" prop="note">
				<el-input v-model="dataForm.note" placeholder="备注"></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import baseService from '@/service/commonService'
import { ElMessage } from "element-plus";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
	id: '', name: '', growthPoint: '', defaultStatus: 0, freeFreightPoint: '', commentGrowthPoint: '', priviledgeFreeFreight: 0, priviledgeMemberPrice: 0, priviledgeBirthday: 0, note: ''
});

const rules = ref({
	name: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	growthPoint: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	defaultStatus: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	freeFreightPoint: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	commentGrowthPoint: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	priviledgeFreeFreight: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	priviledgeMemberPrice: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	priviledgeBirthday: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	note: [
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
	baseService.get("/member/memberlevel/" + id).then((res) => {
		Object.assign(dataForm, res.data);
	});
};

// 表单提交
const dataFormSubmitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false;
		}
		(!dataForm.id ? baseService.post : baseService.put)("/member/memberlevel", dataForm).then((res) => {
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

defineExpose({
	init
});
</script>
