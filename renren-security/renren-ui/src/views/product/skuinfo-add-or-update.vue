<template>
  <el-dialog v-model="visible" :title="!dataForm.skuId ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="spuId" prop="spuId">
        <el-input v-model="dataForm.spuId" placeholder="spuId"></el-input>
      </el-form-item>
          <el-form-item label="sku名称" prop="skuName">
        <el-input v-model="dataForm.skuName" placeholder="sku名称"></el-input>
      </el-form-item>
          <el-form-item label="sku介绍描述" prop="skuDesc">
        <el-input v-model="dataForm.skuDesc" placeholder="sku介绍描述"></el-input>
      </el-form-item>
          <el-form-item label="所属分类id" prop="catalogId">
        <el-input v-model="dataForm.catalogId" placeholder="所属分类id"></el-input>
      </el-form-item>
          <el-form-item label="品牌id" prop="brandId">
        <el-input v-model="dataForm.brandId" placeholder="品牌id"></el-input>
      </el-form-item>
          <el-form-item label="默认图片" prop="skuDefaultImg">
        <el-input v-model="dataForm.skuDefaultImg" placeholder="默认图片"></el-input>
      </el-form-item>
          <el-form-item label="标题" prop="skuTitle">
        <el-input v-model="dataForm.skuTitle" placeholder="标题"></el-input>
      </el-form-item>
          <el-form-item label="副标题" prop="skuSubtitle">
        <el-input v-model="dataForm.skuSubtitle" placeholder="副标题"></el-input>
      </el-form-item>
          <el-form-item label="价格" prop="price">
        <el-input v-model="dataForm.price" placeholder="价格"></el-input>
      </el-form-item>
          <el-form-item label="销量" prop="saleCount">
        <el-input v-model="dataForm.saleCount" placeholder="销量"></el-input>
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
  skuId: '',  spuId: '',  skuName: '',  skuDesc: '',  catalogId: '',  brandId: '',  skuDefaultImg: '',  skuTitle: '',  skuSubtitle: '',  price: '',  saleCount: ''});

const rules = ref({
          spuId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          skuName: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          skuDesc: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          catalogId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          brandId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          skuDefaultImg: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          skuTitle: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          skuSubtitle: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          price: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          saleCount: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ]
  });

const init = (skuId?: number) => {
  visible.value = true;
  dataForm.skuId = "";

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  if (skuId) {
    getInfo(skuId);
  }
};

// 获取信息
const getInfo = (skuId: number) => {
  baseService.get("/product/skuinfo/" + skuId).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.skuId ? baseService.post : baseService.put)("/product/skuinfo", dataForm).then((res) => {
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
