package com.booking.wechat.controller.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.vo.OrderParamVO;
import com.booking.wechat.persistence.bean.bus.Business;
import com.booking.wechat.persistence.bean.coupon.CouponActivity;
import com.booking.wechat.persistence.bean.order.OrderFreeMoney;
import com.booking.wechat.persistence.bean.order.OrderItems;
import com.booking.wechat.persistence.bean.order.OrderPayItems;
import com.booking.wechat.persistence.bean.order.Orders;
import com.booking.wechat.persistence.bean.room.Room;
import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.coupon.CouponActivityDao;
import com.booking.wechat.persistence.service.order.OrderFreeMoneyDao;
import com.booking.wechat.persistence.service.order.OrderItemsDao;
import com.booking.wechat.persistence.service.order.OrderPayItemsDao;
import com.booking.wechat.persistence.service.order.OrdersDao;
import com.booking.wechat.persistence.service.room.RoomDao;
import com.booking.wechat.persistence.service.shop.BranchShopDao;
import com.booking.wechat.util.DateUtils;
import com.booking.wechat.util.ListUtils;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/system/orders")
public class OrdersController extends BaseController{

	@Autowired
	private BusinessDao busDao;
	
	@Autowired
	private BranchShopDao shopDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private OrdersDao orderDao;
	
	@Autowired
	private OrderPayItemsDao payItemDao;
	
	@Autowired
	private OrderItemsDao itemDao;
	
	@Autowired
	private OrderFreeMoneyDao freeMoneyDao;
	
	@Autowired
	private CouponActivityDao couponDao;
	
	@RequestMapping("/list")
	public String getAllList(OrderParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		Orders params = new Orders();
		
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		List<Business> bus = busDao.getBusinessByUser();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			params.setBusId(user.getBusId());
		}
		
		if(StringUtil.isNotEmpty(param.getSearchKey())){
			params.setUserName(param.getSearchKey());
		}
		
		if(StringUtil.isNotEmpty(param.getBookingDates())){
			params.setBookingDates(param.getBookingDates());
		}
		
		if(null != param.getBusId() && param.getBusId()>0){
			params.setBusId(param.getBusId());
		}
		if(null != param.getRoomId() && param.getRoomId()>0){
			Room roomParam = roomDao.find(param.getRoomId());
			params.setRoomNames(roomParam.getRoomName());
		}
		List<Room> rooms = new ArrayList<Room>();
		if(null != param.getShopId() && param.getShopId()>0){
			params.setShopId(param.getShopId());
			rooms = roomDao.findRoomByShop(param.getShopId());
		}
		
		
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		if(StringUtil.isNotEmpty(param.getOrderField())){
			orderby.put(param.getOrderField(), param.getOrderDirection());
		}else{
			orderby.put("orderDate", "desc");
		}
		QueryResult<Orders> list = orderDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage(),orderby);
		model.put("bus", bus);
		
		List<BranchShop> shops = shopDao.getBranchShopByUser();
		model.put("shops", shops);
		
		model.put("rooms", rooms);
		
		model.put("list",list);
		
		model.put("pageInfo",param);
		
		return "system/order/list";
	}
	
	@RequestMapping("/{id}")
	public String getOrderInfo(@PathVariable Long id, Map<String, Object> model){
		Orders order = orderDao.find(id);
		model.put("order",order);
		List<OrderPayItems> payItems = payItemDao.findByOrder(id);
		model.put("payItems",payItems);
		List<OrderItems> items = itemDao.findByOrder(id);
		model.put("roomItems",items);
		return "system/order/info";
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long id){
		Orders order = orderDao.find(id);
		order.setStatus(Orders.DELETED);
		orderDao.update(order);
		return ajaxDoneSuccess("删除订单信息成功");
	}
	
	@RequestMapping("/updaterBookingDates")
	public void updaterBookingDates(){
		List<Orders> orderList = orderDao.getAll();
		for (Orders orders : orderList) {
			List<OrderItems> items = itemDao.findByOrder(orders.getId());
			List<String> bookingDates = new ArrayList<String>();
			for (OrderItems orderItems : items) {
				String temp = DateUtils.date2String(orderItems.getReserveDate(), DateUtils.PATTERN_DATE);
				if(!bookingDates.contains(temp)){
					bookingDates.add(temp);
				}
			}
			orders.setBookingDates(ListUtils.convertToString(bookingDates, ","));
			orderDao.update(orders);
		}
	}
	
	@RequestMapping("/updateOrderRoomNames")
	public void updateOrderRoomNames(){
		List<Orders> orderList = orderDao.getAll();
		for (Orders orders : orderList) {
			List<OrderItems> items = itemDao.findByOrder(orders.getId());
			List<String> roomNames = new ArrayList<String>();
			for (OrderItems orderItems : items) {
				String temp = orderItems.getRoomName();
				if(!roomNames.contains(temp)){
					roomNames.add(temp);
				}
			}
			orders.setRoomNames(ListUtils.convertToString(roomNames, ","));
			orderDao.update(orders);
		}
	}
	
	/**
	 * 输入付款流水号后，确认订单收款
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/affirmOrder",method=RequestMethod.POST)
	public ModelAndView affirmOrder(@PathVariable Long id){
		Orders order = orderDao.find(id);
		order.setStatus(Orders.PAID);
		orderDao.update(order);
		
		OrderPayItems payItem = payItemDao.findByOrder(id, OrderPayItems.TYPE_BOOKING);
		payItem.setStatus(OrderPayItems.STATUS_PAID);
		payItemDao.update(payItem);
		return ajaxDoneSuccess("确认收款成功");
	}
	
	/**
	 * 取消订单
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/cancelOrder",method=RequestMethod.POST)
	public ModelAndView cancelOrder(@PathVariable Long id){
		Orders order = orderDao.find(id);
		order.setStatus(Orders.CANCELLED);
		orderDao.update(order);
		return ajaxDoneSuccess("订单取消成功");
	}
	
	@RequestMapping(value="/{id}/editPrice",method=RequestMethod.GET)
	public ModelAndView editPrice(@PathVariable Long id){
		Orders order = orderDao.find(id);
		ModelAndView model = new ModelAndView("system/order/editOrderPrice");
		model.addObject("order", order);
		
		List<OrderFreeMoney> frees = freeMoneyDao.findByOrderId(id);
		//查询免费金额的优惠
		List<OrderItems> items = itemDao.findByOrder(id);
		List<CouponActivity> coupons = couponDao.findByUser(CouponActivity.CASH_TYPE,"OK",items.get(0).getReserveDate());
		List<Map<String,Object>> couponList = new ArrayList<Map<String,Object>>();
		for (CouponActivity couponActivity : coupons) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", couponActivity.getId());
			map.put("freeMoney", couponActivity.getFreeMoney());
			map.put("couponName", couponActivity.getCouponName());
			map.put("checked", "false");
			for (OrderFreeMoney free : frees) {
				if(StringUtil.equals(couponActivity.getId(), free.getCouponId())){
					map.put("checked", "true");
				}
			}
			couponList.add(map);
		}
		model.addObject("coupons", couponList);
		return model;
	}
	
	/**
	 * 付全款款后，确认订单
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/paidOrder",method=RequestMethod.POST)
	public ModelAndView paidOrder(@PathVariable Long id) throws IOException{
		Orders order = orderDao.find(id);
		order.setStatus(Orders.FINISH);
		orderDao.update(order);
		
		OrderPayItems payItem = payItemDao.findByOrder(id, OrderPayItems.TYPE_FINAL);
		payItem.setStatus(OrderPayItems.STATUS_PAID);
		payItemDao.update(payItem);
		return ajaxDoneSuccess("确认订单成功");
	}
	
	/**
	 * 修改订单价格
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/updateOrderPrice",method=RequestMethod.POST)
	public ModelAndView updateOrderPrice(@PathVariable Long id,Orders updateBean,String[] coupon) throws IOException{
		Orders order = orderDao.find(id);
		order.setRoomPrice(updateBean.getRoomPrice());
		order.setOtherPrice(updateBean.getOtherPrice());
		order.setOrderDesc(updateBean.getOrderDesc());
		orderDao.update(order);
		
		freeMoneyDao.deleteByOrderId(id);
		if(null != coupon){
			for (String couponId : coupon) {
				OrderFreeMoney bean = new OrderFreeMoney();
				bean.setCouponId(Long.valueOf(couponId));
				bean.setOrderId(id);
				freeMoneyDao.save(bean);
			}
		}
		return ajaxDoneSuccess("订单价格修改成功");
	}
}
