<template>
	<el-select
	clearable
    v-model="wareId"
    placeholder="选择仓库"
    size="large"
    style="width: 240px">
    <el-option
      v-for="item in wareInfoList"
      :key="item.id"
      :label="item.name"
      :value="item.id"
    />
  </el-select>
</template>
<script lang="ts" setup>
import productService from '@/service/productService';
import {onMounted, ref} from 'vue'
const wareId = defineModel('wareId')
const wareInfoList = ref()
onMounted(()=>{
  productService.get('ware/wareinfo/page', {limit: -1})
  .then(({data})=>{
    wareInfoList.value = data.list
  })
})
</script>
<style lang="less">
</style>