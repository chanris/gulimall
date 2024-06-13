<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="优惠卷类型[0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券]" prop="couponType">
        <el-input v-model="dataForm.couponType" placeholder="优惠卷类型[0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券]"></el-input>
      </el-form-item>
          <el-form-item label="优惠券图片" prop="couponImg">
        <el-input v-model="dataForm.couponImg" placeholder="优惠券图片"></el-input>
      </el-form-item>
          <el-form-item label="优惠卷名字" prop="couponName">
        <el-input v-model="dataForm.couponName" placeholder="优惠卷名字"></el-input>
      </el-form-item>
          <el-form-item label="数量" prop="num">
        <el-input v-model="dataForm.num" placeholder="数量"></el-input>
      </el-form-item>
          <el-form-item label="金额" prop="amount">
        <el-input v-model="dataForm.amount" placeholder="金额"></el-input>
      </el-form-item>
          <el-form-item label="每人限领张数" prop="perLimit">
        <el-input v-model="dataForm.perLimit" placeholder="每人限领张数"></el-input>
      </el-form-item>
          <el-form-item label="使用门槛" prop="minPoint">
        <el-input v-model="dataForm.minPoint" placeholder="使用门槛"></el-input>
      </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
        <el-input v-model="dataForm.startTime" placeholder="开始时间"></el-input>
      </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
        <el-input v-model="dataForm.endTime" placeholder="结束时间"></el-input>
      </el-form-item>
          <el-form-item label="使用类型[0->全场通用；1->指定分类；2->指定商品]" prop="useType">
        <el-input v-model="dataForm.useType" placeholder="使用类型[0->全场通用；1->指定分类；2->指定商品]"></el-input>
      </el-form-item>
          <el-form-item label="备注" prop="note">
        <el-input v-model="dataForm.note" placeholder="备注"></el-input>
      </el-form-item>
          <el-form-item label="发行数量" prop="publishCount">
        <el-input v-model="dataForm.publishCount" placeholder="发行数量"></el-input>
      </el-form-item>
          <el-form-item label="已使用数量" prop="useCount">
        <el-input v-model="dataForm.useCount" placeholder="已使用数量"></el-input>
      </el-form-item>
          <el-form-item label="领取数量" prop="receiveCount">
        <el-input v-model="dataForm.receiveCount" placeholder="领取数量"></el-input>
      </el-form-item>
          <el-form-item label="可以领取的开始日期" prop="enableStartTime">
        <el-input v-model="dataForm.enableStartTime" placeholder="可以领取的开始日期"></el-input>
      </el-form-item>
          <el-form-item label="可以领取的结束日期" prop="enableEndTime">
        <el-input v-model="dataForm.enableEndTime" placeholder="可以领取的结束日期"></el-input>
      </el-form-item>
          <el-form-item label="优惠码" prop="code">
        <el-input v-model="dataForm.code" placeholder="优惠码"></el-input>
      </el-form-item>
          <el-form-item label="可以领取的会员等级[0->不限等级，其他-对应等级]" prop="memberLevel">
        <el-input v-model="dataForm.memberLevel" placeholder="可以领取的会员等级[0->不限等级，其他-对应等级]"></el-input>
      </el-form-item>
          <el-form-item label="发布状态[0-未发布，1-已发布]" prop="publish">
        <el-input v-model="dataForm.publish" placeholder="发布状态[0-未发布，1-已发布]"></el-input>
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
  id: '',  couponType: '',  couponImg: '',  couponName: '',  num: '',  amount: '',  perLimit: '',  minPoint: '',  startTime: '',  endTime: '',  useType: '',  note: '',  publishCount: '',  useCount: '',  receiveCount: '',  enableStartTime: '',  enableEndTime: '',  code: '',  memberLevel: '',  publish: ''});

const rules = ref({
          couponType: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          couponImg: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          couponName: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          num: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          amount: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          perLimit: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          minPoint: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          startTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          endTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          useType: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          note: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          publishCount: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          useCount: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          receiveCount: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          enableStartTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          enableEndTime: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          code: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          memberLevel: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          publish: [
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
  baseService.get("/coupon/coupon/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/coupon/coupon", dataForm).then((res) => {
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
