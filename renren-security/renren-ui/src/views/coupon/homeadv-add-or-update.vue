<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="名字" prop="name">
        <el-input v-model="dataForm.name" placeholder="名字"></el-input>
      </el-form-item>
          <el-form-item label="图片地址" prop="pic">
        <el-input v-model="dataForm.pic" placeholder="图片地址"></el-input>
      </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
        <el-input v-model="dataForm.startTime" placeholder="开始时间"></el-input>
      </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
        <el-input v-model="dataForm.endTime" placeholder="结束时间"></el-input>
      </el-form-item>
          <el-form-item label="状态" prop="status">
        <el-input v-model="dataForm.status" placeholder="状态"></el-input>
      </el-form-item>
          <el-form-item label="点击数" prop="clickCount">
        <el-input v-model="dataForm.clickCount" placeholder="点击数"></el-input>
      </el-form-item>
          <el-form-item label="广告详情连接地址" prop="url">
        <el-input v-model="dataForm.url" placeholder="广告详情连接地址"></el-input>
      </el-form-item>
          <el-form-item label="备注" prop="note">
        <el-input v-model="dataForm.note" placeholder="备注"></el-input>
      </el-form-item>
          <el-form-item label="排序" prop="sort">
        <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
      </el-form-item>
          <el-form-item label="发布者" prop="publisherId">
        <el-input v-model="dataForm.publisherId" placeholder="发布者"></el-input>
      </el-form-item>
          <el-form-item label="审核者" prop="authId">
        <el-input v-model="dataForm.authId" placeholder="审核者"></el-input>
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
  id: '',  name: '',  pic: '',  startTime: '',  endTime: '',  status: '',  clickCount: '',  url: '',  note: '',  sort: '',  publisherId: '',  authId: ''});

const rules = ref({
          name: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          pic: [
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
          clickCount: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          url: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          note: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          sort: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          publisherId: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          authId: [
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
  baseService.get("/coupon/homeadv/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/coupon/homeadv", dataForm).then((res) => {
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
