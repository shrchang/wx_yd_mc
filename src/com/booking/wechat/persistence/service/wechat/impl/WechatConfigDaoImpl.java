package com.booking.wechat.persistence.service.wechat.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;
import com.booking.wechat.util.ListUtils;

@Service
public class WechatConfigDaoImpl  extends DaoSupport<WechatConfig> implements WechatConfigDao{

	
	public WechatConfig selectBySystemCode(String systemCode) {
		WechatConfig param = new WechatConfig();
		param.setSystemCode(systemCode);
		List<WechatConfig> list = findByExample(param,true).getRows(); 
		if(ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	
	public void updateBusName(Long busId, String busName) {
		batchUpdate("update "+getEntityName(WechatConfig.class)+" set busName='"+busName+"' where busId="+busId);
	}

	
	public WechatConfig selectByBusId(Long busId) {
		WechatConfig param = new WechatConfig();
		param.setBusId(busId);
		List<WechatConfig> list = findByExample(param,true).getRows(); 
		if(ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

}
