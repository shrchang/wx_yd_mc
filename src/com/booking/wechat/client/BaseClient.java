package com.booking.wechat.client;

import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.base.DaoFactory;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;
import com.booking.wechat.persistence.service.wechat.impl.WechatConfigDaoImpl;

public abstract class BaseClient {

	protected Long busId;
	
	protected WechatConfig config;

	protected BaseClient(Long busId){
		this.busId = busId;
		WechatConfigDao configDao = DaoFactory.getDao(WechatConfigDaoImpl.class);
		config = configDao.selectByBusId(busId);
	}
}
