import { IHttpResponse, IObject } from "@/types/interface";
import http from "@/utils/http";
import commonService from "./commonService";

/**
 * 商品微服务相关api
 */
export default {
	// 获得分类下所有分组&关联属性
	attrGroupListWithAttr(catelogId: string | number): Promise<IHttpResponse> {
		return new Promise((resolve, reject)=>{
			http({url: `product/attrgroup/${catelogId}/withattr`, method: 'get'})
			.then(resolve)
			.catch((err)=>{
				if(err !== '999') {
					reject(err)
				}
			})
		})
	},
	/**
	 * 根据分类id 获得 属性分组列表，不分页
	 * @param catelogId
	 * @returns
	 */
	attrGroupList(catelogId: string | number): Promise<IHttpResponse> {
		return new Promise((resolve, reject)=>{
			http({url: 'product/attrgroup/page', params: {catelogId} })
			.then(resolve)
			.catch((err)=>{
				if(err !== '-999') {
					reject(err)
				}
			})
		})
	},
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