package com.booking.wechat.controller.protal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.booking.wechat.client.model.UserBean;
import com.booking.wechat.client.model.pay.UnifiedorderBean;
import com.booking.wechat.client.pay.TenPayClient;
import com.booking.wechat.client.pay.TenPaySign;
import com.booking.wechat.client.pay.TenPayUtils;
import com.booking.wechat.client.user.UserClient;
import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.bean.coupon.CouponActivity;
import com.booking.wechat.persistence.bean.order.OrderItems;
import com.booking.wechat.persistence.bean.order.OrderPayItems;
import com.booking.wechat.persistence.bean.order.Orders;
import com.booking.wechat.persistence.bean.room.Room;
import com.booking.wechat.persistence.bean.room.RoomBookingRangeConfig;
import com.booking.wechat.persistence.bean.usercard.CardChangeRecords;
import com.booking.wechat.persistence.bean.usercard.UserCard;
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.persistence.service.coupon.CouponActivityDao;
import com.booking.wechat.persistence.service.order.OrderItemsDao;
import com.booking.wechat.persistence.service.order.OrderPayItemsDao;
import com.booking.wechat.persistence.service.order.OrdersDao;
import com.booking.wechat.persistence.service.room.RoomBookingRangeConfigDao;
import com.booking.wechat.persistence.service.room.RoomDao;
import com.booking.wechat.persistence.service.usercard.CardChangeRecordsDao;
import com.booking.wechat.persistence.service.usercard.UserCardDao;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;
import com.booking.wechat.util.DateUtils;
import com.booking.wechat.util.ListUtils;
import com.booking.wechat.util.NumberUtil;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/protal/orders")
public class WCOrdersController {

	@Autowired
	private WechatConfigDao configDao;
	
	@Autowired
	private OrdersDao orderDao;
	
	@Autowired
	private OrderPayItemsDao payItemDao;
	
	@Autowired
	private CardChangeRecordsDao recordsDao;
	
	@Autowired
	private OrderItemsDao orderItemDao;
	
	@Autowired
	private UserCardDao cardDao;
	
	@Autowired
	private ConstantDao constantDao;
	
	@Autowired
	private CouponActivityDao couponDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private RoomBookingRangeConfigDao roomConfigDao;
	
	@RequestMapping("/{systemCode}")
	public String getWcOrders(@PathVariable String systemCode,String code,Model model){
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute("systemCode", systemCode);
		UserBean user = (UserBean) session.getAttribute("WC_USER");
		List<Orders> orders = null;
		if(StringUtil.isNotEmpty(code) && null == user){
			WechatConfig config = configDao.selectBySystemCode(systemCode);
			UserClient client = new UserClient(config.getBusId());
			user = client.getUserInfoByAuthorize(code);
			session.setAttribute("WC_USER", user);
			
		}
		orders = orderDao.selectByOpenId(user.getOpenid());
		for (Orders order : orders) {
			List<OrderItems> orderItems = orderItemDao.findByOrder(order.getId());
			order.setOrderItems(orderItems);
		}
		
		//TODO 代付订单
		model.addAttribute("orders", orders);
		return "wap/orders";
	}
	
	/**
	 * 取消订单
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{orderId}/cancelOrder",method=RequestMethod.PUT)
	public @ResponseBody JSONObject cancelOrder(@PathVariable Long orderId) {
		Orders order = orderDao.find(orderId);
		order.setStatus(Orders.CANCELLED);
		orderDao.update(order);
		JSONObject result = new JSONObject();
		result.put("state", "OK");
		return result;
	}
	
	/**
	 * 会员卡支付订单
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/{orderId}/cardPayOrder",method=RequestMethod.PUT)
	public @ResponseBody JSONObject cardPayOrder(@PathVariable Long orderId) {
		Orders order = orderDao.find(orderId);
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean) session.getAttribute("WC_USER");
		List<UserCard> cards = cardDao.selectByUserId(user.getOpenid());
		JSONObject result = new JSONObject();
		if(ListUtils.isEmpty(cards)){
			result.put("state", "NOT_FOUND_CARD");
			result.put("errmsg", "没有找到会员卡信息");
			return result;
		}
		UserCard card = cards.get(0);
		if(UserCard.STATUS_AFFIRM.equals(card.getStatus())){
			result.put("state", "NOT_AVAILABLE_CARD");
			result.put("errmsg", "会员卡等待审核中，请联系客服进行充值，多充多送。");
			return result;
		}
		BigDecimal payMoney = order.getOrderMoney();
		String payType = OrderPayItems.TYPE_BOOKING;
		if(Orders.PAID.equals(order.getStatus())){
			payMoney = NumberUtil.numberSubtract(order.getRoomPrice(), order.getOrderMoney(), 2);
			payMoney = NumberUtil.add(payMoney, order.getOtherPrice()==null?new BigDecimal(0):order.getOtherPrice(), 2);
			payType = OrderPayItems.TYPE_FINAL;
		}
		int flag = card.getRemainingSum().compareTo(payMoney);
		if(flag<0){
			result.put("state", "NOT_ENOUGH_CARD");
			result.put("errmsg", "会员卡余额不足，请充值！");
			return result;
		}
		card.setRemainingSum(card.getRemainingSum().subtract(payMoney));
		card.setLastPayTime(new Date());
		cardDao.update(card);
		saveCardChangeRecords(payMoney, CardChangeRecords.TYPE_CONSUME, card);
		saveOrderPayItem(order,payType,OrderPayItems.MODE_CARD);
		if(Orders.PAID.equals(order.getStatus())){
			order.setStatus(Orders.FINISH);
		}else{
			order.setStatus(Orders.PAID);
		}
		orderDao.update(order);
		result.put("state", "OK");
		return result;
	}
	
	
	@RequestMapping(value="/{orderId}/otherPayOrder",method=RequestMethod.PUT)
	public @ResponseBody JSONObject otherPayOrder(@PathVariable Long orderId,String swiftNumber) throws UnsupportedEncodingException{
		JSONObject result = new JSONObject();
		Orders order = orderDao.find(orderId);
		if(null == order){
			result.put("state", "NOT_FOUND_ORDER");
			result.put("errmsg", "没有找到订单信息");
			return result;
		}
		order.setStatus(Orders.AFFIRM);
		orderDao.update(order);
		
		OrderPayItems item = null;
		if(Orders.PAID.equals(order.getStatus())){
			item = payItemDao.findByOrder(order.getId(), OrderPayItems.TYPE_FINAL);
		}else{
			item = payItemDao.findByOrder(order.getId(), OrderPayItems.TYPE_BOOKING);
		}
		swiftNumber = java.net.URLDecoder.decode(swiftNumber,"UTF-8");
		item.setSwiftNumber(swiftNumber);
		item.setPayMode(OrderPayItems.MODE_ALIPAY);
		item.setStatus(OrderPayItems.STATUS_AFFIRM);
		payItemDao.update(item);
		
		result.put("state", "OK");
		return result;
	}
	
	@RequestMapping(value="/{orderId}/otherPayPage",method=RequestMethod.GET)
	public String otherPayPage(@PathVariable Long orderId,Model model){
		Orders order = orderDao.find(orderId);
		
		WechatConfig config = configDao.selectByBusId(order.getBusId());
		model.addAttribute("systemCode", config.getSystemCode());
		model.addAttribute("order", order);
		Constant alipayAccount = constantDao.findByConstantId(order.getBusId(),"ALIPAY_ACCOUNT");
		Constant alipayName = constantDao.findByConstantId(order.getBusId(),"ALIPAY_NAME");
		Constant orderTimeout = constantDao.findByConstantId(order.getBusId(), "ORDER_OUT_TIME");
		model.addAttribute("alipayAccount",alipayAccount.getConstantValue());
		model.addAttribute("alipayName",alipayName.getConstantValue());
		model.addAttribute("timeOut",orderTimeout.getConstantValue());
		return "wap/alipayOrder";
	}
	
	
	@RequestMapping(value="/{orderId}/otherCardPayOrder",method=RequestMethod.PUT)
	public @ResponseBody JSONObject otherCardPayOrder(@PathVariable Long orderId,String cardNumber) throws UnsupportedEncodingException{
		JSONObject result = new JSONObject();
		Orders order = orderDao.find(orderId);
		if(null == order){
			result.put("state", "NOT_FOUND_ORDER");
			result.put("errmsg", "没有找到订单信息");
			return result;
		}
		cardNumber = java.net.URLDecoder.decode(cardNumber,"UTF-8");
		UserCard card = cardDao.selectByCardNumber(cardNumber);
		result.put("errmsg", "预订成功，等待代付会员支付该订单。");
		if(card==null){
			result.put("state", "NOT_FOUND_CARD");
			result.put("errmsg", "没有找到会员卡信息，请确认卡号是否正确。");
			return result;
		}
		if(UserCard.STATUS_AFFIRM.equals(card.getStatus())){
			result.put("state", "NOT_AVAILABLE_CARD");
			result.put("errmsg", "会员卡等待审核中，请联系客服进行充值，多充多送。");
			return result;
		}
		OrderPayItems item = null;
		BigDecimal money = new BigDecimal(0);
		if(Orders.PAID.equals(order.getStatus())){
			item = payItemDao.findByOrder(order.getId(), OrderPayItems.TYPE_FINAL);
			money = order.getRoomPrice().subtract(order.getOrderMoney());
			if(order.getOtherPrice()!=null){
				money = money.add(order.getOtherPrice());
			}
		}else{
			item = payItemDao.findByOrder(order.getId(), OrderPayItems.TYPE_BOOKING);
			money = order.getOrderMoney();
		}
		int flag = card.getRemainingSum().compareTo(money);
		if(flag<0){
			result.put("state", "NOT_ENOUGH_CARD");
			result.put("errmsg", "会员卡余额不足，请充值！");
			return result;
		}
		item.setPayForAnotherId(card.getUserId());
		item.setPayForAnotherName(card.getUserName());
		item.setUserCardNumber(card.getCardNumber());
		item.setPayMode(OrderPayItems.MODE_REPLACE);
		item.setStatus(OrderPayItems.STATUS_AFFIRM);
		payItemDao.update(item);
		
		result.put("state", "OK");
		return result;
	} 
	
	/**
	 * 变更记录
	 * @param changeMoney
	 * @param changeType
	 * @param userCard
	 * @param roomId
	 */
	private void saveCardChangeRecords(BigDecimal changeMoney,String changeType,UserCard userCard){
		int flag = changeMoney.compareTo(BigDecimal.ZERO);
		if(flag==0 || flag==-1){
			return;
		}
		CardChangeRecords record = new CardChangeRecords();
		record.setChangeDate(new Date());
		record.setChangeMoney(changeMoney);
		record.setChangeType(changeType);
		record.setRemainingSum(userCard.getRemainingSum());
		record.setTotalAmount(userCard.getTotalAmount());
		record.setUserCardId(userCard.getId());
		record.setRecordDesc("会员卡支付");
		record.setStatus("OK");
		recordsDao.save(record);
	}
	
	private void saveOrderPayItem(Orders order,String payType,String payMode){
		OrderPayItems item = payItemDao.findByOrder(order.getId(), payType);
		item.setPayMode(payMode);
		if(StringUtil.equals(OrderPayItems.TYPE_BOOKING, payType)){
			item.setPayMoney(order.getOrderMoney());
		}else{
			item.setPayMoney(NumberUtil.numberSubtract(order.getRoomPrice(),order.getOrderMoney(),2));
			BigDecimal otherPrice = (null!=order.getOtherPrice()?order.getOtherPrice():new BigDecimal(0));
			item.setPayMoney(item.getPayMoney().add(otherPrice));
		}
		item.setPayType(payType);
		item.setStatus(OrderPayItems.STATUS_PAID);
		payItemDao.update(item);
	}
	
	/**
	 * 预定页面-确认预定
	 * @return
	 */
	@RequestMapping(value="/bookingPage",method=RequestMethod.POST)
	public String bookingPage(String roomIds,String bookingDates,String timeRanges,Model model){
		
		List<OrderItems> orderItems = parseOrderItems(roomIds, bookingDates, timeRanges);
		
		BigDecimal roomPrice = new BigDecimal(0);
		BigDecimal bookingPrice = new BigDecimal(0);
		for (OrderItems orderItem : orderItems) {
			roomPrice = NumberUtil.add(roomPrice, orderItem.getRoomPrice(), 2);
			bookingPrice = NumberUtil.add(bookingPrice, orderItem.getBookingPrice(), 2);
		}
		Long busId = orderItems.get(0).getBusId();
		
		model.addAttribute("roomPrice",roomPrice);
		model.addAttribute("bookingPrice",bookingPrice);
		model.addAttribute("orderItems", orderItems);
		model.addAttribute("roomIds",roomIds);
		model.addAttribute("bookingDates",bookingDates);
		model.addAttribute("timeRanges", timeRanges);
		List<CouponActivity> coupons = couponDao.findByBus(busId, CouponActivity.DISCOUNT_TYPE, "OK", orderItems.get(0).getReserveDate());
		model.addAttribute("coupons", coupons);
		return "wap/yuding";
	}
	
	private List<OrderItems> parseOrderItems(String roomIds,String bookingDates,String timeRanges){
		String[] roomIdArray = roomIds.split("\\|");
		String[] bookingDateArray = bookingDates.split("\\|");
		String[] timeRangeArray = timeRanges.split("\\|");
		List<OrderItems> orderItems = new ArrayList<OrderItems>();
		
		for (int i=0;i<roomIdArray.length;i++) {
			Room room = roomDao.find(Long.valueOf(roomIdArray[i]));
			Date reserveDate = DateUtils.string2Date(bookingDateArray[i], "yyyy-MM-dd");
			RoomBookingRangeConfig roomConfig = roomConfigDao.find(Long.valueOf(timeRangeArray[i]));
			OrderItems item = new OrderItems();
			item.setBusId(room.getBusId());
			item.setRoomId(room.getId());
			item.setRoomName(room.getRoomName());
			item.setShopId(room.getShopId());
			item.setShopName(room.getShopName());
			item.setReserveDate(reserveDate);
			item.setTimeRange(timeRangeArray[i]);
			item.setRoomPrice(roomConfig.getRoomPrice());
			item.setReserveTime(roomConfig.getTimeRange());
			item.setBookingPrice(NumberUtil.multiply(roomConfig.getRoomPrice(), new BigDecimal(roomConfig.getBookingPriceRate()), 2));
			item.setPersonRange(room.getMinPerson()+"-"+room.getMaxPerson());
			orderItems.add(item);
		}
		return orderItems;
	}
	
	/**
	 * 获取场地名称
	 * @param items
	 * @return
	 */
	public String getRoomNames(List<OrderItems> items){
		List<String> roomNames = new ArrayList<String>();
		for (OrderItems orderItems : items) {
			String temp = orderItems.getRoomName();
			if(!roomNames.contains(temp)){
				roomNames.add(temp);
			}
		}
		return ListUtils.convertToString(roomNames, ",");
	}
	
	/**
	 * 获取预定时间
	 * @param items
	 * @return
	 */
	public String getBookingDates(String bookingDates){
		String[] bookingDateArray = bookingDates.split("\\|");
		List<String> bookingDateList = new ArrayList<String>();
		for (String temp : bookingDateArray) {
			if(!bookingDateList.contains(temp)){
				bookingDateList.add(temp);
			}
		}
		return ListUtils.convertToString(bookingDateList, ",");
	}
	
	/**
	 * 提交订单
	 * @param order
	 */
	@RequestMapping(value="/createOrder",method=RequestMethod.POST)
	public ModelAndView createOrder(Orders order,String roomIds,String bookingDates,String timeRanges){
		
		List<OrderItems> items = parseOrderItems(roomIds, bookingDates, timeRanges);
		String roomNames = getRoomNames(items);
		String bookingDateList = getBookingDates(bookingDates);
		Room room = roomDao.find(items.get(0).getRoomId());
		order.setBusId(room.getBusId());
		order.setBusName(room.getBusName());
		order.setShopId(room.getShopId());
		order.setShopName(room.getShopName());
		order.setStatus(Orders.UNPAID);
		order.setOrderNumber(TenPayUtils.getCurrTime());
		order.setOrderDate(new Date());
		order.setRoomNames(roomNames);
		order.setBookingDates(bookingDateList);
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean) session.getAttribute("WC_USER");
		
		ModelAndView view = new ModelAndView("wap/zhifu");
		if(null == user){
			//TOOD 
			return view;
		}
		order.setUserName(user.getNickname());
		order.setUserToken(user.getOpenid());
		orderDao.save(order);
		
		OrderPayItems payItem = new OrderPayItems();
		payItem.setOrderId(order.getId());
		payItem.setPayType(OrderPayItems.TYPE_BOOKING);
		payItem.setStatus(OrderPayItems.STATUS_UNPAID);
		payItem.setPayMoney(order.getOrderMoney());
		payItemDao.save(payItem);
		
		OrderPayItems fullPayItem = new OrderPayItems();
		
		fullPayItem.setOrderId(order.getId());
		fullPayItem.setPayType(OrderPayItems.TYPE_FINAL);
		fullPayItem.setStatus(OrderPayItems.STATUS_UNPAID);
		fullPayItem.setPayMoney(order.getRoomPrice().subtract(order.getOrderMoney()));
		payItemDao.save(fullPayItem);
		
		for (OrderItems orderItems : items) {
			orderItems.setOrderId(order.getId());
			orderItems.setCouponActivityId(order.getCouponActivityId());
			orderItems.setCouponActivityName(order.getCouponActivityName());
			orderItemDao.save(orderItems);
		}
		view.addObject("order", order);
		view.addObject("roomNames", roomNames);
		
		Constant outTime = constantDao.findByConstantId(order.getBusId(), "ORDER_OUT_TIME");
		Calendar cal = Calendar.getInstance();
		cal.setTime(order.getOrderDate());
		cal.add(Calendar.MINUTE, Integer.valueOf(outTime.getConstantValue()));
		view.addObject("outTime", cal.getTimeInMillis());
		
		return view;
	}
	
	/**
	 * 支付界面
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/{orderId}/payOrderPage")
	public ModelAndView payOrderPage(@PathVariable Long orderId){
		ModelAndView view = new ModelAndView("wap/zhifu");
		Orders order = orderDao.find(orderId);
		view.addObject("order", order);
		
		Constant outTime = constantDao.findByConstantId(order.getBusId(), "ORDER_OUT_TIME");
		Calendar cal = Calendar.getInstance();
		cal.setTime(order.getOrderDate());
		cal.add(Calendar.MINUTE, Integer.valueOf(outTime.getConstantValue()));
		view.addObject("outTime", cal.getTimeInMillis());
		
		List<OrderItems> items = orderItemDao.findByOrder(orderId);
		String roomNames = "";
		for (OrderItems orderItems : items) {
			roomNames += orderItems.getRoomName()+"，";
		}
		view.addObject("roomNames", roomNames.substring(0, roomNames.length()-1));
		view.addObject("showwxpaytitle","1");
		return view;
	}
	
	/**
	 * 微信支付订单
	 * @param orderId
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/{orderId}/tenPayOrder")
	public @ResponseBody JSONObject tenPayOrder(@PathVariable Long orderId,HttpServletRequest request) throws Exception{
		Orders order = orderDao.find(orderId);
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean) session.getAttribute("WC_USER");
		JSONObject result = tenPay(request, order, user);
		return result;
	}
	
	/**
	 * 微信支付处理逻辑
	 * @param request
	 * @param localOrder
	 * @param wxUser
	 * @return
	 * @throws Exception
	 */
	private JSONObject tenPay(HttpServletRequest request,Orders localOrder,UserBean wxUser) throws Exception{
		WechatConfig config = configDao.selectByBusId(localOrder.getBusId());
		UnifiedorderBean order = new UnifiedorderBean();
//		order.setAppid(config.getAppId());
		order.setAppid("wx0f0a6062e33d800c");
		order.setDevice_info("WEB");
		order.setMch_id(config.getMachId());// 商户号
		order.setNonce_str(TenPayUtils.getCurrTime() + TenPayUtils.buildRandom(18));// 32位随机数 :12位日期+20位随机数
		
		List<OrderItems> items = orderItemDao.findByOrder(localOrder.getId());
		
		OrderPayItems payItem = null;
		
		String body = "";
		for (OrderItems orderItems : items) {
			body += orderItems.getRoomName()+"，";
		}
		order.setBody(body.substring(0,body.length()-1));// 商品简要描述
		Long totalFee = 0l;
		if(Orders.PAID.equals(localOrder.getStatus())){
			//（场地价格-预付定金）*1000
			totalFee = localOrder.getRoomPrice().subtract(localOrder.getOrderMoney()).multiply(new BigDecimal(100)).longValue();
			order.setOut_trade_no("FULL"+localOrder.getOrderNumber());// 商户订单号
			payItem = payItemDao.findByOrder(localOrder.getId(), OrderPayItems.TYPE_FINAL);
		}else{
			order.setOut_trade_no(localOrder.getOrderNumber());// 商户订单号
			totalFee = localOrder.getOrderMoney().multiply(new BigDecimal(100)).longValue();
			payItem = payItemDao.findByOrder(localOrder.getId(), OrderPayItems.TYPE_BOOKING);
		}
		order.setTotal_fee(totalFee+"");// 根据订单状态设置，未付款的设置预订款，已付款的设置尾款
		order.setSpbill_create_ip(request.getRemoteAddr());// 终端IP
		order.setTime_start(TenPayUtils.getCurrTime());// 交易开始时间
		order.setAttach(localOrder.getStatus());//订单状态
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);
		order.setTime_expire(TenPayUtils.date2String(cal.getTime(),"yyyyMMddHHmmss"));// 交易结束时间（失效时间） 默认当前时间+5分钟
		
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+path;
		
		order.setNotify_url(basePath+"/protal/tenpay/notify");// 接收微信支付异步通知回调地址
		
		order.setTrade_type("JSAPI");// 交易类型
		order.setOpenid(wxUser.getOpenid());
		//将参数以a-z排序
		Map<String, String> map = TenPaySign.convertBean(order);
		//将参数通过MD5加密生成签名,注意：key为sign|key的，value为空的不参与签名
		String sign = TenPaySign.createSign(config.getPrivateKey(),map);
		order.setSign(sign);
		
		JSONObject json = new JSONObject();
		
		TenPayClient client = new TenPayClient(localOrder.getBusId());
		Map<String,String> result = client.unifiedorder(order);
		if("FAIL".equals(result.get("return_code"))){
			json.put("errorMsg", result.get("return_msg"));
			return json;
		}
		if(!"OUT_TRADE_NO_USED".equals(result.get("err_code"))){
			payItem.setPrepay_id(result.get("prepay_id"));
			payItem.setPayMode(OrderPayItems.MODE_TENAPY);
			payItemDao.update(payItem);
		}
		
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = TenPayUtils.getUnixTime(new Date())+"";
		String packages = "prepay_id="+payItem.getPrepay_id();
//		finalpackage.put("appId", config.getAppId());  
		finalpackage.put("appId", "wx0f0a6062e33d800c");
		finalpackage.put("timeStamp", timestamp);  
		finalpackage.put("nonceStr", order.getNonce_str());  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		String finalsign = TenPaySign.createSign(config.getPrivateKey(),finalpackage);
		json.put("errorMsg", "OK");
		json.put("appId", config.getAppId());  
		json.put("timeStamp", timestamp);  
		json.put("nonceStr", order.getNonce_str());  
		json.put("packageStr", packages);  
		json.put("signType", "MD5");
		json.put("sign", finalsign);
		return json;
	}
}
