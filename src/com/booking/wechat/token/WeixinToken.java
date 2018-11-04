package com.booking.wechat.token;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.restlet.HttpClientUtils;

/**
 * 获取微信授权的token
 * @author Luoxx
 *
 */
public class WeixinToken {

	private static Logger log = Logger.getLogger(WeixinToken.class);  
	
	private static Map<String,AccessToken> accessTokenMap = null;
	
	public static AccessToken getAccessToken(WechatConfig config){
		String accessTokenJson = null;
		String url_AccessToken = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+config.getAppId()+"&secret="+config.getSecret();
		AccessToken accessToken = null;
		try {
			
			if(null != accessTokenMap && null != accessTokenMap.get(config.getSystemCode()) && accessTokenMap.get(config.getSystemCode()).getEnd_time()>System.currentTimeMillis()){
				
				return accessTokenMap.get(config.getSystemCode());
				
			}
			
			accessTokenJson = HttpClientUtils.httpGet(url_AccessToken);
			
			log.info("get wechat api access_token："+accessTokenJson);
			
			JSONObject jsonObject = JSONObject.fromObject(accessTokenJson);
			
			accessToken = (AccessToken) JSONObject.toBean(jsonObject, AccessToken.class);;
			
			Calendar cal = Calendar.getInstance();
			
			cal.add(Calendar.SECOND, (accessToken.getExpires_in()-200));
			
			accessToken.setEnd_time(cal.getTimeInMillis());
			if(null == accessTokenMap){
				accessTokenMap = new HashMap<String, AccessToken>();
			}
			accessTokenMap.put(config.getSystemCode(), accessToken);
			
		} catch (Exception e) {
			log.error(e);
		}
		
		return accessToken;
	}
}
