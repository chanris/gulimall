<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item label="场次名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="场次名称"></el-input>
      </el-form-item>
      <el-form-item label="每日开始时间" prop="startTime">
        <!-- <el-input v-model="dataForm.startTime" placeholder="每日开始时间"></el-input> -->
		<el-date-picker
        v-model="dataForm.startTime"
        type="datetime"
        placeholder="每日开始时间"/>
      </el-form-item>
      <el-form-item label="每日结束时间" prop="endTime">
		<el-date-picker
        v-model="dataForm.endTime"
        type="datetime"
        placeholder="每日结束时间"/>
      </el-form-item>
      <el-form-item label="启用状态" prop="status">
        <el-input v-model="dataForm.status" placeholder="启用状态"></el-input>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
        v-model="dataForm.createTime"
        type="datetime"
        placeholder="创建时间"/>
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
  id: "",
  name: "",
  startTime: "",
  endTime: "",
  status: "",
  createTime: ""
});

const rules = ref({
  name: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  startTime: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  endTime: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  status: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  createTime: [{ required: true, message: "必填项不能为空", trigger: "blur" }]
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
  baseService.get("/coupon/seckillsession/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/coupon/seckillsession", dataForm).then((res) => {
      ElMessage.success({
        message: "成功",
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
