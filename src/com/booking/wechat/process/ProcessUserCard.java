package com.booking.wechat.process;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.booking.wechat.client.model.UserBean;
import com.booking.wechat.client.user.UserClient;
import com.booking.wechat.controller.wechat.WechatRequestModel;
import com.booking.wechat.message.req.TextMessage;
import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.bean.usercard.UserCard;
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.base.DaoFactory;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.persistence.service.constant.impl.ConstantDaoImpl;
import com.booking.wechat.persistence.service.usercard.UserCardDao;
import com.booking.wechat.persistence.service.usercard.impl.UserCardDaoImpl;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;
import com.booking.wechat.persistence.service.wechat.impl.WechatConfigDaoImpl;
import com.booking.wechat.util.MessageUtil;

public class ProcessUserCard {
	
	
	private UserCardDao cardDao = DaoFactory.getDao(UserCardDaoImpl.class);
	
	private ConstantDao constantDao = DaoFactory.getDao(ConstantDaoImpl.class);
	
	private WechatConfigDao wechatConfigDao = DaoFactory.getDao(WechatConfigDaoImpl.class);
	/**
	 * 成为会员
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String createUserCard(WechatRequestModel model) throws Exception{
		Map<String, String> map = model.getParams();
		List<UserCard> userCards = cardDao.selectByUserId(map.get("FromUserName"));
		TextMessage result = new TextMessage();
		result.setCreateTime(new Date().getTime());
		result.setFromUserName(map.get("ToUserName"));
		result.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		result.setToUserName(map.get("FromUserName"));
		
		Constant constant = constantDao.findByConstantId(null,"WEB_SITE");
		
		String webSite = constant.getConstantValue();
		
		if(null != userCards && userCards.size()>0){
			String msg = getCardMessage(userCards.get(0), true);
			result.setContent(msg);
		}else{
			UserCard card = new UserCard();
			SimpleDateFormat format = new SimpleDateFormat("MMddHHmmssSSS");
			String cardNumber = format.format(new Date());
			
			card.setCardNumber(cardNumber);
			card.setRecharge(new BigDecimal(0));
			card.setRemainingSum(new BigDecimal(0));
			card.setStatus(UserCard.STATUS_AFFIRM);
			card.setTotalAmount(new BigDecimal(0));
			card.setUserId(map.get("FromUserName"));
			card.setCardDesc("申请会员");
			card.setCreateTime(new Date());
			
			WechatConfig config = wechatConfigDao.selectBySystemCode(model.getSystemCode());
			UserClient userClient = new UserClient(config.getBusId());
			
			UserBean bean = userClient.getUserInfo(map.get("FromUserName"));
			card.setUserName(bean.getNickname());
			card.setBusId(config.getBusId());
			card.setBusName(config.getBusName());
			cardDao.save(card);
			result.setContent("申请会员成功！请联系客服进行充值，多充多送。\n\n卡号："+cardNumber+"\n状态：待审核\n<a href='"+webSite+"/protal/card/"+card.getId()+"/recharge'>充值请戳我</a>");
		}
		return MessageUtil.textMessageToXml(result);
	}
	
	public String getSimpleDateFormatter(Date date){
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
	
	/**
	 * 查询会员信息
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public String queryUserCard(WechatRequestModel model) throws Exception{
		Map<String, String> map = model.getParams();
		List<UserCard> userCards = cardDao.selectByUserId(map.get("FromUserName"));
		TextMessage result = new TextMessage();
		result.setCreateTime(new Date().getTime());
		result.setFromUserName(map.get("ToUserName"));
		result.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		result.setToUserName(map.get("FromUserName"));
		if(null != userCards && userCards.size()>0){
			String msg = getCardMessage(userCards.get(0), false);
			result.setContent(msg);
		}else{
			WechatConfig config = wechatConfigDao.selectBySystemCode(model.getSystemCode());
			result.setContent("你还不是"+config.getBusName()+"的会员哟！马上申请吧！");
		}
		return MessageUtil.textMessageToXml(result);
	}
	
	private String getCardMessage(UserCard userCard,boolean create) throws Exception{
		StringBuffer sb = new StringBuffer();
		if(create){
			sb.append("你已经是"+userCard.getBusName()+"的会员了。\n\n");
		}
		sb.append("卡号："+userCard.getCardNumber());
		sb.append("\n余额："+userCard.getRemainingSum());
		
		Constant constant = constantDao.findByConstantId(null,"WEB_SITE");
		String webSite = constant.getConstantValue();
		
		if(null != userCard.getLastPayTime()){
			sb.append("\n最后消费时间："+getSimpleDateFormatter(userCard.getLastPayTime()));
		}
		sb.append("\n消费记录：<a href='"+webSite+"/protal/card/"+userCard.getId()+"/records'>查看消费记录</a>");
		sb.append("\n状态："+("OK".equals(userCard.getStatus())?"正常":"待审核"));
		sb.append("\n<a href='"+webSite+"/protal/card/"+userCard.getId()+"/recharge'>充值请戳我</a>");
		return sb.toString();
	}
}
