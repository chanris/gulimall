<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false"
    :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item label="属性id" prop="attrId">
		<attr-select v-model:attr-id="dataForm.attrId" v-model:attrgroup-id="dataForm.attrGroupId"></attr-select>
      </el-form-item>
      <el-form-item label="属性分组id" prop="attrGroupId">
        <el-input v-model="dataForm.attrGroupId" placeholder="属性分组id" disabled ></el-input>
      </el-form-item>
      <el-form-item label="属性组内排序" prop="attrSort">
        <el-input v-model="dataForm.attrSort" placeholder="属性组内排序"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref, watch} from "vue";
// import baseService from "@/service/baseService";
import baseService from "@/service/commonService";
import { ElMessage } from "element-plus";
import attrSelect from "../common/attr-select.vue";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

// const props = defineProps<{attrgroupId: any}>()
// watch(()=>props.attrgroupId, (val, oldVal)=>{
// 	if(val) {
// 		console.log('watch attrgourpId:', val)
// 		dataForm.attrGroupId = val
// 		console.log('watch dataForm.attrgourpId:', 	dataForm.attrGroupId)
// 	}
// })

const dataForm = reactive({
  id: '', attrId: '', attrGroupId: '', attrSort: ''
});

const rules = ref({
  attrId: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  attrGroupId: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  attrSort: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ]
});

const init = (attrGroupId: string, id?: number) => {
  visible.value = true;
  dataForm.id = "";
//   console.log('add-or-update dataForm.attrGroupId: ', attrGroupId)
  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }
  // 设置新增关联的 attrgroupId 
  dataForm.attrGroupId = attrGroupId
  if (id) {
    getInfo(id);
  }
};

// 获取信息
const getInfo = (id: number) => {
  baseService.get("/product/attrattrgrouprelation/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/product/attrattrgrouprelation", dataForm).then((res) => {
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
