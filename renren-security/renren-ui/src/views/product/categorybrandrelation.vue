<template>
  <el-dialog v-model="visible" title="关联分类" :close-on-click-modal="false"
    :close-on-press-escape="false">
    <div class="mod-product__categorybrandrelation">
      <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
        <el-form-item>
          <el-button v-if="state.hasPermission('product:categorybrandrelation:save')" type="primary"
            @click="addOrUpdateHandle()">新增</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="state.hasPermission('product:categorybrandrelation:delete')" type="danger"
            @click="state.deleteHandle()">删除</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="state.dataListLoading" :data="state.dataList" border
        @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="brandId" label="品牌id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="catelogId" label="分类id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="brandName" label="品牌名称" header-align="center" align="center"></el-table-column>
        <el-table-column prop="catelogName" label="分类名称" header-align="center" align="center"></el-table-column>
        <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
          <template v-slot="scope">
            <!-- <el-button v-if="state.hasPermission('product:categorybrandrelation:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">修改</el-button> -->
            <el-button v-if="state.hasPermission('product:categorybrandrelation:delete')" type="danger" link
              @click="state.deleteHandle(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit"
        :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle"
        @current-change="state.pageCurrentChangeHandle"> </el-pagination>
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList">确定</add-or-update>
    </div>
  </el-dialog>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./categorybrandrelation-add-or-update.vue";
const visible = ref(false)

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/product/categorybrandrelation/page",
  getDataListIsPage: true,
  exportURL: "/product/categorybrandrelation/export",
  deleteURL: "/product/categorybrandrelation",
  dataForm: {
    brandId: ''
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};

const init = (brandId: number) => {
  visible.value = true
  state.dataForm.brandId = '' + brandId
  state.getDataList()
}

defineExpose({
  init
});
</script>
