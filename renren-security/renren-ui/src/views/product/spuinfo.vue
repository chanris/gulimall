<template>
	<div class="mod-product__spuinfo">
		<el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
			<el-form-item label="分类" props="catalogId">
				<!-- <el-tree-select v-model="state.dataForm.catalogId" :data="treeList" check-strictly :render-after-expand="false"
          					style="width: 240px" :props="{ children: 'children', label: 'name', value: 'catId' }" /> -->
				<category-cascader v-model:catalog-id="state.dataForm.catalogId"></category-cascader>
			</el-form-item>
			<el-form-item label="分类" props="brandId">
				<brandSelect v-model:brand-id="state.dataForm.brandId"></brandSelect>
			</el-form-item>
			<el-form-item label="上架状态" props="publishStatus">
				<el-select
					v-model="state.dataForm.publishStatus"
					placeholder="选择上架状态"
					size="large"
					style="width: 240px">
					<el-option
						v-for="item in publisStatusCollect"
						:key="item.value"
						:label="item.label"
						:value="item.value"
					/>
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button color="#626aef" @click.stop="state.getDataList()">检索</el-button>
			</el-form-item>
			<el-form-item>
				<el-button v-if="state.hasPermission('product:spuinfo:save')" type="primary"
					@click="addOrUpdateHandle()">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-button v-if="state.hasPermission('product:spuinfo:delete')" type="danger"
					@click="state.deleteHandle()">删除</el-button>
			</el-form-item>
		</el-form>
		<el-table v-loading="state.dataListLoading" :data="state.dataList" border
			@selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
			<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
			<el-table-column prop="id" label="商品id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="spuName" label="商品名称" header-align="center" align="center"></el-table-column>
			<el-table-column prop="spuDescription" label="商品描述" header-align="center" align="center"></el-table-column>
			<el-table-column prop="catalogId" label="所属分类id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="brandId" label="品牌id" header-align="center" align="center"></el-table-column>
			<el-table-column prop="weight" label="商品重量" header-align="center" align="center"></el-table-column>
			<el-table-column prop="publishStatus" label="上架状态" header-align="center" align="center">
				<template #default="scope">
					<el-tag v-if="scope.row.publishStatus === 1" type="primary">已上架</el-tag>
					<el-tag v-else type="danger">未上架</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" header-align="center" min-width="100" align="center"></el-table-column>
			<el-table-column prop="updateTime" label="更新时间" header-align="center" min-width="100" align="center"></el-table-column>
			<el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
				<template v-slot="scope">
					<el-button v-if="state.hasPermission('product:spuinfo:update')" type="primary" link
						@click="listing(scope.row.id)">上架</el-button>
					<!-- <el-button v-if="state.hasPermission('product:spuinfo:update')" type="primary" link
						@click="addOrUpdateHandle(scope.row.id)">修改</el-button> -->
					<!-- <el-button v-if="state.hasPermission('product:spuinfo:delete')" type="primary" link
						@click="state.deleteHandle(scope.row.id)">删除</el-button> -->
					<el-button v-if="state.hasPermission('product:spuinfo:delete')" type="primary" link
						@click="attrUpdateShow(scope.row)">规格维护</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit"
			:total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle"
			@current-change="state.pageCurrentChangeHandle"> </el-pagination>
		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList">确定</add-or-update>
	</div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs, onMounted} from "vue";
import AddOrUpdate from "./spuinfo-add-or-update.vue";
import productService from "@/service/productService";
import brandSelect from "../common/brand-select.vue";
import categoryCascader from "../common/category-cascader.vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
const view = reactive({
	deleteIsBatch: true,
	getDataListURL: "/product/spuinfo/page",
	getDataListIsPage: true,
	exportURL: "/product/spuinfo/export",
	deleteURL: "/product/spuinfo",
	dataForm: {
		catalogId: null,
		brandId: null,
		publishStatus: null
	}
});
const treeList = ref([])
const publisStatusCollect = ref<any>([])

onMounted(()=>{
	publisStatusCollect.value.push({label: '已上架', value: 1})
	publisStatusCollect.value.push({label: '未上架', value: 0})
	publisStatusCollect.value.push({label: '全部', value: null})
	productService.cateTree().then(({data})=>{
			treeList.value = data
		})
})
const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const listing = (spuId: number) => {
	productService.put('/product/spuinfo', {id: spuId, publishStatus: 1})
	.then((resp)=>{
		if(resp.code === 0) {
			ElMessage.success({message: '上架成功'})
			state.getDataList()
		}else {
			ElMessage.error({message: '上架失败'})
		}
	})
}
const router = useRouter()
const attrUpdateShow = (row: any) => {
	router.push({
	path: "/product-attrupdate",
	query: { spuId: row.id, catalogId: row.catalogId }
	});
}
const addOrUpdateHandle = (id?: number) => {
	addOrUpdateRef.value.init(id);
};
</script>
