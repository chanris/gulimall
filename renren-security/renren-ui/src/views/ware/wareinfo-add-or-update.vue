<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="仓库名" prop="name">
        <el-input v-model="dataForm.name" placeholder="仓库名"></el-input>
      </el-form-item>
          <el-form-item label="仓库地址" prop="address">
        <el-input v-model="dataForm.address" placeholder="仓库地址"></el-input>
      </el-form-item>
          <el-form-item label="区域编码" prop="areacode">
        <el-input v-model="dataForm.areacode" placeholder="区域编码"></el-input>
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
import baseService from "@/service/commonService";
import { ElMessage } from "element-plus";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
  id: '',  name: '',  address: '',  areacode: ''});

const rules = ref({
          name: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          address: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          areacode: [
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
  baseService.get("/ware/wareinfo/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/ware/wareinfo", dataForm).then((res) => {
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
