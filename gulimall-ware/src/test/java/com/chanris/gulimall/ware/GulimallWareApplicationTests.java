package com.chanris.gulimall.ware;

import com.chanris.gulimall.ware.dto.PurchaseDTO;
import com.chanris.gulimall.ware.dto.PurchaseDetailDTO;
import com.chanris.gulimall.ware.service.PurchaseDetailService;
import com.chanris.gulimall.ware.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class GulimallWareApplicationTests {

	@Resource
	private PurchaseService purchaseService;

	@Resource
	private PurchaseDetailService purchaseDetailService;

	@Test
	void contextLoads() {
	}

	@Test
	void test01() {
		PurchaseDTO purchaseDTO = new PurchaseDTO();
		purchaseDTO.setId(1L);
		purchaseDTO.setStatus(4);
		purchaseService.update(purchaseDTO);
	}

	@Test
	void test02() {
		List<Long> storageIds = new ArrayList<>(1);
		storageIds.add(1L);
		Map<String, Object> condition = new HashMap<>(1);
		condition.put("storageIds", storageIds);
		List<PurchaseDetailDTO> validDetailDTOs = purchaseDetailService.list(condition);
		System.out.println(validDetailDTOs.size());
	}
}
