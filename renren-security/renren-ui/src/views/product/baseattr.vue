<template>
  <el-row :gutter="20">
    <el-col :span="8">
      <CommonTree :data="treeList" :node-key="'catId'" @node-click="handleNodeClick" :expand-on-click-node="false"
        :props="{ children: 'children', label: 'name' }">
      </CommonTree>
    </el-col>
    <el-col :span="16">
      <div class="mod-product__attr">
        <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
          <el-form-item>
            <el-button v-if="state.hasPermission('product:attr:save')" type="primary"
              @click="addOrUpdateHandle()">新增</el-button>
          </el-form-item>
          <el-form-item>
            <el-button v-if="state.hasPermission('product:attr:delete')" type="danger"
              @click="state.deleteHandle()">删除</el-button>
          </el-form-item>
        </el-form>
        <el-table v-loading="state.dataListLoading" :data="state.dataList" border
          @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
          <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
          <el-table-column prop="attrId" label="属性id" header-align="center" align="center"></el-table-column>
          <el-table-column prop="attrName" label="属性名" header-align="center" align="center"></el-table-column>
          <el-table-column prop="searchType" label="是否需要检索" header-align="center" align="center"></el-table-column>
          <el-table-column prop="valueType" label="值类型" header-align="center" align="center"></el-table-column>
          <el-table-column prop="icon" label="属性图标" header-align="center" align="center"></el-table-column>
          <el-table-column prop="valueSelect" label="可选值列表" header-align="center" align="center"></el-table-column>
          <el-table-column prop="attrType" label="属性类型" header-align="center" align="center"></el-table-column>
          <el-table-column prop="enable" label="启用状态" header-align="center" align="center"></el-table-column>
          <el-table-column prop="catelogId" label="所属分类id" header-align="center" align="center"></el-table-column>
          <el-table-column prop="attrGroupId" label="所属分组id" header-align="center" align="center"></el-table-column>
          <el-table-column prop="showDesc" label="快速展示" header-align="center" align="center"></el-table-column>
          <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
            <template v-slot="scope">
              <el-button v-if="state.hasPermission('product:attr:update')" type="primary" link
                @click="addOrUpdateHandle(scope.row.attrId)">修改</el-button>
              <el-button v-if="state.hasPermission('product:attr:delete')" type="primary" link
                @click="state.deleteHandle(scope.row.attrId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit"
          :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle"
          @current-change="state.pageCurrentChangeHandle"> </el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update ref="addOrUpdateRef" v-model:tree-list="treeList"
          @refreshDataList="state.getDataList">确定</add-or-update>
      </div>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs, onMounted } from "vue";
import AddOrUpdate from "./attr-add-or-update.vue";
import productService from "@/service/productService";
import CommonTree from "@/components/base-chanris/CommonTree.vue";


const treeList = ref([])
onMounted(() => {
  productService.cateTree().then(({ data }) => {
    treeList.value = data
  })
})

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/product/attr/page",
  getDataListIsPage: true,
  exportURL: "/product/attr/export",
  deleteURL: "/product/attr",
  deleteIsBatchKey: 'attrId',
  dataForm: {
    catelogId: '',
    attrType: 1
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};

const handleNodeClick = (data: any) => {
  state.dataForm.catelogId = "" + data.catId
  state.getDataList()
}
</script>
