package com.booking.wechat.controller.wechat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;
import com.booking.wechat.process.WeixinProcess;
import com.booking.wechat.util.MessageUtil;
import com.booking.wechat.util.SignUtil;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/protal")
public class WechatProtalController {

	public WechatProtalController() {
		super();
	}

	@Autowired
	private WechatConfigDao configDao;
	/**
	 * 微信服务器认证
	 */
	@RequestMapping(value="/{systemCode}",method=RequestMethod.GET)
	public @ResponseBody String validate(@PathVariable String systemCode,WechatRequestModel model){
		WechatConfig config = configDao.selectBySystemCode(systemCode);
		if(null == config){
			return "error";
		}
		if(StringUtil.isEmpty(model.getSignature())){
			return "error";
		}
		if (SignUtil.checkSignature(config.getToken(),model)) {
			return model.getEchostr();
		}
		return "error";
	}
	
	/**
	 * 处理微信服务器发送的消息
	 * @throws Exception 
	 */
	@RequestMapping(value="/{systemCode}",method=RequestMethod.POST,produces="application/xml; charset=utf-8")
	public @ResponseBody String doWechatPost(@PathVariable String systemCode,WechatRequestModel model,HttpServletRequest request) throws Exception {
		model.setSystemCode(systemCode);
		WechatConfig config = configDao.selectBySystemCode(systemCode);
		if(null == config){
			return "error";
		}
		if (SignUtil.checkSignature(config.getToken(),model)) {
			 WeixinProcess process = new WeixinProcess();
			 Map<String, String> params = MessageUtil.parseXml(request);
			 model.setParams(params);
			 String xml = process.processRequest(model);
			 return xml;
		}
		return "error";
	}

}
