package com.booking.wechat.controller.mobile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.member.BalanceRecordDao;
import com.booking.wechat.persistence.service.member.MemberCardDao;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;

import net.sf.json.JSONObject;

/**
 * 会员服务
 * @ClassName MemberController
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月29日 上午10:35:56
 *
 */
@Controller
@RequestMapping("/mobile/member")
public class MemberController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberCardDao memberCardDao;
	
	@Autowired
	private WechatConfigDao wechatconfigDao;
	
	@Autowired
	private BalanceRecordDao balanceRecordDao;
	
	/**
	 * 查询用户
	 * @author shrChang.Liu
	 * @return
	 * @date 2018年10月29日 上午10:39:36
	 * @return Map<String,String>
	 * @description
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> queryMember(){
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean)session.getAttribute("WC_USER");
		if(user == null){
			return getFailResultMap("无法获取微信用户！");
		}else{
			MemberCard card = memberCardDao.findByOpenId(user.getOpenid());
			return getSuccessResultMap("获取用户成功！", card);
		}
	}
	
	/**
	 * 添加会员 一般是从游客变成会员
	 * @author shrChang.Liu
	 * @return
	 * @date 2018年10月29日 上午10:43:43
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addMember(@RequestParam(value="phoneNo",required=false)String phoneNo,@RequestParam(value="birthDay",required=false) @DateTimeFormat(pattern="yyyy-MM-dd")Date birthDay){
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean)session.getAttribute("WC_USER");
		if(user == null){
			return getFailResultMap("无法获取微信用户！");
		}else{
			MemberCard card = memberCardDao.findByOpenId(user.getOpenid());
			MemberCard memberCard = memberCardDao.findByPhoneNo(phoneNo);
			if(memberCard == null){
				card.setPhoneNo(phoneNo);
				card.setBirthDay(birthDay);
				card.setLevel(1);//变成普通会员
				card.setCardNo(phoneNo);//使用手机号码做会员号 个人觉得这个地方要判断手机号码是否已经使用
				return getSuccessResultMap("注册会员成功！", card);
			}else{
				return getFailResultMap("当前手机号码已经被注册，请重新填写手机号码！");
			}
		}
	}
	
	/**
	 * 充值 注意的是这个地方是异步的 但是应该刷新也会很快  
	 * 需要具体测试 可能需要配置充多少送多少的逻辑 先不管 只充多少就是多少
	 * 默认传进来的是元
	 * @author shrChang.Liu
	 * @return
	 * @date 2018年10月29日 上午10:51:53
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/recharge",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> recharge(@RequestParam(value="amount")BigDecimal amount,HttpServletRequest request)throws Exception{
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean)session.getAttribute("WC_USER");
		if(user == null){
			return getFailResultMap("无法获取微信用户！");
		}else{
			MemberCard card = memberCardDao.findByOpenId(user.getOpenid());
			String orderId = UUID.randomUUID().toString().replaceAll("-", "");
			//获取微信配置嘛
			WechatConfig config = wechatconfigDao.selectByBusId(card.getBusId());
			UnifiedorderBean order = new UnifiedorderBean();
			order.setAppid("wx0f0a6062e33d800c");
			order.setDevice_info("WEB");
			order.setMch_id(config.getMachId());// 商户号
			order.setNonce_str(TenPayUtils.getCurrTime() + TenPayUtils.buildRandom(18));// 32位随机数 :12位日期+20位随机数
			order.setBody("噼里啪啦");// 商品简要描述
			order.setOut_trade_no(orderId);// 商户订单号
			order.setTotal_fee(amount.multiply(new BigDecimal(100)).toString());// 根据订单状态设置，未付款的设置预订款，已付款的设置尾款
			order.setSpbill_create_ip(request.getRemoteAddr());// 终端IP
			order.setTime_start(TenPayUtils.getCurrTime());// 交易开始时间
			order.setAttach("recharge");//充值
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, 5);
			order.setTime_expire(TenPayUtils.date2String(cal.getTime(),"yyyyMMddHHmmss"));// 交易结束时间（失效时间） 默认当前时间+5分钟
			String path = request.getContextPath();
			String basePath = request.getScheme()+"://"+request.getServerName()+path;
			order.setNotify_url(basePath+"/mobile/member/tenpay/notify");// 接收微信支付异步通知回调地址
			order.setTrade_type("JSAPI");// 交易类型
			order.setOpenid(card.getOpenId());
			//将参数以a-z排序
			Map<String, String> map = TenPaySign.convertBean(order);
			//将参数通过MD5加密生成签名,注意：key为sign|key的，value为空的不参与签名
			String sign = TenPaySign.createSign(config.getPrivateKey(),map);
			order.setSign(sign);
			TenPayClient client = new TenPayClient(card.getBusId());
			logger.info("微信支付传参：" + JSONObject.fromObject(order).toString());
			Map<String,String> result = client.unifiedorder(order);
			BalanceRecord balanceRecord = new BalanceRecord();
			balanceRecord.setAmount(amount);//元
			balanceRecord.setCreateDate(new Date());
			balanceRecord.setGiftAmount(new BigDecimal("0.00"));//默认是没有赠送金额的
			balanceRecord.setMemberCard(card.getCardNo());
			balanceRecord.setMemberId(card.getId());
			balanceRecord.setOpenId(card.getOpenId());
			balanceRecord.setOrderId(orderId);
			balanceRecord.setSource(Constant.PAY_TYPE_WX);//微信支付
			balanceRecord.setType(Constant.CHANGE_TYPE_RECHARGE);//充值
			balanceRecord.setStatus(Constant.PAY_RESULT_DOING);
			
			if("FAIL".equals(result.get("return_code"))){
				balanceRecord.setStatus(Constant.PAY_RESULT_FAILED);
				balanceRecordDao.save(balanceRecord);
				return getFailResultMap(result.get("return_msg"));
			}
			if(!"OUT_TRADE_NO_USED".equals(result.get("err_code"))){
				String payId = result.get("prepay_id");//微信支付id
				balanceRecord.setPayOrder(payId);
				balanceRecordDao.save(balanceRecord);
			}
		}
		return getSuccessResultMap("正在支付中！");
	}
	
	/**
	 * 充值后的回调 所有设置的都是元 没有分
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
				BalanceRecord record = balanceRecordDao.selectByOrderId(orderNumber);
				MemberCard card = memberCardDao.find(record.getMemberId());
				//要关联商户号 记得
				TenPayClient payClient = new TenPayClient(card.getBusId());
				Map<String, String> tenOrder = payClient.queryOrder(orderNumber);
				//查询订单验证订单支付状态
				if(null != tenOrder.get("trade_state") && "SUCCESS".equals(tenOrder.get("trade_state"))){
					//这是成功的情况
					record.setStatus(Constant.PAY_RESULT_SUCCESS);
					balanceRecordDao.update(record);//更新一下状态
					//调整用户的余额
					BigDecimal b = card.getBalance();//原有的余额
					BigDecimal balance = b.add(record.getAmount()).add(record.getGiftAmount());
					card.setBalance(balance);
					memberCardDao.update(card);
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
