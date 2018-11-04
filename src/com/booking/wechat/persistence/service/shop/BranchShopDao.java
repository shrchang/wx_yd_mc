package com.booking.wechat.persistence.service.shop;

import java.util.List;

import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.service.base.DAO;

public interface BranchShopDao extends DAO<BranchShop>{

	public List<BranchShop> findByBusiness(Long busId);
	
	/**
	 * 根据用户角色查询
	 * @return
	 */
	public List<BranchShop> getBranchShopByUser();
	
	public void updateBusName(Long busId,String busName) ;
}
