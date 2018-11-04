package com.booking.wechat.persistence.service.order;

import java.util.List;

import com.booking.wechat.persistence.bean.order.OrderPayItems;
import com.booking.wechat.persistence.service.base.DAO;

public interface OrderPayItemsDao extends DAO<OrderPayItems>{

	public List<OrderPayItems> findByOrder(Long orderId);
	
	public OrderPayItems findByOrder(Long orderId,String payType);
}
