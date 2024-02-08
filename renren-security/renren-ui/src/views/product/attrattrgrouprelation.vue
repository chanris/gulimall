<template>
  <el-dialog v-model="visible" title="关联属性" :close-on-click-modal="false" :close-on-press-escape="false">
    <div class="mod-product__attrattrgrouprelation">
      <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
        <el-form-item>
          <el-button v-if="state.hasPermission('product:attrattrgrouprelation:save')" type="primary"
            @click="addOrUpdateHandle()">新增</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="state.hasPermission('product:attrattrgrouprelation:delete')" type="danger"
            @click="state.deleteHandle()">删除</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="state.dataListLoading" :data="state.dataList" border
        @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="attrId" label="属性id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="attrGroupId" label="属性分组id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="attrSort" label="属性组内排序" header-align="center" align="center"></el-table-column>
        <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
          <template v-slot="scope">
            <el-button v-if="state.hasPermission('product:attrattrgrouprelation:update')" type="primary" link
              @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button v-if="state.hasPermission('product:attrattrgrouprelation:delete')" type="primary" link
              @click="state.deleteHandle(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit"
        :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle"
        @current-change="state.pageCurrentChangeHandle"></el-pagination>
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update ref="addOrUpdateRef"  @refreshDataList="state.getDataList">确定</add-or-update>
    </div>
  </el-dialog>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./attrattrgrouprelation-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/product/attrattrgrouprelation/page",
  getDataListIsPage: true,
  exportURL: "/product/attrattrgrouprelation/export",
  deleteURL: "/product/attrattrgrouprelation",
  dataForm: {
    attrGroupId: ''
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};

const visible = ref(false)

const init = (attrGroupId: number) => {
  visible.value = true
  state.dataForm.attrGroupId = '' + attrGroupId
  state.getDataList()
}

defineExpose({
  init
})
</script>
