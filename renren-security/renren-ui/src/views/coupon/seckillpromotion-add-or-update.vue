<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="活动标题" prop="title">
        <el-input v-model="dataForm.title" placeholder="活动标题"></el-input>
      </el-form-item>
          <el-form-item label="开始日期" prop="startTime">
        <el-input v-model="dataForm.startTime" placeholder="开始日期"></el-input>
      </el-form-item>
          <el-form-item label="结束日期" prop="endTime">
        <el-input v-model="dataForm.endTime" placeholder="结束日期"></el-input>
      </el-form-item>
          <el-form-item label="上下线状态" prop="status">
        <el-input v-model="dataForm.status" placeholder="上下线状态"></el-input>
      </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
        <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
      </el-form-item>
          <el-form-item label="创建人" prop="userId">
        <el-input v-model="dataForm.userId" placeholder="创建人"></el-input>
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
  id: '',  title: '',  startTime: '',  endTime: '',  status: '',  createTime: '',  userId: ''});

const rules = ref({
          title: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          startTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          endTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          status: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          createTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          userId: [
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
  baseService.get("/coupon/seckillpromotion/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/coupon/seckillpromotion", dataForm).then((res) => {
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
