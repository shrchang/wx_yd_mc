package com.booking.wechat.persistence.service.order.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.order.OrderItems;
import com.booking.wechat.persistence.bean.order.Orders;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.order.OrderItemsDao;

@Service
public class OrderItemsDaoImpl extends DaoSupport<OrderItems> implements OrderItemsDao{

	
	public List<OrderItems> findByOrder(Long orderId) {
		String sql = "from "+getEntityName(OrderItems.class) +" where orderId=?1";
		List<OrderItems> list = findByCustomJpql(sql, new Object[]{orderId});
		return list;
	}

	
	public List<OrderItems> selectByBusId(Long busId, Date startDate, Date endDate) {
		String sql = "from "+getEntityName(OrderItems.class)+" oi where oi.orderId in (select o.id from "+getEntityName(Orders.class)+" o where o.status<>'CANCELLED' and o.status<>'DELETED' and o.busId=?1 and oi.reserveDate>=?2 and oi.reserveDate<=?3)";
		List<OrderItems> list = super.findByCustomJpql(sql,new Object[]{busId,startDate,endDate});
		return list;
	}


	public List<OrderItems> selectAfterTodayItemsByRoomId(Long roomId) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date startDate = cal.getTime();
		String hql = "from " + getEntityName(OrderItems.class) + " oi where oi.orderId in (select o.id from "
				+ getEntityName(Orders.class)
				+ " o where o.status<>'CANCELLED' and o.status<>'DELETED') and "
				+ "oi.roomId=?1 and oi.reserveDate>=?2";
		List<OrderItems> list = super.findByCustomJpql(hql,new Object[]{roomId,startDate});
		return list;
	}
}
