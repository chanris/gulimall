<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="order_id" prop="orderId">
        <el-input v-model="dataForm.orderId" placeholder="order_id"></el-input>
      </el-form-item>
          <el-form-item label="order_sn" prop="orderSn">
        <el-input v-model="dataForm.orderSn" placeholder="order_sn"></el-input>
      </el-form-item>
          <el-form-item label="收货人" prop="consignee">
        <el-input v-model="dataForm.consignee" placeholder="收货人"></el-input>
      </el-form-item>
          <el-form-item label="收货人电话" prop="consigneeTel">
        <el-input v-model="dataForm.consigneeTel" placeholder="收货人电话"></el-input>
      </el-form-item>
          <el-form-item label="配送地址" prop="deliveryAddress">
        <el-input v-model="dataForm.deliveryAddress" placeholder="配送地址"></el-input>
      </el-form-item>
          <el-form-item label="订单备注" prop="orderComment">
        <el-input v-model="dataForm.orderComment" placeholder="订单备注"></el-input>
      </el-form-item>
          <el-form-item label="付款方式【 1:在线付款 2:货到付款】" prop="paymentWay">
        <el-input v-model="dataForm.paymentWay" placeholder="付款方式【 1:在线付款 2:货到付款】"></el-input>
      </el-form-item>
          <el-form-item label="任务状态" prop="taskStatus">
        <el-input v-model="dataForm.taskStatus" placeholder="任务状态"></el-input>
      </el-form-item>
          <el-form-item label="订单描述" prop="orderBody">
        <el-input v-model="dataForm.orderBody" placeholder="订单描述"></el-input>
      </el-form-item>
          <el-form-item label="物流单号" prop="trackingNo">
        <el-input v-model="dataForm.trackingNo" placeholder="物流单号"></el-input>
      </el-form-item>
          <el-form-item label="create_time" prop="createTime">
        <el-input v-model="dataForm.createTime" placeholder="create_time"></el-input>
      </el-form-item>
          <el-form-item label="仓库id" prop="wareId">
        <el-input v-model="dataForm.wareId" placeholder="仓库id"></el-input>
      </el-form-item>
          <el-form-item label="工作单备注" prop="taskComment">
        <el-input v-model="dataForm.taskComment" placeholder="工作单备注"></el-input>
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
  id: '',  orderId: '',  orderSn: '',  consignee: '',  consigneeTel: '',  deliveryAddress: '',  orderComment: '',  paymentWay: '',  taskStatus: '',  orderBody: '',  trackingNo: '',  createTime: '',  wareId: '',  taskComment: ''});

const rules = ref({
          orderId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          orderSn: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          consignee: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          consigneeTel: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          deliveryAddress: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          orderComment: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          paymentWay: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          taskStatus: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          orderBody: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          trackingNo: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          createTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          wareId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          taskComment: [
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
  baseService.get("/ware/wareordertask/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/ware/wareordertask", dataForm).then((res) => {
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
