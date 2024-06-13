<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="优惠券id" prop="couponId">
        <el-input v-model="dataForm.couponId" placeholder="优惠券id"></el-input>
      </el-form-item>
          <el-form-item label="会员id" prop="memberId">
        <el-input v-model="dataForm.memberId" placeholder="会员id"></el-input>
      </el-form-item>
          <el-form-item label="会员名字" prop="memberNickName">
        <el-input v-model="dataForm.memberNickName" placeholder="会员名字"></el-input>
      </el-form-item>
          <el-form-item label="获取方式[0->后台赠送；1->主动领取]" prop="getType">
        <el-input v-model="dataForm.getType" placeholder="获取方式[0->后台赠送；1->主动领取]"></el-input>
      </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
        <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
      </el-form-item>
          <el-form-item label="使用状态[0->未使用；1->已使用；2->已过期]" prop="useType">
        <el-input v-model="dataForm.useType" placeholder="使用状态[0->未使用；1->已使用；2->已过期]"></el-input>
      </el-form-item>
          <el-form-item label="使用时间" prop="useTime">
        <el-input v-model="dataForm.useTime" placeholder="使用时间"></el-input>
      </el-form-item>
          <el-form-item label="订单id" prop="orderId">
        <el-input v-model="dataForm.orderId" placeholder="订单id"></el-input>
      </el-form-item>
          <el-form-item label="订单号" prop="orderSn">
        <el-input v-model="dataForm.orderSn" placeholder="订单号"></el-input>
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
  id: '',  couponId: '',  memberId: '',  memberNickName: '',  getType: '',  createTime: '',  useType: '',  useTime: '',  orderId: '',  orderSn: ''});

const rules = ref({
          couponId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          memberId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          memberNickName: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          getType: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          createTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          useType: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          useTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          orderId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          orderSn: [
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
  baseService.get("/coupon/couponhistory/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/coupon/couponhistory", dataForm).then((res) => {
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
