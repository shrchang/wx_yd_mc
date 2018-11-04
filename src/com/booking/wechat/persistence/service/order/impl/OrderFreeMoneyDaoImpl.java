package com.booking.wechat.persistence.service.order.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.order.OrderFreeMoney;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.order.OrderFreeMoneyDao;

@Service
public class OrderFreeMoneyDaoImpl  extends DaoSupport<OrderFreeMoney> implements OrderFreeMoneyDao{

	public List<OrderFreeMoney> findByOrderId(Long orderId) {
		String sql = "from "+getEntityName(OrderFreeMoney.class) +" where orderId=?1";
		List<OrderFreeMoney> list = findByCustomJpql(sql,new Object[]{orderId});
		return list;
	}

	public void deleteByOrderId(Long orderId) {
		batchUpdate("delete from "+this.getEntityName(OrderFreeMoney.class) +" where orderId="+orderId);
	}

}
