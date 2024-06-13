<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item label="活动场次id" prop="promotionSessionId" >
        <el-input v-model="dataForm.promotionSessionId" :disabled="true"  placeholder="活动场次id"></el-input>
      </el-form-item>
          <el-form-item label="商品id" prop="skuId">
        <el-input v-model="dataForm.skuId" placeholder="商品id"></el-input>
      </el-form-item>
          <el-form-item label="秒杀价格" prop="seckillPrice">
        <el-input v-model="dataForm.seckillPrice" placeholder="秒杀价格"></el-input>
      </el-form-item>
          <el-form-item label="秒杀总量" prop="seckillCount">
        <el-input v-model="dataForm.seckillCount" placeholder="秒杀总量"></el-input>
      </el-form-item>
          <el-form-item label="每人限购数量" prop="seckillLimit">
        <el-input v-model="dataForm.seckillLimit" placeholder="每人限购数量"></el-input>
      </el-form-item>
          <el-form-item label="排序" prop="seckillSort">
        <el-input v-model="dataForm.seckillSort" placeholder="排序"></el-input>
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
  id: '',  promotionId: '',  promotionSessionId: '',  skuId: '',  seckillPrice: '',  seckillCount: '',  seckillLimit: '',  seckillSort: ''});

const rules = ref({
          promotionId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          promotionSessionId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          skuId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          seckillPrice: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          seckillCount: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          seckillLimit: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          seckillSort: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ]
  });

const init = (fixedPromotionSessionId:string, id?: number) => {
  visible.value = true;
  dataForm.id = "";

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }
  dataForm.promotionSessionId = fixedPromotionSessionId
  if (id) {
    getInfo(id);
  }
};

// 获取信息
const getInfo = (id: number) => {
  baseService.get("/coupon/seckillskurelation/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/coupon/seckillskurelation", dataForm).then((res) => {
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
