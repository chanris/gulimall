<template>
	<!-- <el-tree-select v-model="catalogId" :data="treeList" check-strictly :render-after-expand="false"
    style="width: 240px" :props="{ children: 'children', label: 'name', value: 'catId' }" /> -->
	<el-cascader v-model="catalogIds" :options="treeList" :props="props" clearable ></el-cascader>
</template>
<script lang="ts" setup>
import {ref, watch} from 'vue'
import productService from '@/service/productService';
const catalogId = defineModel<Number | null>("catalogId")
const catalogIds = ref([])
const treeList = ref([])
const props = ref({
	label: 'name',
	value: 'catId',
	children: 'children',
	checkStrictly: true,
})
productService.cateTree().then(({data})=>{
	treeList.value = data
})
watch(()=> catalogIds.value, (val, oldVal)=>{
	if(val) {
		catalogId.value = val[val.length-1]
		// console.log(catalogId.value)
	}
})
</script>
<style lang="less">
</style>