<template>
	<el-upload
    v-model:file-list="fileList"
    class="upload-demo"
    :action="dataObj.host"
	:data="dataObj"
	accept=".png,.jpeg"
	:before-upload="handlebeforeUpload">
		<el-button type="primary">上传图片</el-button>
		<template #tip>
			<div class="el-upload__tip">jpg/png文件不能超过10MB大小</div>
		</template>
  </el-upload>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
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

async function getPolicy(fileName: string) {
	return new Promise((resolve, reject)=>{
		commonService.get('/thirdparty/oss/policy')
		.then(({data})=>{
			if(new Date().getTime() <= parseInt(data.expire) * 1000) {
				dataObj.value.OSSAccessKeyId = data.accessid
				dataObj.value.host = data.host
				dataObj.value.policy = data.policy
				dataObj.value.signature = data.signature
				dataObj.value.key = data.dir + fileName
				resolve(true)
			}else {
				reject('过期的签名')
			}
		})
	})
}

const handlebeforeUpload: UploadProps['beforeUpload'] = async (rawFile) => {
	if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
		ElMessage.error('上次文件必须为JPG或PNG格式')
		return false
	} else if (rawFile.size / 1024 / 1024 > 10) {
		ElMessage.error('jpg/png文件不超过10MB')
		return false
	}

	// 请求服务端OSS签名
	try{
		// await关键字：等待异步函数执行完毕才往下执行
		const result = await getPolicy(rawFile.name)
		if(result) return true
	}catch(err) {
		console.error('获取服务端OSS签名失败:', err)
	}
	return false
}


</script>
<style lang="">
	
</style>