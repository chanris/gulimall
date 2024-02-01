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
						<a style="margin-left: 8px; color: #409EFF;" @click="dialogFormVisible = true; updateForm = data">修改</a>
						<a style="margin-left: 8px; color: #F56C6C;" @click="remove(node, data)">删除</a>
					</span>
				</span>
			</template>
		</el-tree>
		<el-dialog v-model="dialogFormVisible" title="更新分类" width="30%"
			:close-on-click-modal="false"
			:close-on-press-escape="false"
			>
			<el-form  ref="updateFormRef" :model="updateForm" :rules="updateFormRules">
				<el-form-item label="分类名称" :label-width="80">
					<el-input v-model="updateForm.name" autocomplete="off" />
				</el-form-item>
				<el-form-item label="分类图标" :label-width="80">
					<el-input v-model="updateForm.icon" autocomplete="off" />
				</el-form-item>
				<el-form-item label="计量单位" :label-width="80">
					<el-input v-model="updateForm.productUnit" autocomplete="off" />
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取消</el-button>
					<el-button type="primary" @click="submitUpdate(updateFormRef)">更新</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script lang="ts" setup>
import type ElTreeNode from 'element-plus/es/components/tree/src/model/node'
import productService from '@/service/productService'
import { onMounted, ref, reactive } from 'vue'
import { ElMessage, ElMessageBox, } from 'element-plus'
import type{ FormInstance, FormRules} from 'element-plus'

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
	productUnit: string
	icon: string
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
			// 更新数据 方式一：
			// productService.cateTree().then(({ data }) => {
			// 	treeData.value = data
			// })
			// 方式二：
			node.data.name = value
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

// 修改分类名称
const updateFormRef = ref<FormInstance>() // vue ref
const updateFormRules = reactive<FormRules>({ // updateForm Rules 
	name: [
		{ required: true, message: '请输入分类名称', trigger: 'blur'}
	],
	productUnit: [
		{ required: true, message: '请输入计量单位', trigger: 'blur'}
	],
	icon: [
		{ required: true, message: '请输入分类图标', trigger: 'blur'}
	],
})
const updateForm = ref({ name: '', productUnit: '', icon: ''}) // updateForm Data
const dialogFormVisible = ref(false) // update dialog visible control  
const submitUpdate = (formEl: FormInstance | undefined) => {
	if(!formEl) return 
	formEl.validate((valid, fields) => {
		if(valid) {
			productService.updateCate(updateForm.value)
			.then((resp)=>{
				ElMessage.success({message: '更新成功'})
				productService.cateTree().then(({ data }) => {
					treeData.value = data
				})
			})
			.finally(()=>{
				dialogFormVisible.value = false
			})
		} else {
			ElMessage.error({message: '请输入合法数据'})
		}
	})
	
}

</script>
<style lang="less"></style>