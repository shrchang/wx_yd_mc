package com.booking.wechat.task;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.booking.wechat.client.kf.CustomerServiceClient;
import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.bean.order.OrderItems;
import com.booking.wechat.persistence.bean.order.Orders;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.persistence.service.order.OrderItemsDao;
import com.booking.wechat.persistence.service.order.OrdersDao;
import com.booking.wechat.util.ConstantKey;
import com.booking.wechat.util.DateUtils;

/**
 * 每一分钟检查订单的支付状态，在30分钟之内没有支付的订单自动取消
 * @author Luoxx
 */
public class OrderPayStatusTask {

	@Autowired
	private OrdersDao orderDao;
	
	@Autowired
	private ConstantDao constantDao;
	//订单超时时间
	public static int outTime = 0;
	
	public int getOutTime(Long busId) {
		if(outTime==0){
			Constant constant = constantDao.findByConstantId(busId,ConstantKey.ORDER_OUT_TIME);
			outTime = Integer.valueOf(constant.getConstantValue());
		}
		return outTime;
	}
	@Autowired
	private OrderItemsDao itemsDao;
	
	@Scheduled(cron="0 */1 * * * ?")
	public void orderListener(){
		List<Orders> orders = orderDao.selectByStatus(Orders.UNPAID);
		for (Orders order : orders) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(order.getOrderDate());
			//订单时间上加30分钟
			cal.add(Calendar.MINUTE, getOutTime(order.getBusId()));
			if(cal.getTimeInMillis()<System.currentTimeMillis()){
				order.setStatus(Orders.CANCELLED);
				orderDao.update(order);
				try {
					String info = String.format("您的订单：%s在%s分钟之内没有支付定金%s元，系统已经自动取消订单！", 
							getRoomName(order.getId()),getOutTime(order.getBusId()),order.getOrderMoney());
					CustomerServiceClient client = new CustomerServiceClient(order.getBusId());
					client.sendMessage(order.getUserToken(),info);
				} catch (Exception e) {
				}
			}
		}
	}
	
	private String getRoomName(Long orderId){
		List<OrderItems> orderItems = itemsDao.findByOrder(orderId);
		String msg = "";
		for (OrderItems item : orderItems) {
			msg += "[场地："+item.getRoomName()+"，时间："+DateUtils.date2String(item.getReserveDate(), DateUtils.PATTERN_DATE)+" "+item.getReserveTime()+"]\n";
		}
		return msg;
	}
}
