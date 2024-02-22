<template>
  <div class="mod-product__skuinfo">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item label="分类" prop="catalogId">
        <categoryCascader v-model:catalog-id="state.dataForm.catalogId"></categoryCascader>
      </el-form-item>
      <el-form-item label="品牌" prop="brandId">
        <brand-select v-model:brand-id="state.dataForm.brandId"></brand-select>
      </el-form-item>
      <el-form-item>
        <el-button color="#626aef" @click.stop="state.getDataList()">检索</el-button>
      </el-form-item>
      <el-form-item>
        <el-button color="#626aef" @click.stop=" state.dataForm.catalogId = null; state.dataForm.brandId = null; state.getDataList()">重置</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('product:skuinfo:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('product:skuinfo:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
              <el-table-column prop="skuId" label="skuId" header-align="center" align="center"></el-table-column>
              <el-table-column prop="spuId" label="spuId" header-align="center" align="center"></el-table-column>
              <el-table-column prop="skuName" label="sku名称" header-align="center" align="center"></el-table-column>
              <el-table-column prop="skuDesc" label="sku介绍描述" header-align="center" align="center"></el-table-column>
              <el-table-column prop="catalogId" label="所属分类id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="brandId" label="品牌id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="skuDefaultImg" label="默认图片" header-align="center" align="center">
                <template #default="scope">
                  <el-image :src="scope.row.skuDefaultImg" fit="cover" style="width: 100px; height: 100px;"/>
                </template>
              </el-table-column>
              <el-table-column prop="skuTitle" label="标题" header-align="center" align="center"></el-table-column>
              <el-table-column prop="skuSubtitle" label="副标题" header-align="center" align="center"></el-table-column>
              <el-table-column prop="price" label="价格" header-align="center" align="center"></el-table-column>
              <el-table-column prop="saleCount" label="销量" header-align="center" align="center"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('product:skuinfo:update')" type="primary" link @click="addOrUpdateHandle(scope.row.skuId)">修改</el-button>
          <el-button v-if="state.hasPermission('product:skuinfo:delete')" type="primary" link @click="state.deleteHandle(scope.row.skuId)">删除</el-button>
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
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./skuinfo-add-or-update.vue";
import categoryCascader from "../common/category-cascader.vue";
import brandSelect from "../common/brand-select.vue";
const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/product/skuinfo/page",
  getDataListIsPage: true,
  exportURL: "/product/skuinfo/export",
  deleteURL: "/product/skuinfo",
  dataForm: {
    catalogId: null,
    brandId: null
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
