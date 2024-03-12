<template>
  <el-dialog v-model="visible" :title="!dataForm.attrId ? '新增' : '修改'" :close-on-click-modal="false"
    :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item label="属性名" prop="attrName">
        <el-input v-model="dataForm.attrName" placeholder="属性名"></el-input>
      </el-form-item>
      <el-form-item label="是否需要检索" prop="searchType">
        <!-- <el-input v-model="" placeholder="是否需要检索[0-不需要，1-需要]"></el-input> -->
        <el-switch v-model="dataForm.searchType" active-text="需要" inactive-text="不需要" :active-value="1"
          :inactive-value="0"></el-switch>
      </el-form-item>
      <el-form-item label="值类型" prop="valueType">
        <!-- <el-input v-model="dataForm.valueType" placeholder="值类型[0-为单个值，1-可以选择多个值]"></el-input> -->
        <el-switch v-model="dataForm.valueType" active-text="多值" inactive-text="单值" :active-value="1"
          :inactive-value="0"></el-switch>
      </el-form-item>
      <el-form-item label="属性图标" prop="icon">
        <el-input v-model="dataForm.icon" placeholder="属性图标"></el-input>
      </el-form-item>
      <el-form-item label="可选值列表" prop="valueSelect">
        <el-input v-model="dataForm.valueSelect" placeholder="可选值列表[用分号分隔]"></el-input>
      </el-form-item>
      <!-- <el-form-item label="属性类型" prop="attrType">
        <el-input v-model="dataForm.attrType" placeholder="属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]"></el-input>
      </el-form-item> -->
      <el-form-item label="启用状态" prop="enable">
        <!-- <el-input v-model="dataForm.enable" placeholder="启用状态[0 - 禁用，1 - 启用]"></el-input> -->
        <el-switch v-model="dataForm.enable" active-text="启用" inactive-text="禁用" :active-value="1"
          :inactive-value="0"></el-switch>
      </el-form-item>
      <el-form-item label="所属分类" prop="catelogId">
        <!-- <el-input v-model="dataForm.catelogId" placeholder="所属分类"></el-input> -->
        <el-tree-select v-model="dataForm.catelogId" :data="props.treeList" check-strictly :render-after-expand="false"
          style="width: 240px" :props="{ children: 'children', label: 'name', value: 'catId' }" />
      </el-form-item>
      <el-form-item label="所属分组" prop="attrGroupId">
        <!-- <el-input v-model="dataForm.catelogId" placeholder="所属分类"></el-input> -->
        <el-select v-model="dataForm.attrGroupId" placeholder="请选择分组" size="large" style="width: 240px">
          <el-option v-for="item in attrGroupList" :key="item.attrGroupId" :label="item.attrGroupName"
            :value="item.attrGroupId" />
        </el-select>
      </el-form-item>
      <el-form-item label="快速展示" prop="showDesc">
        <!-- <el-input v-model="dataForm.showDesc" placeholder="快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整"></el-input> -->
        <el-switch v-model="dataForm.showDesc" active-text="是" inactive-text="否" :active-value="1"
          :inactive-value="0"></el-switch>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref, watch, onMounted } from "vue";
import baseService from "@/service/commonService";
import { ElMessage } from "element-plus";
import productService from "@/service/productService";
const emit = defineEmits(["refreshDataList"]);
const props = defineProps(["treeList"])
const visible = ref(false);
const dataFormRef = ref();
const attrGroupList = ref<any>([])

const dataForm = reactive({
  attrId: '', attrName: '', searchType: 0, valueType: 0, icon: '', 
  valueSelect: '', attrType: 1, enable: 1, catelogId: '', attrGroupId: '', showDesc: 1
});
watch(() => dataForm.catelogId, (val, prevVal) => {
  if (val) {
    productService.attrGroupList(val).then(({ data }) => {
      attrGroupList.value = data.list
    })
  }
})
const rules = ref({
  attrName: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  searchType: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  valueType: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  icon: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  valueSelect: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  enable: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  catelogId: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ],
  showDesc: [
    { required: true, message: '必填项不能为空', trigger: 'blur' }
  ]
});

const init = (attrId?: number, attrType?:number) => {
  visible.value = true;
  dataForm.attrId = "";
  attrGroupList.value = []
  dataForm.attrGroupId = ''; //清空选择
  // Boolean(0) ==> false
  if(attrType === 0) {
    dataForm.attrType = attrType
  }
  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  if (attrId) {
    getInfo(attrId);
  }
};

// 获取信息
const getInfo = (attrId: number) => {
  baseService.get("/product/attr/" + attrId).then((res) => {  
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.attrId ? baseService.post : baseService.put)("/product/attr", dataForm).then((res) => {
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
