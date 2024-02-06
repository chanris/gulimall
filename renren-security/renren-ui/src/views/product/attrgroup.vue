<template>
  <el-row :gutter="20">
    <el-col :span="6">
      <!-- <CommonTree 
        v-model:tree-list="treeList" 
        :node-key="'catId'"
        @node-click="handleNodeClick"
        :default-props="{ children: 'children', label: 'name' }">
      </CommonTree> -->

      <!-- 在CommonTree上 可以直接绑定 el-tree的事件 -->
      <CommonTree 
        :data="treeList" 
		    :node-key="'catId'"
        @node-click="handleNodeClick"
        :expand-on-click-node="false"
        :props="{ children: 'children', label: 'name' }">
      </CommonTree>
    </el-col>
    <el-col :span="18">
      <div class="mod-product__attrgroup">
        <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
          <el-form-item style="width: 280px;">
            <el-input v-model.trim="state.dataForm.attrGroupName" style="width: 200px; margin-right: 20px;" placeholder="模糊查询"></el-input>
            <el-button @click.stop="state.getDataList()">搜索</el-button>
          </el-form-item>
          <el-form-item>
            <el-button v-if="state.hasPermission('product:attrgroup:save')" type="primary"
              @click="addOrUpdateHandle()">新增</el-button>
          </el-form-item>
          <el-form-item>
            <el-button v-if="state.hasPermission('product:attrgroup:delete')" type="danger"
              @click="state.deleteHandle()">删除</el-button>
          </el-form-item>
        </el-form>
        <el-table v-loading="state.dataListLoading" :data="state.dataList" border
          @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
          <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
          <el-table-column prop="attrGroupId" label="分组id" header-align="center" align="center"></el-table-column>
          <el-table-column prop="attrGroupName" label="组名" header-align="center" align="center"></el-table-column>
          <el-table-column prop="sort" label="排序" header-align="center" align="center"></el-table-column>
          <el-table-column prop="descript" label="描述" header-align="center" align="center"></el-table-column>
          <el-table-column prop="icon" label="组图标" header-align="center" align="center"></el-table-column>
          <el-table-column prop="catelogId" label="所属分类id" header-align="center" align="center"></el-table-column>
          <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
            <template v-slot="scope">
              <el-button v-if="state.hasPermission('product:attrgroup:update')" type="primary" link
                @click="addOrUpdateHandle(scope.row.attrGroupId)">修改</el-button>
              <el-button v-if="state.hasPermission('product:attrgroup:delete')" type="primary" link
                @click="state.deleteHandle(scope.row.attrGroupId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit"
          :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle"
          @current-change="state.pageCurrentChangeHandle"> </el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList">确定</add-or-update>
      </div>
    </el-col>
  </el-row>
</template>
<script lang="ts" setup>
import CommonTree from '@/components/base-chanris/CommonTree.vue';
import productService from '@/service/productService';
import { ref, onMounted, reactive, toRefs } from 'vue'

const treeList = ref([])
onMounted(() => {
  productService.cateTree().then(({ data }) => {
    treeList.value = data
  })
})


import useView from "@/hooks/useView";
import AddOrUpdate from "./attrgroup-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/product/attrgroup/page",
  getDataListIsPage: true,
  exportURL: "/product/attrgroup/export",
  deleteURL: "/product/attrgroup",
  // 这里添加需要条件查询 
  dataForm: {
    attrGroupName: '',
    catelogId: ''
  }
});
// toRefs: 用于将一个响应式对象的每个属性都转换为一个响应式引用。这些响应式引用可以被解构或传递，同时保持其响应性。
const state = reactive({ ...useView(view), ...toRefs(view) });
const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};

// 节点的data对象
const handleNodeClick = (data: any) => {
  // console.log(data)
	state.dataForm.catelogId = data.catId
  state.getDataList()
}
</script>
<style lang="less"></style>