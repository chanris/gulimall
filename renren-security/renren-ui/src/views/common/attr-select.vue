<template>
	<el-select
    v-model="attrId"
    placeholder="选择属性"
    size="large"
	clearable
    style="width: 240px">
    <el-option
      v-for="item in attrList"
      :key="item.attrId"
      :label="item.attrName"
      :value="item.attrId"
    />
  </el-select>
</template>
<script lang="ts" setup>
import productService from '@/service/productService';
import {onMounted, ref, watch} from 'vue'
const attrId = defineModel('attrId')
const attrgroupId = defineModel('attrgroupId')
const attrList = ref()

watch(()=>attrgroupId.value, (val, oldVal)=>{
	if(val) {
		// console.log('attr-select attrgroupId', val)
		productService.get(`product/attrgroup/${val}/noattr/relation`, {})
		.then(({data})=>{
			attrList.value = data
		})
	}
}, {immediate: true})

</script>
<style lang="less">
</style>