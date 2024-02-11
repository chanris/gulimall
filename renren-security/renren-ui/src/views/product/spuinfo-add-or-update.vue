<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="商品名称" prop="spuName">
        <el-input v-model="dataForm.spuName" placeholder="商品名称"></el-input>
      </el-form-item>
          <el-form-item label="商品描述" prop="spuDescription">
        <el-input v-model="dataForm.spuDescription" placeholder="商品描述"></el-input>
      </el-form-item>
          <el-form-item label="所属分类id" prop="catalogId">
        <el-input v-model="dataForm.catalogId" placeholder="所属分类id"></el-input>
      </el-form-item>
          <el-form-item label="品牌id" prop="brandId">
        <el-input v-model="dataForm.brandId" placeholder="品牌id"></el-input>
      </el-form-item>
          <el-form-item label="" prop="weight">
        <el-input v-model="dataForm.weight" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="上架状态[0 - 下架，1 - 上架]" prop="publishStatus">
        <el-input v-model="dataForm.publishStatus" placeholder="上架状态[0 - 下架，1 - 上架]"></el-input>
      </el-form-item>
          <el-form-item label="" prop="createTime">
        <el-input v-model="dataForm.createTime" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="updateTime">
        <el-input v-model="dataForm.updateTime" placeholder=""></el-input>
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
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
  id: '',  spuName: '',  spuDescription: '',  catalogId: '',  brandId: '',  weight: '',  publishStatus: '',  createTime: '',  updateTime: ''});

const rules = ref({
          spuName: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          spuDescription: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          catalogId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          brandId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          weight: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          publishStatus: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          createTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          updateTime: [
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
  baseService.get("/product/spuinfo/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/product/spuinfo", dataForm).then((res) => {
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
