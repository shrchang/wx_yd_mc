package com.booking.wechat.client.kf;


import java.security.MessageDigest;

import net.sf.json.JSONObject;

import com.booking.wechat.client.BaseClient;
import com.booking.wechat.restlet.HttpClientUtils;
import com.booking.wechat.token.WeixinToken;

public class CustomerServiceClient extends BaseClient{

	public CustomerServiceClient(Long busId) {
		super(busId);
	}

	public void createCustomeService() throws Exception{
		String createCsUrl = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token="+WeixinToken.getAccessToken(config).getAccess_token();
		JSONObject json = new JSONObject();
		json.put("kf_account", "plpljhb@gh_78176e32200e");
		json.put("nickname", "噼里啪啦聚会吧");
		json.put("password", encoderByMd5("123456"));//MD5 32位加密
		String result = HttpClientUtils.httpPost(createCsUrl, json.toString());
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
	
	public void getCustomeServiceList(){
		String url = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token="+WeixinToken.getAccessToken(config).getAccess_token();
		String result = HttpClientUtils.httpGet(url);
		System.out.println(result);
	}
	
	public String sendMessage(String toUser,String text) throws Exception{
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+WeixinToken.getAccessToken(config).getAccess_token();
		JSONObject json = new JSONObject();
		json.put("touser", toUser);
		json.put("msgtype", "text");
		JSONObject content = new JSONObject();
		content.put("content", text);
		json.put("text", content);
		String result = HttpClientUtils.httpPost(url, json.toString());
		return result;
	}
	
}
