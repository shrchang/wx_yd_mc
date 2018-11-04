package com.booking.wechat.persistence.service.order;

import java.util.List;

import com.booking.wechat.persistence.bean.order.Orders;
import com.booking.wechat.persistence.service.base.DAO;

public interface OrdersDao extends DAO<Orders>{

	public List<Orders> selectOrdersByRoomId(Long roomId);
	
	public List<Orders> selectByRoomAdded(Long roomAddedId,String reserveDate,String reserveTime);
	
	public List<Orders> selectByAdmin();
	
	/**
	 * 检查场地在某一个时间段是否已经被预订，返回不为空表示已预订
	 * @param roomId
	 * @param reserveDate
	 * @param reserveTime
	 * @return
	 */
	public List<Orders> checkOrders(Long roomId,String reserveDate,String reserveTime);
	
	/**
	 * 修改订单付款的流水号
	 * @param id
	 * @param swiftNumber
	 * @return
	 */
	@Deprecated
	public boolean updateSwiftNumber(Long id,String swiftNumber);
	
	
	public List<Orders> selectByStatus(String status);
	
	
	public List<Orders> selectByOpenId(String openId);
	
	
	public List<Orders> selectByCard(String cardNumber);
	
	
	public Orders selectByOrderNumber(String orderNumber);
	
	public void updateBusName(Long busId,String busName) ;
	
	public void updateShopName(Long shopId,String shopName) ;
}
