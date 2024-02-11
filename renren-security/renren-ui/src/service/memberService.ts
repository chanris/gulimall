import { IHttpResponse, IObject } from "@/types/interface";
import http from "@/utils/http";
import commonService from "./commonService";

/**
 * 商品微服务相关api
 */
export default {
	memberLevelList(): Promise<IHttpResponse> {
		return new Promise((resolve, reject)=>{
			http({ url: 'member/memberlevel/page', params: { limit: -1}})
			.then(resolve)
			.catch((err)=>{
				if(err !== '-999') {
					reject(err)
				}
			})
		})	
	},
	...commonService
}