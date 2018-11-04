package com.booking.wechat.process;

import java.util.List;
import java.util.Map;

import com.booking.wechat.controller.wechat.WechatRequestModel;
import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.bean.faq.FaqList;
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.base.DaoFactory;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.persistence.service.constant.impl.ConstantDaoImpl;
import com.booking.wechat.persistence.service.faq.FaqListDao;
import com.booking.wechat.persistence.service.faq.impl.FaqListDaoImpl;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;
import com.booking.wechat.persistence.service.wechat.impl.WechatConfigDaoImpl;
import com.booking.wechat.util.ListUtils;
import com.booking.wechat.util.MessageUtil;
import com.booking.wechat.util.StringUtil;

public class WeixinProcess {

	private ConstantDao constantDao = DaoFactory.getDao(ConstantDaoImpl.class);
	
	private WechatConfigDao wechatConfigDao = DaoFactory.getDao(WechatConfigDaoImpl.class);
	
	public String processRequest(WechatRequestModel model) throws Exception{
		
		Map<String, String> map = model.getParams();
		
		String msgType = map.get("MsgType");
		/**
		 * 用户的openId
		 */
		String fromUserName = map.get("FromUserName"); 
		/**
		 * 公众号ID
		 */
		String toUserName = map.get("ToUserName");
		
		String content = map.get("Content");
		
		if(MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)){
			String faq = returnFaqList(model.getSystemCode(),content);
			if(StringUtil.isEmpty(faq)){
				return returnKf(fromUserName, toUserName);
			}else{
				return MessageUtil.sendTextMsg(fromUserName, toUserName, faq);
			}
		}
		if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)){
			
			String eventType = map.get("Event");
			
			if(MessageUtil.EVENT_TYPE_CLICK.equals(eventType)){
				
				String eventKey = map.get("EventKey");
				
				if("wdzl".equals(eventKey)){//我的资料
					return new ProcessUserCard().queryUserCard(model);
				}
				if("cwhy".equals(eventKey)){//成为会员
					return new ProcessUserCard().createUserCard(model);
				}
				if("wyzx".equals(eventKey)){//我要咨询
					String faq = returnFaqList(model.getSystemCode());
					if(StringUtil.isEmpty(faq)){
						faq = "请输入需要咨询的问题，稍后客服将会为您解答！";
					}else{
						faq +="请回复编码获取更多信息！";
					}
					return MessageUtil.sendTextMsg(fromUserName, toUserName, faq);
				}
			}
			if(MessageUtil.EVENT_TYPE_SUBSCRIBE.equals(eventType)){
				WechatConfig config = wechatConfigDao.selectBySystemCode(model.getSystemCode());
				Constant constant = constantDao.findByConstantId(config.getBusId(),"SUBSCRIBE");
				return MessageUtil.sendTextMsg(fromUserName, toUserName, constant.getConstantValue().replace("\\n", "\n"));
			}
		}
		return "";
	}
	
	private FaqListDao faqDao = DaoFactory.getDao(FaqListDaoImpl.class);
	
	private String returnFaqList(String systemCode){
		WechatConfig config = wechatConfigDao.selectBySystemCode(systemCode);
		FaqList entity = new FaqList();
		entity.setParentId(0l);
		entity.setBusId(config.getBusId());
		List<FaqList> list = faqDao.findByExample(entity,true).getRows();
		StringBuffer sb = new StringBuffer();
		for (FaqList faqList : list) {
			sb.append(faqList.getFaqNumber()+"："+faqList.getFaqType()+"\n");
		}
		return sb.toString();
	}
	
	private String returnFaqList(String systemCode,String faqNumber){
		WechatConfig config = wechatConfigDao.selectBySystemCode(systemCode);
		FaqList entity = new FaqList();
		entity.setFaqNumber(faqNumber);
		entity.setBusId(config.getBusId());
		List<FaqList> list = faqDao.findByExample(entity,true).getRows();
		if(ListUtils.isEmpty(list)){
			return "";
		}
		FaqList faq = list.get(0);
		
		FaqList child = new FaqList();
		child.setParentId(faq.getId());
		List<FaqList> childList = faqDao.findByExample(child,true).getRows();
		if(ListUtils.isEmpty(childList)){
			return faq.getFaqContext();
		}else if(childList.size()==1){
			return childList.get(0).getFaqContext();
		}else{
			StringBuffer sb = new StringBuffer();
			for (FaqList faqList : childList) {
				sb.append(faqList.getFaqNumber()+"："+faqList.getFaqTitle()+"\n");
			}
			return sb.toString();
		}
	}
	
	private String returnKf(String toUser,String fromUser){
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA["+toUser+"]]></ToUserName>");
		sb.append("<FromUserName><![CDATA["+fromUser+"]]></FromUserName>");
		sb.append("<CreateTime>"+System.currentTimeMillis()+"</CreateTime>");
		sb.append("<MsgType><![CDATA[transfer_customer_service]]></MsgType>");
		sb.append("</xml>");
		return sb.toString();
	}
	
}
