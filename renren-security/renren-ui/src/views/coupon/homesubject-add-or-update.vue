<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="专题名字" prop="name">
        <el-input v-model="dataForm.name" placeholder="专题名字"></el-input>
      </el-form-item>
          <el-form-item label="专题标题" prop="title">
        <el-input v-model="dataForm.title" placeholder="专题标题"></el-input>
      </el-form-item>
          <el-form-item label="专题副标题" prop="subTitle">
        <el-input v-model="dataForm.subTitle" placeholder="专题副标题"></el-input>
      </el-form-item>
          <el-form-item label="显示状态" prop="status">
        <el-input v-model="dataForm.status" placeholder="显示状态"></el-input>
      </el-form-item>
          <el-form-item label="详情连接" prop="url">
        <el-input v-model="dataForm.url" placeholder="详情连接"></el-input>
      </el-form-item>
          <el-form-item label="排序" prop="sort">
        <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
      </el-form-item>
          <el-form-item label="专题图片地址" prop="img">
        <el-input v-model="dataForm.img" placeholder="专题图片地址"></el-input>
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
  id: '',  name: '',  title: '',  subTitle: '',  status: '',  url: '',  sort: '',  img: ''});

const rules = ref({
          name: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          title: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          subTitle: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          status: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          url: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          sort: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          img: [
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
  baseService.get("/coupon/homesubject/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/coupon/homesubject", dataForm).then((res) => {
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
