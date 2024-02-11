<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false"
		:close-on-press-escape="false">
		<el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()"
			label-width="120px">
			<el-form-item label="会员等级" prop="levelId">
				<!-- <el-input v-model="dataForm.levelId" placeholder="会员等级id"></el-input> -->
				<el-select v-model="dataForm.levelId" placeholder="请选择会员等级" size="large" style="width: 240px">
					<el-option v-for="item in memberLevelList" :key="item.id" :label="item.name" :value="item.id" />
				</el-select>
			</el-form-item>
			<el-form-item label="用户名" prop="username">
				<el-input v-model="dataForm.username" placeholder="用户名"></el-input>
			</el-form-item>
			<el-form-item label="密码" prop="password">
				<el-input v-model="dataForm.password" placeholder="密码"></el-input>
			</el-form-item>
			<el-form-item label="昵称" prop="nickname">
				<el-input v-model="dataForm.nickname" placeholder="昵称"></el-input>
			</el-form-item>
			<el-form-item label="手机号码" prop="mobile">
				<el-input v-model="dataForm.mobile" placeholder="手机号码" maxlength="30"></el-input>
			</el-form-item>
			<el-form-item label="邮箱" prop="email">
				<el-input v-model="dataForm.email" placeholder="邮箱" maxlength="30"></el-input>
			</el-form-item>
			<el-form-item label="头像" prop="header">
				<el-input v-model="dataForm.header" placeholder="头像"></el-input>
			</el-form-item>
			<el-form-item label="性别" prop="gender">
				<!-- <el-input v-model="dataForm.gender" placeholder="性别"></el-input> -->
				<el-select v-model="dataForm.gender" placeholder="请选择性别" size="large" style="width: 240px">
					<el-option v-for="item in genderList" :key="item.value" :label="item.label" :value="item.value" />
				</el-select>
			</el-form-item>
			<el-form-item label="生日" prop="birth">
				<!-- <el-input v-model="dataForm.birth" placeholder="生日"></el-input> -->
				<el-date-picker
					v-model="dataForm.birth"
					type="date"
					placeholder="请选择日期"
				/>
			</el-form-item>
			<el-form-item label="所在城市" prop="city">
				<el-input v-model="dataForm.city" placeholder="所在城市"></el-input>
			</el-form-item>
			<el-form-item label="职业" prop="job">
				<el-input v-model="dataForm.job" placeholder="职业"></el-input>
			</el-form-item>
			<el-form-item label="个性签名" prop="sign">
				<el-input v-model="dataForm.sign" placeholder="个性签名"></el-input>
			</el-form-item>
			<el-form-item label="用户来源" prop="sourceType">
				<el-input-number v-model="dataForm.sourceType" :min="0" :max="10000"></el-input-number>
			</el-form-item>
			<el-form-item label="积分" prop="integration">
				<el-input-number v-model="dataForm.integration" :min="0" :max="999999999"></el-input-number>
			</el-form-item>
			<el-form-item label="成长值" prop="growth">
				<!-- <el-input v-model="dataForm.growth" placeholder="成长值"></el-input> -->
				<el-input-number v-model="dataForm.growth" :min="0" :max="999999999"></el-input-number>
			</el-form-item>
			<el-form-item label="启用状态" prop="status">
				<!-- <el-input v-model="dataForm.status" placeholder="启用状态"></el-input> -->
				<el-switch v-model="dataForm.status" :active-value="1" :inactive-value="0"></el-switch>
			</el-form-item>
			<el-form-item label="注册时间" prop="createTime">
				<!-- <el-input v-model="dataForm.createTime" placeholder="注册时间"></el-input> -->
				<el-date-picker
					v-model="dataForm.createTime"
					type="date"
					placeholder="请选择日期"
				/>
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
import memberService from '@/service/memberService'
import { ElMessage } from "element-plus";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
	id: '', levelId: '', username: '', password: '', nickname: '', mobile: '', email: '', header: '', gender: '', birth: '', city: '', job: '', sign: '', sourceType: '', integration: '', growth: '', status: '', createTime: ''
});

const rules = ref({
	levelId: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	username: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	password: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	nickname: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	mobile: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	email: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	header: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	gender: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	birth: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	city: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	job: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	sign: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	sourceType: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	integration: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	growth: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	status: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	],
	createTime: [
		{ required: true, message: '必填项不能为空', trigger: 'blur' }
	]
});
const memberLevelList = ref<any>([])
onMounted(()=>{
	memberService.memberLevelList().then(({data})=>{
		memberLevelList.value = data.list
	})
})

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
	baseService.get("/member/member/" + id).then((res) => {
		Object.assign(dataForm, res.data);
	});
};

// 表单提交
const dataFormSubmitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false;
		}
		(!dataForm.id ? baseService.post : baseService.put)("/member/member", dataForm).then((res) => {
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

const genderList = ref([
	{
		value: 0,
		label: '女'
	},
	{
		value: 1,
		label: '男'
	},
	{
		value: 2,
		label: '其他'
	}
])

defineExpose({
	init
});
</script>
