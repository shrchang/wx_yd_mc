package com.booking.wechat.persistence.service.shop.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.shop.ShopLinkConfig;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.shop.ShopLinkConfigDao;

@Service
public class ShopLinkConfigDaoImpl extends DaoSupport<ShopLinkConfig> implements ShopLinkConfigDao{

	
	public List<ShopLinkConfig> selectByShopId(Long shopId) {
		ShopLinkConfig params = new ShopLinkConfig();
		params.setShopId(shopId);
		return findByExample(params,true).getRows();
	}

	
	public void deleteByShopId(Long shopId) {
		batchUpdate("delete from "+this.getEntityName(ShopLinkConfig.class) +" where shopId="+shopId);
	}
}
