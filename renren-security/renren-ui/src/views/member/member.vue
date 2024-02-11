<template>
  <div class="mod-member__member">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button v-if="state.hasPermission('member:member:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('member:member:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
              <el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="levelId" label="会员等级id" header-align="center" align="center">
				<template #default=scope>
					{{ memberLevelDisplay(scope.row.levelId) }}
				</template>
			  </el-table-column>
              <el-table-column prop="username" label="用户名" header-align="center" align="center"></el-table-column>
              <el-table-column prop="password" label="密码" header-align="center" align="center"></el-table-column>
              <el-table-column prop="nickname" label="昵称" header-align="center" align="center"></el-table-column>
              <el-table-column prop="mobile" label="手机号码" header-align="center" align="center"></el-table-column>
              <el-table-column prop="email" label="邮箱" header-align="center" align="center"></el-table-column>
              <el-table-column prop="header" label="头像" header-align="center" align="center"></el-table-column>
              <el-table-column prop="gender" label="性别" header-align="center" align="center">
				<template #default="scope">
					{{scope.row.gender === 1 ? '男' : scope.row.gender === 0 ? '女' : '其他'}}
				</template>
			  </el-table-column>
              <el-table-column prop="birth" label="生日" header-align="center" align="center"></el-table-column>
              <el-table-column prop="city" label="所在城市" header-align="center" align="center"></el-table-column>
              <el-table-column prop="job" label="职业" header-align="center" align="center"></el-table-column>
              <el-table-column prop="sign" label="个性签名" header-align="center" align="center"></el-table-column>
              <el-table-column prop="sourceType" label="用户来源" header-align="center" align="center"></el-table-column>
              <el-table-column prop="integration" label="积分" header-align="center" align="center"></el-table-column>
              <el-table-column prop="growth" label="成长值" header-align="center" align="center"></el-table-column>
              <el-table-column prop="status" label="启用状态" header-align="center" align="center">
				<template #default="scope">
					<el-icon :size="20" :color="'#67C23A'" v-if="scope.row.status === 1">
						<CircleCheckFilled/>
					</el-icon>
					<el-icon :size="20" :color="'#F56C6C'" v-else>
						<CircleCloseFilled/>
					</el-icon>
				</template>
			  </el-table-column>
              <el-table-column prop="createTime" label="注册时间" header-align="center" align="center"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('member:member:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button v-if="state.hasPermission('member:member:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
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
import { reactive, ref, toRefs, onMounted } from "vue";
import AddOrUpdate from "./member-add-or-update.vue";
import memberService from "@/service/memberService";

const memberLevelList = ref<any>([])
onMounted(()=>{
	memberService.memberLevelList()
	.then(({data})=>{
		memberLevelList.value = data.list		
	})
})

const memberLevelDisplay = (id: number) => {
	for(let item of memberLevelList.value) {
		if(item.id === id) {
			return item.name
		}
	}
}

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/member/member/page",
  getDataListIsPage: true,
  exportURL: "/member/member/export",
  deleteURL: "/member/member"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
