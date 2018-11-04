package com.booking.wechat.persistence.service.order.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.order.OrderPayItems;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.order.OrderPayItemsDao;
import com.booking.wechat.util.ListUtils;

@Service
public class OrderPayItemsDaoImpl extends DaoSupport<OrderPayItems> implements OrderPayItemsDao{

	public List<OrderPayItems> findByOrder(Long orderId) {
		String sql = "from "+getEntityName(OrderPayItems.class) +" where orderId=?1";
		List<OrderPayItems> list = findByCustomJpql(sql, new Object[]{orderId});
		return list;
	}

	public OrderPayItems findByOrder(Long orderId, String payType) {
		String sql = "from "+getEntityName(OrderPayItems.class) +" where orderId=?1 and payType=?2";
		List<OrderPayItems> list = findByCustomJpql(sql, new Object[]{orderId,payType});
		if(ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		OrderPayItems payItem = new OrderPayItems();
		payItem.setOrderId(orderId);
		payItem.setPayType(payType);
		return payItem;
	}

}
