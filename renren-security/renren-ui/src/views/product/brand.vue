<template>
  <div class="mod-product__brand">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button v-if="state.hasPermission('product:brand:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('product:brand:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
              <el-table-column prop="brandId" label="品牌id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="name" label="品牌名" header-align="center" align="center"></el-table-column>
              <el-table-column prop="logo" label="品牌logo地址" header-align="center" align="center"></el-table-column>
              <el-table-column prop="descript" label="介绍" header-align="center" align="center"></el-table-column>
              <el-table-column prop="showStatus" label="显示状态" header-align="center" align="center">
				<template #default="scope">
					<el-switch
						:v-loading="loading"
						v-model="scope.row.showStatus"
						inline-prompt
						@change="changeShowStatus(scope.row)"
						style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
						active-text="启用"
						inactive-text="禁用"
						:active-value="1"
						:inactive-value="0"
					/>
				</template>
			  </el-table-column>
              <el-table-column prop="firstLetter" label="检索首字母" header-align="center" align="center"></el-table-column>
              <el-table-column prop="sort" label="排序" header-align="center" align="center"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
			<!-- 有坑：默认 生成的addOrUpdateHandle(scope.row.id)，找不到id 修改会变成新增 需要改成对应的表id字段 eg: addOrUpdateHandle(scope.row.brandId)，其他同理 -->
          <el-button v-if="state.hasPermission('product:brand:update')" type="primary" link @click="addOrUpdateHandle(scope.row.brandId)">修改</el-button>
          <el-button v-if="state.hasPermission('product:brand:delete')" type="primary" link @click="state.deleteHandle(scope.row.brandId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList">确定</add-or-update>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs, computed , watch} from "vue";
import AddOrUpdate from "./brand-add-or-update.vue";
import commonService from "@/service/commonService";
import { ElMessage } from "element-plus";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/product/brand/page",
  getDataListIsPage: true,
  exportURL: "/product/brand/export",
  deleteURL: "/product/brand"
});

const state = reactive({ ...useView(view), ...toRefs(view) });
const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
const loading = ref(false)
const changeShowStatus = (row?: any) => {
	if(!row) return
	loading.value = true
	let dataForm = [row.brandId]
	commonService.delete("/product/brand", dataForm).then((res)=>{
		ElMessage.success({ message: '删除成功'})
		if(state.dataList) {
			let idx = -1
			for(let i = 0; i < state.dataList.length; i++) {
				if(state.dataList[i].brandId === row.brandId) {
					idx = i
					break
				}
			}
			if(idx !== -1) {
				state.dataList.splice(idx, 1)
			}
		}
		// state.dataForm = state.dataForm.value.filter(item => {
		// 	return item.brandId !== row.brandId
		// })
		
	}).finally(()=>{ loading.value = false })
}

</script>
