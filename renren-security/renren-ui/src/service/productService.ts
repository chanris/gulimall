import { IHttpResponse, IObject } from "@/types/interface";
import http from "@/utils/http";
import commonService from "./commonService";

/**
 * 商品微服务相关api
 */
export default {
	cateTree(): Promise<IHttpResponse> {
		return new Promise((resolve, reject)=>{
			http({url: '/product/category/list/tree'})
			.then(resolve)
			.catch((err)=>{
				if(err !== '-999') {
					reject(err)
				}
			})
		})
	},
	saveCate(params: IObject): Promise<IHttpResponse> {
		return new Promise((resolve, reject)=>{
			http({
				url: '/product/category',
				method: 'POST',
				data: params
			}).then(resolve).catch(
				(err)=>{
					if(err !== '-999') {
						reject(err)
					}
				}
			)
		})
	},
	/**
	 * 批量删掉分类
	 * @param params number[]
	 * @returns 
	 */
	deleteCate(params: IObject): Promise<IHttpResponse> {
		return new Promise((resolve, reject)=>{
			http({
				url: '/product/category',
				method: 'DELETE',
				data: params
			}).then(resolve).catch(
				(err)=>{
					if(err !== '-999') {
						reject(err)
					}
				}
			)
		})
	},
	/**
	 * 更新分类
	 * @param params Category
	 * @returns 
	 */
	updateCate(params: IObject): Promise<IHttpResponse> {
		return new Promise((resolve, reject)=>{
			http({
				url: '/product/category',
				method: 'PUT',
				data: params
			}).then(resolve).catch(
				(err)=>{
					if(err !== '-999') {
						reject(err)
					}
				}
			)
		})
	},
	...commonService
}