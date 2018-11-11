package com.booking.wechat.controller.mobile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.booking.wechat.client.model.UserBean;
import com.booking.wechat.client.model.pay.UnifiedorderBean;
import com.booking.wechat.client.pay.TenPayClient;
import com.booking.wechat.client.pay.TenPaySign;
import com.booking.wechat.client.pay.TenPayUtils;
import com.booking.wechat.constant.Constant;
import com.booking.wechat.controller.BaseController;
import com.booking.wechat.persistence.bean.member.BalanceRecord;
import com.booking.wechat.persistence.bean.member.MemberCard;
import com.booking.wechat.persistence.bean.order.OrderPayItems;
import com.booking.wechat.persistence.bean.order.Orders;
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.member.BalanceRecordDao;
import com.booking.wechat.persistence.service.member.MemberCardDao;
import com.booking.wechat.persistence.service.order.OrderPayItemsDao;
import com.booking.wechat.persistence.service.order.OrdersDao;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;

/**
 * 支付控制器
 * 
 * @ClassName MobilePayController
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月31日 下午2:49:53
 *
 */
@Controller
@RequestMapping("/mobile/pay")
public class MobilePayController extends BaseController {
	
	private Logger logerr = Logger.getLogger(MobilePayController.class);
	
	@Autowired
	private WechatConfigDao wechatconfigDao;

	@Autowired
	private OrdersDao ordersDao;// 订单

	@Autowired
	private BalanceRecordDao balanceRecordDao;// 会员变动

	@Autowired
	private MemberCardDao memberCardDao;// 会员卡信息
	
	@Autowired
	private OrderPayItemsDao orderPayItemsDao;//支付单据 其实这个没什么用的

	/**
	 * 支付定金 预付款
	 * 
	 * @author shrChang.Liu
	 * @param orderId
	 * @param payType
	 *            {@link Constant}
	 * @return
	 * @date 2018年10月31日 下午2:52:20
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value = "/frontMoney", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> orderPay(@RequestParam(value = "orderId") Long orderId,
			@RequestParam(value = "payType") String payType,HttpServletRequest request) {
		// 查询到订单
		Orders orders = ordersDao.find(orderId);
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean) session.getAttribute("WC_USER");
		// 余额支付
		BigDecimal amount = orders.getOrderMoney().add(orders.getOtherPrice());
		if (payType.equals(Constant.PAY_TYPE_BALANCE)) {
			MemberCard card = memberCardDao.findByOpenId(user.getOpenid());
			if (card == null) {
				return getFailResultMap("您还不是会员，请确认！");
			}
			// 需要付的金额 定金+附加套餐的钱
			if (card.getBalance().subtract(amount).doubleValue() < 0) {
				return getFailResultMap("您的余额不足,请充值或者选择其他支付方式！");
			}
			//下面就是写进去流水扣钱 修改订单的状态
			BalanceRecord balanceRecord = new BalanceRecord();
			balanceRecord.setAmount(new BigDecimal("0.00").subtract(amount));//这个地方应该是负数
			balanceRecord.setCreateDate(new Date());
			balanceRecord.setMemberCard(card.getCardNo());
			balanceRecord.setMemberId(card.getId());
			balanceRecord.setOpenId(user.getOpenid());
			balanceRecord.setOrderId(String.valueOf(orderId));//这个订单号啊 是真的订单号
			balanceRecord.setSource(null);
			balanceRecord.setStatus(Constant.PAY_RESULT_SUCCESS);
			balanceRecord.setType(Constant.CHANGE_TYPE_BALANCE);//这个是余额支付
			balanceRecordDao.save(balanceRecord);//新增一条流水
			//下面是修改帐户金额
			card.setBalance(card.getBalance().subtract(amount));
			memberCardDao.update(card);
			//修改订单信息
			orders.setStatus(Orders.PAID);
			ordersDao.update(orders);
			//可以的话就把那个失效的也修改了
			OrderPayItems payItems = orderPayItemsDao.findByOrder(orderId, OrderPayItems.TYPE_BOOKING);
			payItems.setStatus(OrderPayItems.STATUS_PAID);
			return getSuccessResultMap("支付成功！");
		}else if(payType.equals(Constant.PAY_TYPE_WX)){//微信支付
			try {
				return WxPay(request, orders, user, amount);
			} catch (Exception e) {
				logerr.error("调用微信支付失败：",e);
				return getFailResultMap("调用微信支付失败！");
			}
		}
		return getFailResultMap("没有找到对应的支付类型！");
	}

	/**
	 * 微信支付
	 * @author shrChang.Liu
	 * @param request
	 * @param orders
	 * @param user
	 * @param amount
	 * @return
	 * @throws Exception
	 * @date 2018年11月11日 下午10:57:56
	 * @return Map<String,Object>
	 * @description
	 */
	private Map<String, Object> WxPay(HttpServletRequest request, Orders orders, UserBean user, BigDecimal amount)
			throws Exception {
		String outOrderId =  UUID.randomUUID().toString().replaceAll("-", "");
		WechatConfig config = wechatconfigDao.selectByBusId(orders.getBusId());
		UnifiedorderBean order = new UnifiedorderBean();
		order.setAppid(config.getAppId());
		order.setDevice_info("WEB");
		order.setMch_id(config.getMachId());// 商户号
		order.setNonce_str(TenPayUtils.getCurrTime() + TenPayUtils.buildRandom(18));// 32位随机数 :12位日期+20位随机数
		order.setBody(orders.getRoomNames());// 商品简要描述
		order.setOut_trade_no(outOrderId);// 商户订单号
		order.setTotal_fee(amount.multiply(new BigDecimal(100)).longValue()+"");// 根据订单状态设置，未付款的设置预订款，已付款的设置尾款
		order.setSpbill_create_ip(request.getRemoteAddr());// 终端IP
		order.setTime_start(TenPayUtils.getCurrTime());// 交易开始时间
		order.setAttach(orders.getStatus());//订单状态
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);
		order.setTime_expire(TenPayUtils.date2String(cal.getTime(),"yyyyMMddHHmmss"));// 交易结束时间（失效时间） 默认当前时间+5分钟
		
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+path;
		order.setNotify_url(basePath+"/mobile/pay/tenpay/notify");// 接收微信支付异步通知回调地址
		
		order.setTrade_type("JSAPI");// 交易类型
		order.setOpenid(user.getOpenid());
		//将参数以a-z排序
		Map<String, String> map = TenPaySign.convertBean(order);
		//将参数通过MD5加密生成签名,注意：key为sign|key的，value为空的不参与签名
		String sign = TenPaySign.createSign(config.getPrivateKey(),map);
		order.setSign(sign);
		Map<String,String> payResult = new HashMap<String, String>();
		
		TenPayClient client = new TenPayClient(orders.getBusId());
		Map<String,String> result = client.unifiedorder(order);
		OrderPayItems payItems = orderPayItemsDao.findByOrder(orders.getId(), OrderPayItems.TYPE_BOOKING);
		if("FAIL".equals(result.get("return_code"))){
			return getFailResultMap("支付失败:"+result.get("return_msg"));
		}
		//下面添加一条资金变动的流水
		//下面就是写进去流水扣钱 修改订单的状态
		MemberCard card = memberCardDao.findByOpenId(user.getOpenid());
		BalanceRecord balanceRecord = new BalanceRecord();
		balanceRecord.setAmount(amount);//这个地方应该是负数 花掉的钱
		balanceRecord.setCreateDate(new Date());
		balanceRecord.setMemberCard(card.getCardNo());
		balanceRecord.setMemberId(card.getId());
		balanceRecord.setOpenId(user.getOpenid());
		balanceRecord.setOrderId(String.valueOf(orders.getId()));//这个订单号啊 是真的订单号
		balanceRecord.setOutOrderId(outOrderId);
		balanceRecord.setSource(null);
		balanceRecord.setStatus(Constant.PAY_RESULT_DOING);
		balanceRecord.setType(Constant.CHANGE_TYPE_WX);//微信支付
		String prepay_id = null;//第三方的
		if(!"OUT_TRADE_NO_USED".equals(result.get("err_code"))){
			prepay_id = result.get("prepay_id");
			payItems.setPrepay_id(prepay_id);//没办法暂时用这个吧
			orderPayItemsDao.update(payItems);//更新了
			balanceRecord.setPayOrder(prepay_id);
		}
		balanceRecordDao.save(balanceRecord);//新增一条流水
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = TenPayUtils.getUnixTime(new Date())+"";
		String packages = "prepay_id="+prepay_id;
		finalpackage.put("appId", config.getAppId());  
		finalpackage.put("timeStamp", timestamp);  
		finalpackage.put("nonceStr", order.getNonce_str());  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		String finalsign = TenPaySign.createSign(config.getPrivateKey(),finalpackage);
		payResult.put("errorMsg", "OK");
		payResult.put("appId", config.getAppId());  
		payResult.put("timeStamp", timestamp);  
		payResult.put("nonceStr", order.getNonce_str());  
		payResult.put("packageStr", packages);  
		payResult.put("signType", "MD5");
		payResult.put("sign", finalsign);
		return getSuccessResultMap("调用微信支付成功",payResult);
	}
	
	/**
	 * 微信支付的内容
	 * @author shrChang.Liu
	 * @param request
	 * @param response
	 * @throws Exception
	 * @date 2018年10月29日 上午11:08:47
	 * @return void
	 * @description TODO 怎么处理会员等级的事情 这个需要讨论 还有积分的也是一样的 积分也是要消费的时候才有积分
	 */
	@RequestMapping(value="/tenpay/notify",method=RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        //sb为微信返回的xml
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine())!=null){
            sb.append(line);
        }
        //修改订单信息
        try {
			Map<String, String> notifyMap = TenPayUtils.parseXml(sb.toString());
			if("SUCCESS".equals(notifyMap.get("result_code")) && "SUCCESS".equals(notifyMap.get("return_code"))){
				String orderNumber = notifyMap.get("out_trade_no");
				BalanceRecord record = balanceRecordDao.selectByOutOrderId(orderNumber);
				Orders orders = ordersDao.find(record.getOrderId());
				MemberCard card = memberCardDao.find(record.getMemberId());
				//要关联商户号 记得
				TenPayClient payClient = new TenPayClient(card.getBusId());
				Map<String, String> tenOrder = payClient.queryOrder(orderNumber);
				//查询订单验证订单支付状态
				if(null != tenOrder.get("trade_state") && "SUCCESS".equals(tenOrder.get("trade_state"))){
					//这是成功的情况
					record.setStatus(Constant.PAY_RESULT_SUCCESS);
					balanceRecordDao.update(record);//更新一下状态
					//需要注意的是这里有订单的内容需要看看了
					OrderPayItems payItem = null;
					if(Orders.UNPAID.equals(notifyMap.get("attach"))){
						payItem = orderPayItemsDao.findByOrder(orders.getId(), OrderPayItems.TYPE_BOOKING);
						orders.setStatus(Orders.PAID);
					}else if(Orders.PAID.equals(notifyMap.get("attach"))){
						orders.setStatus(Orders.FINISH);
						payItem = orderPayItemsDao.findByOrder(orders.getId(), OrderPayItems.TYPE_FINAL);
					}
					payItem.setPayMode(OrderPayItems.MODE_TENAPY);
					payItem.setStatus(OrderPayItems.STATUS_PAID);
					orderPayItemsDao.update(payItem);
					ordersDao.update(orders);
				}else{//失败的情况
					record.setStatus(Constant.PAY_RESULT_FAILED);
					balanceRecordDao.update(record);//更新一下状态
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.println("success");
	}
}
