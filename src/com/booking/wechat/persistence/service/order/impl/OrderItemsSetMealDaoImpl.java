package com.booking.wechat.persistence.service.order.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.order.OrderItemsSetMeal;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.order.OrderItemsSetMealDao;

@Service
public class OrderItemsSetMealDaoImpl extends DaoSupport<OrderItemsSetMeal> implements OrderItemsSetMealDao {

	public List<OrderItemsSetMeal> findByOrderId(Long orderId) {
		String jpql = "from " + getEntityName(OrderItemsSetMeal.class) + " where orderId=?1";
		return findByCustomJpql(jpql, new Object[]{orderId});
	}
	
}
