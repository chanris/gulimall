<template>
  <el-dialog v-model="visible" :title="!dataForm.attrGroupId ? '新增' : '修改'" :close-on-click-modal="false"
    :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item label="组名" prop="attrGroupName">
        <el-input v-model="dataForm.attrGroupName" placeholder="组名"></el-input>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="descript">
        <el-input v-model="dataForm.descript" placeholder="描述"></el-input>
      </el-form-item>
      <el-form-item label="组图标" prop="icon">
        <el-input v-model="dataForm.icon" placeholder="组图标"></el-input>
      </el-form-item>
      <el-form-item label="所属分类id" prop="catelogId">
        <!-- disbaled 属性 必须为 boolean 类型 -->
        <el-input v-model="dataForm.catelogId" placeholder="所属分类id" :disabled="!!dataForm.attrGroupId"></el-input>
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
// import baseService from "@/service/baseService";
import commonService from "@/service/commonService";
import { ElMessage } from "element-plus";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
  attrGroupId: '', attrGroupName: '', sort: '', descript: '', icon: '', catelogId: ''
});

const rules = ref({
  attrGroupName: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  sort: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  descript: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  icon: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  catelogId: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ]
});

const init = (attrGroupId?: number) => {
  visible.value = true;
  dataForm.attrGroupId = "";

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  if (attrGroupId) {
    getInfo(attrGroupId);
  }
};

// 获取信息
const getInfo = (attrGroupId: number) => {
  commonService.get("/product/attrgroup/" + attrGroupId).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.attrGroupId ? commonService.post : commonService.put)("/product/attrgroup", dataForm).then((res) => {
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
