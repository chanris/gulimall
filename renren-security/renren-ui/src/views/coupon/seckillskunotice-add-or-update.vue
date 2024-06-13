<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="member_id" prop="memberId">
        <el-input v-model="dataForm.memberId" placeholder="member_id"></el-input>
      </el-form-item>
          <el-form-item label="sku_id" prop="skuId">
        <el-input v-model="dataForm.skuId" placeholder="sku_id"></el-input>
      </el-form-item>
          <el-form-item label="活动场次id" prop="sessionId">
        <el-input v-model="dataForm.sessionId" placeholder="活动场次id"></el-input>
      </el-form-item>
          <el-form-item label="订阅时间" prop="subcribeTime">
        <el-input v-model="dataForm.subcribeTime" placeholder="订阅时间"></el-input>
      </el-form-item>
          <el-form-item label="发送时间" prop="sendTime">
        <el-input v-model="dataForm.sendTime" placeholder="发送时间"></el-input>
      </el-form-item>
          <el-form-item label="通知方式[0-短信，1-邮件]" prop="noticeType">
        <el-input v-model="dataForm.noticeType" placeholder="通知方式[0-短信，1-邮件]"></el-input>
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
  id: '',  memberId: '',  skuId: '',  sessionId: '',  subcribeTime: '',  sendTime: '',  noticeType: ''});

const rules = ref({
          memberId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          skuId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          sessionId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          subcribeTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          sendTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          noticeType: [
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
  baseService.get("/coupon/seckillskunotice/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/coupon/seckillskunotice", dataForm).then((res) => {
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
