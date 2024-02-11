<template>
  <div class="mod-member__memberlevel">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button v-if="state.hasPermission('member:memberlevel:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('member:memberlevel:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
              <el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="name" label="等级名称" header-align="center" align="center"></el-table-column>
              <el-table-column prop="growthPoint" label="等级需要的成长值" header-align="center" align="center"></el-table-column>
              <el-table-column prop="defaultStatus" label="是否为默认等级" header-align="center" align="center">
				<template #default="scope">
					<el-icon :size="20" :color="'#67C23A'" v-if="scope.row.defaultStatus === 1">
						<CircleCheckFilled/>
					</el-icon>
					<el-icon :size="20" :color="'#F56C6C'" v-else>
						<CircleCloseFilled/>
					</el-icon>
				</template>
			  </el-table-column>
              <el-table-column prop="freeFreightPoint" label="免运费标准" header-align="center" align="center"></el-table-column>
              <el-table-column prop="commentGrowthPoint" label="每次评价获取的成长值" header-align="center" align="center"></el-table-column>
              <el-table-column prop="priviledgeFreeFreight" label="是否有免邮特权" header-align="center" align="center">
				<template #default="scope">
					<el-icon :size="20" :color="'#67C23A'" v-if="scope.row.priviledgeFreeFreight === 1">
						<CircleCheckFilled/>
					</el-icon>
					<el-icon :size="20" :color="'#F56C6C'" v-else>
						<CircleCloseFilled/>
					</el-icon>
				</template>
			  </el-table-column>
              <el-table-column prop="priviledgeMemberPrice" label="是否有会员价格特权" header-align="center" align="center">
				<template #default="scope">
					<el-icon :size="20" :color="'#67C23A'" v-if="scope.row.priviledgeMemberPrice === 1">
						<CircleCheckFilled/>
					</el-icon>
					<el-icon :size="20" :color="'#F56C6C'" v-else>
						<CircleCloseFilled/>
					</el-icon>
				</template>
			  </el-table-column>
              <el-table-column prop="priviledgeBirthday" label="是否有生日特权" header-align="center" align="center">
				<template #default="scope">
					<el-icon :size="20" :color="'#67C23A'" v-if="scope.row.priviledgeBirthday === 1">
						<CircleCheckFilled/>
					</el-icon>
					<el-icon :size="20" :color="'#F56C6C'" v-else>
						<CircleCloseFilled/>
					</el-icon>
				</template>
			  </el-table-column>
              <el-table-column prop="note" label="备注" header-align="center" align="center"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('member:memberlevel:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button v-if="state.hasPermission('member:memberlevel:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
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
import AddOrUpdate from "./level-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/member/memberlevel/page",
  getDataListIsPage: true,
  exportURL: "/member/memberlevel/export",
  deleteURL: "/member/memberlevel"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
