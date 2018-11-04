package com.booking.wechat.persistence.service.order;

import java.util.List;

import com.booking.wechat.persistence.bean.order.OrderFreeMoney;
import com.booking.wechat.persistence.service.base.DAO;

public interface OrderFreeMoneyDao extends DAO<OrderFreeMoney>{

	public List<OrderFreeMoney> findByOrderId(Long orderId);
	
	public void deleteByOrderId(Long orderId);
}
