package com.booking.wechat.persistence.service.menu.impl;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.menu.WechatMenu;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.menu.WechatMenuDao;

@Service
public class WechatMenuDaoImpl extends DaoSupport<WechatMenu> implements WechatMenuDao {

	
	public void updateBusName(Long busId,String busName) {
		batchUpdate("update "+getEntityName(WechatMenu.class)+" set busName='"+busName+"' where busId="+busId);
	}

	public void deleteByBusId(Long busId) {
		batchUpdate("delete from "+this.getEntityName(WechatMenu.class) +" where busId="+busId);
	}
}
