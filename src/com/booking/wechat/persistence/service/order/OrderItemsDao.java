package com.booking.wechat.persistence.service.order;

import java.util.Date;
import java.util.List;

import com.booking.wechat.persistence.bean.order.OrderItems;
import com.booking.wechat.persistence.service.base.DAO;

public interface OrderItemsDao extends DAO<OrderItems>{

	public List<OrderItems> findByOrder(Long orderId);
	
	/**
	 * 查询订单，不包含已取消、已删除的订单
	 * @param busId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<OrderItems> selectByBusId(Long busId,Date startDate,Date endDate);
	
	/**
	 * 查询这个场地今天与今天之后已经预定的订单
	 * @author shrChang.Liu
	 * @param roomId
	 * @return
	 * @date 2018年10月30日 上午10:05:37
	 * @return List<OrderItems>
	 * @description
	 */
	public List<OrderItems> selectAfterTodayItemsByRoomId(Long roomId);
	
}
