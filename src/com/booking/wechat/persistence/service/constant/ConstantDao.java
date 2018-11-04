package com.booking.wechat.persistence.service.constant;

import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.service.base.DAO;

public interface ConstantDao extends DAO<Constant>{

	public Constant findByConstantId(Long busId,String constantId);
	
	public void updateBusName(Long busId,String busName);
}
