<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false"
    :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item label="品牌id" prop="brandId">
        <el-input v-model="dataForm.brandId" placeholder="品牌id"></el-input>
      </el-form-item>
      <el-form-item label="分类id" prop="catelogId">
        <el-input v-model="dataForm.catelogId" placeholder="分类id"></el-input>
      </el-form-item>
      <el-form-item label="品牌名称" prop="brandName">
        <el-input v-model="dataForm.brandName" placeholder="品牌名称"></el-input>
      </el-form-item>
      <el-form-item label="分类名称" prop="catelogName">
        <el-input v-model="dataForm.catelogName" placeholder="分类名称"></el-input>
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
import baseService from "@/service/commonService";
import { ElMessage } from "element-plus";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
  id: '', brandId: '', catelogId: '', brandName: '', catelogName: ''
});

const rules = ref({
  brandId: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  catelogId: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  brandName: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  catelogName: [
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
  baseService.get("/product/categorybrandrelation/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/product/categorybrandrelation", dataForm).then((res) => {
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
