<template>
	<div>
		<el-tree ref="treeEl" 
		:data="treeData" 
		node-key="catId" 
	
		:expand-on-click-node="false" 
		:props="defaultProps"
			@node-click="handleNodeClick">
			<template #default="{ node, data }">
				<span class="custom-tree-node">
					<span style="margin-right: 16px;">{{ data.name }}</span>
					<span>
						<a v-if="node.level < 3" @click="append(node, data)">添加</a>

						<a style="margin-left: 8px" @click="remove(node, data)"> 删除 </a>
					</span>
				</span>
			</template>
		</el-tree>

	</div>
</template>

<script lang="ts" setup>
import type ElTreeNode from 'element-plus/es/components/tree/src/model/node'
import productService from '@/service/productService';
import { onMounted, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';

// 分类树
const treeData = ref<CateNode[]>([])

onMounted(() => {
	productService.cateTree().then(({ data }) => {
		treeData.value = data
	})
})

interface CateNode {
	catId: number
	name: string
	children?: CateNode[]
}

const defaultProps = {
	children: 'children',
	label: 'name',
}

const handleNodeClick = (data: CateNode) => {
	console.log('当前选择的node:')
	console.log(data)
}

// 添加子分类
const addCateDialog = ref<boolean>(false)
const treeEl = ref()
const append = (node: ElTreeNode, data: CateNode) => {
	ElMessageBox.prompt('请输入分类名称', '添加分类', {
		confirmButtonText: '添加',
		cancelButtonText: '取消',
		inputPattern: /.+/,
		inputErrorMessage: '名称不能为空',
	})
	.then(({ value }) => {
		let parentCateId = data.catId
		let params = { name: value, parentCid: parentCateId, catLevel: node.level + 1, sort: 0 }
		productService.saveCate(params).then((resp) => {
			ElMessage.success({
				message: '添加成功'
			})
			productService.cateTree().then(({ data }) => {
				treeData.value = data
			})
		}).finally(() => {
			addCateDialog.value = false
		})
	})
	.catch(() => {})

}

/**
 * 删除分类树 节点(注意：只有没有子节点的 node 才能删除)
 * @param node ElTreeNode
 * @param data CateNode
 */
const remove = (node: ElTreeNode, data: CateNode) => {
	console.log(node)
	if (!node.childNodes || node.childNodes.length == 0) {
		const parent = node.parent
		let catId = node.data.catId
		let index = -1
		parent.childNodes.forEach((elNode, _index) => {
			if (elNode.data.catId == catId) {
				index = _index
			}
		})
		productService.deleteCate([data.catId]).then((resp)=>{
			ElMessage.success({
				message: '删除成功'
			})
			// Array.splice(索引, 删除个数)
			parent.childNodes.splice(index, 1)
		})
	} else {
		ElMessage({
			type: 'error',
			message: '分类存在子节点不能删除！'
		})
	}
}
</script>
<style lang="less"></style>