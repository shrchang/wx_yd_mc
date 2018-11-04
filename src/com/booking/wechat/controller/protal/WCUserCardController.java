package com.booking.wechat.controller.protal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.booking.wechat.client.model.pay.UnifiedorderBean;
import com.booking.wechat.client.pay.TenPayClient;
import com.booking.wechat.client.pay.TenPaySign;
import com.booking.wechat.client.pay.TenPayUtils;
import com.booking.wechat.persistence.bean.usercard.CardChangeRecords;
import com.booking.wechat.persistence.bean.usercard.PayItems;
import com.booking.wechat.persistence.bean.usercard.UserCard;
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.usercard.CardChangeRecordsDao;
import com.booking.wechat.persistence.service.usercard.PayItemsDao;
import com.booking.wechat.persistence.service.usercard.UserCardDao;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;
import com.booking.wechat.util.NumberUtil;

@Controller
@RequestMapping("/protal/card")
public class WCUserCardController {

	@Autowired
	private CardChangeRecordsDao recordsDao;
	
	@Autowired
	private UserCardDao cardDao;
	
	@Autowired
	private PayItemsDao itemDao;
	
	@Autowired
	private WechatConfigDao wechatDao;
	
	@RequestMapping(value="/{id}/records",method=RequestMethod.GET)
	public String getCardRecords(@PathVariable Long id,Model model){
		List<CardChangeRecords> records = recordsDao.selectByUserCardId(id);
		model.addAttribute("records", records);
		return "wap/cardRecord";
	}
	
	@RequestMapping(value="/{id}/recharge",method=RequestMethod.GET)
	public String recharge(@PathVariable Long id,Model model){
		UserCard card = cardDao.find(id);
		model.addAttribute("card", card);
		List<PayItems> rechargeItems = itemDao.getAll();
		model.addAttribute("rechargeItems", rechargeItems);
		return "wap/cardRecharge";
	}
	
	@RequestMapping(value="/{id}/submitRecharge",method=RequestMethod.POST)
	public @ResponseBody JSONObject submitRecharge(@PathVariable Long id,Long rechargeItem,HttpServletRequest request) throws Exception{
		UserCard card = cardDao.find(id);
		PayItems item = itemDao.find(rechargeItem);

		WechatConfig config = wechatDao.selectByBusId(card.getBusId());
		
		UnifiedorderBean order = new UnifiedorderBean();
		order.setAppid(config.getAppId());
		order.setDevice_info("WEB");
		order.setMch_id(config.getMachId());// 商户号
		order.setNonce_str(TenPayUtils.getCurrTime() + TenPayUtils.buildRandom(18));// 32位随机数 :12位日期+20位随机数
		order.setBody(item.getPayInfo());// 商品简要描述
		BigDecimal totalFee = NumberUtil.multiply(item.getPayMoney(), new BigDecimal(100), 0);
		order.setOut_trade_no(TenPayUtils.buildRandom(8));// 商户订单号
		order.setTotal_fee(totalFee+"");// 根据订单状态设置，未付款的设置预订款，已付款的设置尾款
		order.setSpbill_create_ip(request.getRemoteAddr());// 终端IP
		order.setTime_start(TenPayUtils.getCurrTime());// 交易开始时间
		HashMap<String, Long> attach = new HashMap<String, Long>();
		attach.put("cardId", id);
		attach.put("payItemId", rechargeItem);
		order.setAttach(JSONObject.fromObject(attach).toString());//订单状态
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);
		order.setTime_expire(TenPayUtils.date2String(cal.getTime(),"yyyyMMddHHmmss"));// 交易结束时间（失效时间） 默认当前时间+5分钟
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+path;
		order.setNotify_url(basePath+"/protal/card/notify");// 接收微信支付异步通知回调地址
		order.setTrade_type("JSAPI");// 交易类型
		order.setOpenid(card.getUserId());
		//将参数以a-z排序
		Map<String, String> map = TenPaySign.convertBean(order);
		//将参数通过MD5加密生成签名,注意：key为sign|key的，value为空的不参与签名
		String sign = TenPaySign.createSign(config.getPrivateKey(),map);
		order.setSign(sign);
		
		TenPayClient client = new TenPayClient(card.getBusId());
		Map<String,String> result = client.unifiedorder(order);
		JSONObject json = new JSONObject();
		if("FAIL".equals(result.get("return_code"))){
			json.put("errorMsg", result.get("return_msg"));
			return json;
		}
		//制作签名
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = TenPayUtils.getUnixTime(new Date())+"";
		String packages = "prepay_id="+result.get("prepay_id");
		finalpackage.put("appId", config.getAppId());  
		finalpackage.put("timeStamp", timestamp);  
		finalpackage.put("nonceStr", order.getNonce_str());  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		String finalsign = TenPaySign.createSign(config.getPrivateKey(),finalpackage);
		//返回数据
		json.put("errorMsg", "OK");
		json.put("appId", config.getAppId());  
		json.put("timeStamp", timestamp);  
		json.put("nonceStr", order.getNonce_str());  
		json.put("packageStr", packages);  
		json.put("signType", "MD5");
		json.put("sign", finalsign);
		return json;
	}
	
	@RequestMapping("/notify")
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
        //修改会员卡信息
        try {
			Map<String, String> notifyMap = TenPayUtils.parseXml(sb.toString());
			if("SUCCESS".equals(notifyMap.get("result_code")) && "SUCCESS".equals(notifyMap.get("return_code"))){
				String orderNumber = notifyMap.get("out_trade_no");
				
				String payInfo = notifyMap.get("attach");
				JSONObject payInfoJson = JSONObject.fromObject(payInfo);
				long cardId = payInfoJson.getLong("cardId");
				long payItemId = payInfoJson.getLong("payItemId");
				PayItems item = itemDao.find(payItemId);
				UserCard card = cardDao.find(cardId);
				
				TenPayClient payClient = new TenPayClient(card.getBusId());
				Map<String, String> tenOrder = payClient.queryOrder(orderNumber);
				//查询订单验证订单支付状态
				if(null != tenOrder.get("trade_state") && "SUCCESS".equals(tenOrder.get("trade_state"))){
					
					card.setRecharge(NumberUtil.add(item.getPayMoney(), card.getRecharge(), 0));//充值金额
					card.setTotalAmount(NumberUtil.add(item.getTotalMoney(), card.getTotalAmount(), 0));//总额
					card.setRemainingSum(NumberUtil.add(item.getTotalMoney(), card.getRemainingSum(), 0));//余额
					card.setLastRechargeTime(new Date());
					card.setStatus(UserCard.STATUS_OK);
					cardDao.update(card);
					saveCardChangeRecords(item.getTotalMoney(), CardChangeRecords.TYPE_RECHARGE, card);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.println("success");
	}
	
	/**
	 * 变更记录
	 * @param changeMoney
	 * @param changeType
	 * @param userCard
	 * @param roomId
	 */
	private void saveCardChangeRecords(BigDecimal changeMoney,String changeType,UserCard userCard){
		CardChangeRecords record = new CardChangeRecords();
		record.setChangeDate(new Date());
		record.setChangeMoney(changeMoney);
		record.setChangeType(changeType);
		record.setRemainingSum(userCard.getRemainingSum());
		record.setTotalAmount(userCard.getTotalAmount());
		record.setUserCardId(userCard.getId());
		record.setRecordDesc("微信支付");
		recordsDao.save(record);
	}
}
