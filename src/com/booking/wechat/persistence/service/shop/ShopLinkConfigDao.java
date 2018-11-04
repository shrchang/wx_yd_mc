package com.booking.wechat.persistence.service.shop;

import java.util.List;

import com.booking.wechat.persistence.bean.shop.ShopLinkConfig;
import com.booking.wechat.persistence.service.base.DAO;

public interface ShopLinkConfigDao extends DAO<ShopLinkConfig>{

	public List<ShopLinkConfig> selectByShopId(Long shopId);
	
	public void deleteByShopId(Long shopId);
}
