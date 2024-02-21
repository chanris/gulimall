<template>
	<el-select
    v-model="brandId"
    placeholder="选择品牌"
    size="large"
    style="width: 240px">
    <el-option
      v-for="item in props.brandList"
      :key="item.brandId"
      :label="item.name"
      :value="item.brandId"
    />
  </el-select>
</template>
<script lang="ts" setup>
const brandId = defineModel('brandId')
const props = defineProps(['brandList'])
</script>
<style lang="less">
</style>