<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="spu_id" prop="skuId">
        <el-input v-model="dataForm.skuId" placeholder="spu_id"></el-input>
      </el-form-item>
          <el-form-item label="满几件" prop="fullCount">
        <el-input v-model="dataForm.fullCount" placeholder="满几件"></el-input>
      </el-form-item>
          <el-form-item label="打几折" prop="discount">
        <el-input v-model="dataForm.discount" placeholder="打几折"></el-input>
      </el-form-item>
          <el-form-item label="折后价" prop="price">
        <el-input v-model="dataForm.price" placeholder="折后价"></el-input>
      </el-form-item>
          <el-form-item label="是否叠加其他优惠[0-不可叠加，1-可叠加]" prop="addOther">
        <el-input v-model="dataForm.addOther" placeholder="是否叠加其他优惠[0-不可叠加，1-可叠加]"></el-input>
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
  id: '',  skuId: '',  fullCount: '',  discount: '',  price: '',  addOther: ''});

const rules = ref({
          skuId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          fullCount: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          discount: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          price: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          addOther: [
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
  baseService.get("/coupon/skuladder/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/coupon/skuladder", dataForm).then((res) => {
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
