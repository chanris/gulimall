<template>
	<!-- <el-tree 
		:data="treeList" 
		:node-key="nodeKey"
		:expand-on-click-node="false"
		:props="defaultProps">
			<template #default="{ node, data }">
				<span class="custom-tree-node">
					<span style="margin-right: 16px;">{{ data.name }}</span>
				</span>
			</template>
	</el-tree> -->

	<el-tree>
			<template #default="{ node, data }">
				<span class="custom-tree-node">
					<span style="margin-right: 16px;">{{ data.name }}</span>
				</span>
			</template>
	</el-tree>
</template>
<script lang="ts" setup>
// import {onMounted} from 'vue'
// // 数据
// const treeList = defineModel('treeList')
// const props = defineProps(['nodeKey','defaultProps'])

// onMounted(()=>{
// 	// console.log('call commontree mounted.')
// 	// console.log(props.nodeKey)
// 	// console.log(props.defaultProps)
// })

// const handleNodeClick = (data: any) => {}
</script>
<style lang="less">
</style>