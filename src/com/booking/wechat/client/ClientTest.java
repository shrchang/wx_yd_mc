package com.booking.wechat.client;

import java.security.MessageDigest;

import net.sf.json.JSONObject;

import com.booking.wechat.client.model.material.MaterialBean;
import com.booking.wechat.client.model.material.MaterialItemBean;
import com.booking.wechat.client.model.material.MaterialNewsItem;
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.restlet.HttpClientUtils;
import com.booking.wechat.token.WeixinToken;

public class ClientTest {

	public static void main(String[] args) throws Exception {
		WechatConfig config = new WechatConfig();
		config.setAppId("wx0f0a6062e33d800c");
		config.setSecret("3b0c920e36a448552d5406a3383235d1");
		
		ClientTest test = new ClientTest();
		test.getMterialList(config);
	}
	
	public void getMterialList(WechatConfig config) throws Exception{
		String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token="+WeixinToken.getAccessToken(config).getAccess_token();
		JSONObject post = new JSONObject();
		post.put("type", "news");
		post.put("offset", "30");
		post.put("count", "50");
		String result = HttpClientUtils.httpPost(url,post.toString());
		MaterialBean bean = (MaterialBean) JSONObject.toBean(JSONObject.fromObject(result), MaterialBean.class);
		MaterialItemBean[] list = bean.getItem();
		for (MaterialItemBean materialItemBean : list) {
			MaterialNewsItem temp = materialItemBean.getContent().getNews_item()[0];
			System.err.println(temp.getTitle()+"=="+temp.getUrl());
		}
	}
	
	public void createCustomeService(WechatConfig config) throws Exception{
		String createCsUrl = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token="+WeixinToken.getAccessToken(config).getAccess_token();
		JSONObject json = new JSONObject();
		json.put("kf_account", "booking@plpljhb");
		json.put("nickname", "客服测试号");
		json.put("password", encoderByMd5("123456"));//MD5 32位加密
		String result = HttpClientUtils.httpPost(createCsUrl, json.toString());
		System.out.println(result);
	}
	
	public void getCustomeServiceList(WechatConfig config){
		String url = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token="+WeixinToken.getAccessToken(config).getAccess_token();
		String result = HttpClientUtils.httpGet(url);
		System.out.println(result);
	}
	
	public String encoderByMd5(String str) throws Exception {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
}
