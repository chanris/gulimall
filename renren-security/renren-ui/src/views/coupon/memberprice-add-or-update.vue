<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="sku_id" prop="skuId">
        <el-input v-model="dataForm.skuId" placeholder="sku_id"></el-input>
      </el-form-item>
          <el-form-item label="会员等级id" prop="memberLevelId">
        <el-input v-model="dataForm.memberLevelId" placeholder="会员等级id"></el-input>
      </el-form-item>
          <el-form-item label="会员等级名" prop="memberLevelName">
        <el-input v-model="dataForm.memberLevelName" placeholder="会员等级名"></el-input>
      </el-form-item>
          <el-form-item label="会员对应价格" prop="memberPrice">
        <el-input v-model="dataForm.memberPrice" placeholder="会员对应价格"></el-input>
      </el-form-item>
          <el-form-item label="可否叠加其他优惠[0-不可叠加优惠，1-可叠加]" prop="addOther">
        <el-input v-model="dataForm.addOther" placeholder="可否叠加其他优惠[0-不可叠加优惠，1-可叠加]"></el-input>
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
  id: '',  skuId: '',  memberLevelId: '',  memberLevelName: '',  memberPrice: '',  addOther: ''});

const rules = ref({
          skuId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          memberLevelId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          memberLevelName: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          memberPrice: [
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
  baseService.get("/coupon/memberprice/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/coupon/memberprice", dataForm).then((res) => {
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
