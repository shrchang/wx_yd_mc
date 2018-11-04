package com.booking.wechat.persistence.service.menu;

import com.booking.wechat.persistence.bean.menu.WechatMenu;
import com.booking.wechat.persistence.service.base.DAO;

public interface WechatMenuDao extends DAO<WechatMenu> {

	public void updateBusName(Long busId,String busName);
	
	public void deleteByBusId(Long busId);
}
