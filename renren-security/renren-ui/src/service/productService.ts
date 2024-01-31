import { IHttpResponse, IObject } from "@/types/interface";
import http from "@/utils/http";
import commonService from "./commonService";

/**
 * 商品微服务相关api
 */
export default {
	cateTree(): Promise<IHttpResponse> {
		return http({
			url: '/product/category/list/tree',
			method: 'GET'
		})
	},
	...commonService
}