package com.booking.wechat.persistence.service.order;

import java.util.List;

import com.booking.wechat.persistence.bean.order.OrderItemsSetMeal;
import com.booking.wechat.persistence.service.base.DAO;


public interface OrderItemsSetMealDao extends DAO<OrderItemsSetMeal> {
	
	/**
	 * 根据订单id去查询
	 * @author shrChang.Liu
	 * @param orderId
	 * @return
	 * @date 2018年10月31日 下午2:02:24
	 * @return List<OrderItemsSetMeal>
	 * @description
	 */
	public List<OrderItemsSetMeal> findByOrderId(Long orderId);

}
