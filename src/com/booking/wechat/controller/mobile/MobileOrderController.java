package com.booking.wechat.controller.mobile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.booking.wechat.client.model.UserBean;
import com.booking.wechat.client.pay.TenPayUtils;
import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.mobile.vo.OrderContent;
import com.booking.wechat.controller.mobile.vo.OrderItem;
import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.bean.coupon.CouponActivity;
import com.booking.wechat.persistence.bean.member.MemberCard;
import com.booking.wechat.persistence.bean.order.OrderItems;
import com.booking.wechat.persistence.bean.order.OrderItemsSetMeal;
import com.booking.wechat.persistence.bean.order.OrderPayItems;
import com.booking.wechat.persistence.bean.order.Orders;
import com.booking.wechat.persistence.bean.setmeal.SetMeal;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.persistence.service.coupon.CouponActivityDao;
import com.booking.wechat.persistence.service.member.MemberCardDao;
import com.booking.wechat.persistence.service.order.OrderItemsDao;
import com.booking.wechat.persistence.service.order.OrderItemsSetMealDao;
import com.booking.wechat.persistence.service.order.OrderPayItemsDao;
import com.booking.wechat.persistence.service.order.OrdersDao;
import com.booking.wechat.util.DateUtils;

/**
 * 订单控制器
 * @ClassName MobileOrderController
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月30日 下午2:44:54
 *
 */
@Controller
@RequestMapping("/mobile/orders")
public class MobileOrderController extends BaseController {
	
	@Autowired
	private ConstantDao constantDao;//系统配置
	
	@Autowired
    private CouponActivityDao couponActivityDao;//优惠卷
	
	@Autowired
	private OrdersDao ordersDao;//订单
	
	@Autowired
	private OrderPayItemsDao orderPayItemsDao;//支付单据 其实这个没什么用的
	
	@Autowired
	private OrderItemsDao OrderItemsDao;//订单详情
	
	@Autowired
	private OrderItemsSetMealDao orderItemsSetMealDao;//这是订单里面包含的套餐内容
	
	@Autowired
	private MemberCardDao memberCardDao;//会员卡信息

	/**
	 * 创建订单，这个地方需要登录微信用户，或者是已经登录了才可以
	 * @author shrChang.Liu
	 * @param Orders 订单信息 JSON格式
	 * @说明：  现在的订单里面可以包含2种优惠信息 一个是折扣 一个是减免多少 ，另外所有的增值服务是没有优惠的 只有场地是有优惠的 暂时
	 * @return 
	 * @date 2018年10月30日 下午3:17:43
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createOrder(@RequestBody OrderContent content){
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean)session.getAttribute("WC_USER");
		if(user == null){
			return getFailResultMap("获取微信用户失败！");
		}
		if(content == null || content.getItems().isEmpty()){
			return getFailResultMap("订单不能为空!");
		}
		
		//创建订单，包含订单主题，订单详情，订单附属套餐 还有价格
		//1.获取订单超时时间 用于界面跳转后的倒计时计算
		//Constant orderTimeout = constantDao.findByConstantId(content.getBusId(), "ORDER_OUT_TIME");
		//组装数据按照原来的逻辑 主订单需要的东西
		Orders orders = new Orders();//创建订单
		orders.setBusId(content.getBusId());
		orders.setBusName(content.getBusName());
		orders.setShopId(content.getShopId());
		orders.setShopName(content.getShopName());
		orders.setStatus(Orders.UNPAID);//未付款 默认
		orders.setOrderNumber(TenPayUtils.getCurrTime());//订单号 现在这个逻辑我感觉会有问题，怎么可以这样？ 不懂 建议使用uuid
		orders.setOrderDate(new Date());
		orders.setCouponActivityId(content.getCouponActivityId());//优惠id
		orders.setCouponActivityName(content.getCouponActivityName());//优惠名称
		orders.setOrderDesc("预定场地下单！");
		orders.setOrderType("yuding");//预定订单
		orders.setPhoneNumber(content.getPhoneNumber());//电话号码、  如果是已经是会员应该默认就是自己的手机号码
		//这里会有一个问题 就是应该去创建一个客户的会员卡信息
		MemberCard card = memberCardDao.findByOpenId(user.getOpenid());
		if(card != null){
			card.setPhoneNo(content.getPhoneNumber());
			if(card.getLevel() == 0){//如果是游客 需要将游客变成有普通的会员 然后这个等级现在还没定按照积分还是按照消费金额来算
				card.setLevel(1);
			}
			memberCardDao.update(card);
		}
		
		orders.setRecommend(content.getRecommend());
		orders.setUserName(user.getNickname());
		orders.setUserToken(user.getOpenid());
		//现在要解决下面的这个东西
		BigDecimal beforeRoomPrice = new BigDecimal("0.00");//总金额
		BigDecimal orderMoney = new BigDecimal("0.00");//预付金额
		BigDecimal otherPrice = new BigDecimal("0.00");//增值套餐
		StringBuffer roomNames = new StringBuffer();
		StringBuffer bookingDates = new StringBuffer();
		for(OrderItem item : content.getItems()){
			beforeRoomPrice.add(item.getPrice());
			orderMoney.add(item.getPrice().multiply(item.getBookingPriceRate()));//预定金额
			roomNames.append(","+item.getRoomName());
			bookingDates.append(","+item.getTimeRange());
			for(SetMeal meal : item.getMeals()){
				otherPrice.add(meal.getMealPrice());
			}
		}
		
		orders.setOtherPrice(otherPrice);//增值服务的钱 ，这个不能优惠且必须全款？ 这个没有折扣？ 还是有折扣？ 这是一个问题
		orders.setRoomNames(roomNames.substring(1));//预定的场地的名称 逗号隔开
		orders.setOrderMoney(orderMoney);//预付金额
		orders.setRoomPrice(beforeRoomPrice);//折扣后的总额 不包含增值服务费用 这个应该是按照优惠卷减免后的？
		orders.setBookingDates(bookingDates.substring(1));//那些时间段 逗号隔开
		orders.setBeforeRoomPrice(beforeRoomPrice);//原价
		//判断是不是优惠卷存在 查询到
		if (orders.getCouponActivityId() != null) {
			CouponActivity activity = couponActivityDao.find(orders.getCouponActivityId());
			if (CouponActivity.CASH_TYPE.equals(activity.getCouponType())) {// 现金优惠
																			// 直接减掉即可
				orders.setOrderMoney(orderMoney.subtract(activity.getFreeMoney()));// 预付金额
				orders.setRoomPrice(beforeRoomPrice.subtract(activity.getFreeMoney()));// 折扣后的总额
																						// 不包含增值服务费用
																						// 这个应该是按照优惠卷减免后的？
			} else if (CouponActivity.DISCOUNT_TYPE.equals(activity.getCouponType())) {//折扣优惠
				orders.setOrderMoney(orderMoney.multiply(activity.getDiscount()));// 预付金额
				orders.setRoomPrice(beforeRoomPrice.multiply(activity.getDiscount()));// 折扣后的总额
			}
		}
		//组装了订单的数据
		ordersDao.save(orders);
		//下面是支付的两个订单生成 暂时不知道是否有必要 后续应该会使用balancerecord替换掉
		createPayOrderItems(orders);
		//创建订单详情
		createOrderItem(content, orders);
		return getSuccessResultMap("创建订单成功！",orders);
	}

	/**
	 * 创建订单详情
	 * @author shrChang.Liu
	 * @param content
	 * @param orders
	 * @date 2018年10月31日 上午11:29:10
	 * @return void
	 * @description
	 */
	private void createOrderItem(OrderContent content, Orders orders) {
		//组装订单详情的数据
		for(OrderItem item : content.getItems()){
			OrderItems orderItems = new OrderItems();
			orderItems.setBookingPrice(item.getPrice().multiply(item.getBookingPriceRate()));//预定价格
			orderItems.setBusId(content.getBusId());//商户id
			orderItems.setCouponActivityId(content.getCouponActivityId());//优惠id
			orderItems.setCouponActivityName(content.getCouponActivityName());//优惠名称
			orderItems.setOrderId(orders.getId());
			orderItems.setReserveDate(DateUtils.string2Date(item.getBookingDate(), "yyyy-MM-dd"));//预定日期
			orderItems.setReserveTime(item.getTimeRange());//预定的时间段
			orderItems.setRoomId(item.getRoomId());//场地id
			orderItems.setRoomName(item.getRoomName());//场地名称
			orderItems.setRoomPrice(item.getPrice());//场地的价格
			orderItems.setShopId(content.getShopId());//商铺id
			orderItems.setShopName(content.getShopName());//商铺名称
			orderItems.setTimeRange(item.getRangeId());//配置场地价格的配置id 很重要 实际上 返回给后端的只需要这个id+预定的日期即可 其余的都可以查到的 还有优惠id
			OrderItemsDao.save(orderItems);
			//保存增值套餐信息
			for(SetMeal meal : item.getMeals()){
				OrderItemsSetMeal itemsSetMeal = new OrderItemsSetMeal();
				itemsSetMeal.setItemId(orderItems.getId());
				itemsSetMeal.setMealDesc(meal.getMealDesc());
				itemsSetMeal.setMealName(meal.getMealName());
				itemsSetMeal.setMealPrice(meal.getMealPrice());
				itemsSetMeal.setMealSrc(meal.getMealSrc());
				itemsSetMeal.setOrderId(orders.getId());
				itemsSetMeal.setSetMealId(meal.getId());
				orderItemsSetMealDao.save(itemsSetMeal);
			}
		}
	}

	/**
	 * 创建支付单据信息 分开2部分 第一部分是定金+增值服务  第二部分是 尾款
	 * @author shrChang.Liu
	 * @param orders
	 * @date 2018年10月31日 上午11:17:41
	 * @return void
	 * @description
	 */
	private void createPayOrderItems(Orders orders) {
		OrderPayItems items = new OrderPayItems();
		items.setOrderId(orders.getId());
		items.setPayMoney(orders.getOrderMoney().add(orders.getOtherPrice()));//预付款必须包含场地定金+增值服务费用
		items.setPayType(OrderPayItems.TYPE_BOOKING);//预付款
		items.setStatus(OrderPayItems.STATUS_UNPAID);//未支付
		items.setPayForAnotherId(null);//会员openid
		items.setPayForAnotherName("会员卡名称！");
		items.setPayMode(null);//支付方式
		items.setPrepay_id(null);//第三方支付的流水号
		items.setSwiftNumber(null);//付款的流水号
		items.setUserCardNumber(null);//会员卡号
		orderPayItemsDao.save(items);
		OrderPayItems items2 = new OrderPayItems();
		items2.setOrderId(orders.getId());
		items2.setPayMoney(orders.getRoomPrice().subtract(orders.getOrderMoney()));//总金额-预付款 因为增值服务的费用已经付了
		items2.setPayType(OrderPayItems.TYPE_FINAL);//尾款
		items2.setStatus(OrderPayItems.STATUS_UNPAID);//未支付
		items2.setPayForAnotherId(null);//会员openid
		items2.setPayForAnotherName("会员卡名称！");
		items2.setPayMode(null);//支付方式
		items2.setPrepay_id(null);//第三方支付的流水号
		items2.setSwiftNumber(null);//付款的流水号
		items2.setUserCardNumber(null);//会员卡号
		orderPayItemsDao.save(items2);
	}
	
	/**
	 * 查询订单的内容 主要是为了在支付界面 还有指定的详情界面使用的,
	 * 可传一个参数 提示是否需要查询对应的子的信息
	 * @author shrChang.Liu
	 * @param id
	 * @return
	 * @date 2018年10月31日 上午11:30:36
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/querOrder",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> queryOrderById(@RequestParam(value="id")Long id){
		Orders orders = ordersDao.find(id);
		//这里可能需要计算一下超时时间 只针对未付款的情况
		if(Orders.UNPAID.equals(orders.getStatus())){
			Constant orderTimeout = constantDao.findByConstantId(orders.getBusId(), "ORDER_OUT_TIME");
			orders.setTimeOut(orderTimeout.getConstantValue());
		}
		return getSuccessResultMap("查询订单信息成功！", orders);
	}
	
	/**
	 * 查询订单详细信息
	 * @author shrChang.Liu
	 * @param id
	 * @return
	 * @date 2018年10月31日 下午1:44:38
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/querOrderDeatail",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> queryOrderDetailById(@RequestParam(value="id")Long id){
		Orders orders = ordersDao.find(id);
		List<OrderItems> items = OrderItemsDao.findByOrder(id);
		List<OrderItemsSetMeal> meals = orderItemsSetMealDao.findByOrderId(id);
		Map<String,List<OrderItemsSetMeal>> mealMap = new HashMap<String, List<OrderItemsSetMeal>>();
		for(OrderItemsSetMeal meal : meals){
			List<OrderItemsSetMeal> setMeals = new ArrayList<OrderItemsSetMeal>();
			if(mealMap.containsKey(String.valueOf(meal.getItemId()))){
				setMeals = mealMap.get(String.valueOf(meal.getItemId()));
			}
			setMeals.add(meal);
			mealMap.put(String.valueOf(meal.getItemId()), setMeals);
		}
		for(OrderItems orderItem : items){
			if(mealMap.containsKey(String.valueOf(orderItem.getId()))){
				orderItem.setMeals(mealMap.get(String.valueOf(orderItem.getId())));
			}
		}
		orders.setOrderItems(items);
		return getSuccessResultMap("获取订单详情成功！", orders);
	}
	
	/**
	 * 取消订单
	 * @author shrChang.Liu
	 * @param id
	 * @return
	 * @date 2018年10月31日 下午2:11:20
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/cancel",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> canleOrder(@RequestParam(value="id")Long id){
		Orders orders = ordersDao.find(id);
		if(!Orders.UNPAID.equals(orders.getStatus())){
			return getFailResultMap("只能取消未付款的订单！");
		}
		orders.setStatus(Orders.CANCELLED);
		ordersDao.update(orders);
		return getSuccessResultMap("取消订单成功！", orders);
	}
	
	/**
	 * 删除订单
	 * @author shrChang.Liu
	 * @param id
	 * @return
	 * @date 2018年10月31日 下午2:14:52
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteOrder(@RequestParam(value="id")Long id){
		Orders orders = ordersDao.find(id);
		if(!Orders.FINISH.equals(orders.getStatus())){
			return getFailResultMap("只能删除已经完成的订单！");
		}
		ordersDao.delete(id);
		return getSuccessResultMap("删除订单成功！");
	}
	
}
