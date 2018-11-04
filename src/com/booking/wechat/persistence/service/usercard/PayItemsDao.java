package com.booking.wechat.persistence.service.usercard;

import java.util.List;

import com.booking.wechat.persistence.bean.usercard.PayItems;
import com.booking.wechat.persistence.service.base.DAO;

public interface PayItemsDao extends DAO<PayItems>{

	public List<PayItems> getPayItemsByUser();
	
	public void updateBusName(Long busId,String busName);
}
