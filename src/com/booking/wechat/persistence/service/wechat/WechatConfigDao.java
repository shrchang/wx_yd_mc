package com.booking.wechat.persistence.service.wechat;

import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.base.DAO;

public interface WechatConfigDao extends DAO<WechatConfig>{

	public WechatConfig selectBySystemCode(String systemCode);
	
	public WechatConfig selectByBusId(Long busId);
	
	public void updateBusName(Long busId,String busName);
}
