package com.chanris.gulimall.ware.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.ware.dto.PurchaseDTO;
import com.chanris.gulimall.ware.entity.PurchaseEntity;
import com.chanris.gulimall.ware.vo.MergeVo;
import com.chanris.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;

/**
 * 采购信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface PurchaseService extends CrudService<PurchaseEntity, PurchaseDTO> {
    List<PurchaseDTO> getUnreceiveList();

    void mergePurchaseDetail(MergeVo vo);

    void receiveByUserId(List<Long> purchaseIds, Long userId);

    void done(PurchaseDoneVo doneVo);
}