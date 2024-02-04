<template>
	<el-upload
    v-model:file-list="fileList"
    class="upload-demo"
    :action="dataObj.host"
	:data="getData"
	list-type="picture"
	accept=".png,.jpeg,.jpg"
	:auto-upload="true"
	:before-upload="handlebeforeUpload">
		<el-button type="primary">上传图片</el-button>
		<template #tip>
			<div class="el-upload__tip">jpg/png文件不能超过10MB大小</div>
		</template>
  </el-upload>
</template>
<script lang="ts" setup>
/**
 * Element-Plus 问题记录
 * 描述：众所周知，el-upload data属性是用来绑定 formdata请求的参数，:data="dataObj"为单向绑定，
 * 但在before-upload钩子中改变dataObj的值后，无法动态更新到实际的请求参数中，导致第一次请求参数还是默认值，报400 bad request。
 * 但按照vue的逻辑在before-upload中改变dataObj的属性值，会更新到绑定的组件中，但el-upload没有生效，不知是什么原因？
 * 看了文档之后，data属性可以绑定一个返回promise对象的函数，经过调试得，绑定data的function，是在upload-before之后的，这样做就不会报400 bad request。
 * 目前就只能使用函数绑定data属性，绑定vue响应对象会出错-_-
 * 
 */
import { getUuid } from '@/utils/utils';
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { UploadProps, UploadUserFile } from 'element-plus'
import commonService from '@/service/commonService';

const fileList = ref<UploadUserFile[]>([])
const dataObj = ref({
	OSSAccessKeyId: '',
	key: '', // 代表上传到bucket内的object的完整路径，例如 exampledir/exampleobj.txt
	host: '',
	policy: '',
	success_action_status: '200',
	signature: ''
})
const handlebeforeUpload: UploadProps['beforeUpload'] = (rawFile) => {
	// console.log('upload 调用 beforeUpload')
	if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
		ElMessage.error('上次文件必须为JPG或PNG格式')
		return false
	} else if (rawFile.size / 1024 / 1024 > 10) {
		ElMessage.error('jpg/png文件不超过10MB')
		return false
	}

	// 请求服务端OSS签名
	return new Promise((resolve, reject) => {
		commonService.get('/thirdparty/oss/policy')
		.then(({data})=>{
			if(new Date().getTime() <= parseInt(data.expire) * 1000) {
				dataObj.value.OSSAccessKeyId = data.accessid
				dataObj.value.host = data.host
				dataObj.value.policy = data.policy
				dataObj.value.signature = data.signature
				dataObj.value.key = data.dir + getUuid().replaceAll('-', '') + rawFile.name.substring(rawFile.name.lastIndexOf('.'))
				resolve(true)
			}else {
				ElMessage.error('过期的签名')
				resolve(false)
			}
		}).catch(error=>{
			ElMessage.error('获得OSS签名失败')
			reject(error)
		})
	})
}

const getData = () => {
	return new Promise((resolve, reject)=>{
		// console.log('upload 调用getData: dataObj:', dataObj.value)
		resolve(dataObj.value)
	})
}

</script>
<style lang="">
	
</style>