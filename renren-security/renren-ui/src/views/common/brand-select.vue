<template>
	<el-select
    v-model="brandId"
    placeholder="选择品牌"
    size="large"
	clearable
    style="width: 240px">
    <el-option
      v-for="item in brandList"
      :key="item.brandId"
      :label="item.name"
      :value="item.brandId"
    />
  </el-select>
</template>
<script lang="ts" setup>
import productService from '@/service/productService';
import {onMounted, ref, watch} from 'vue'
const brandId = defineModel('brandId')
// const props = defineProps(['brandList'])
const brandList = ref()
onMounted(()=>{
  productService.get('product/brand/page', {limit: -1})
  .then(({data})=>{
    brandList.value = data.list
  })
})
// 监控props.brandList的值，当传入有效值后，将brandList置为 props.brandList的值
// watch(()=>props.brandList, (val,oldVal)=>{
//   if(val) {
//     brandList.value = val
//   }
// })
</script>
<style lang="less">
</style>