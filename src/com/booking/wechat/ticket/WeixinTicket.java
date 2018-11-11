package com.booking.wechat.ticket;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.booking.wechat.restlet.HttpClientUtils;
import com.booking.wechat.token.AccessToken;

import net.sf.json.JSONObject;

/**
 * 获取微信JS票据
 * @ClassName WeixinTicket
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年11月11日 上午10:57:18
 *
 */
public class WeixinTicket {
private static Logger log = Logger.getLogger(WeixinTicket.class);  
	
	private static Map<String,JsapiTickect> jsApiTicketMap = null;
	
	/**
	 * 获取票据 用于调用分享接口使用的
	 * @author shrChang.Liu
	 * @param config
	 * @return
	 * @date 2018年11月11日 上午10:57:53
	 * @return AccessToken
	 * @description
	 */
	public static JsapiTickect getJsApiTicket(AccessToken token){
		String jsapiTickectJson = null;
		String url_AccessToken = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+token.getAccess_token()+"&type=jsapi";
		JsapiTickect accessToken = null;
		try {
			
			if(null != jsApiTicketMap && null != jsApiTicketMap.get(token.getAccess_token()) && jsApiTicketMap.get(token.getAccess_token()).getEnd_time()>System.currentTimeMillis()){
				
				return jsApiTicketMap.get(token.getAccess_token());
				
			}
			
			jsapiTickectJson = HttpClientUtils.httpGet(url_AccessToken);
			
			log.info("get wechat api jsapiTickect："+jsapiTickectJson);
			
			JSONObject jsonObject = JSONObject.fromObject(jsapiTickectJson);
			
			accessToken = (JsapiTickect) JSONObject.toBean(jsonObject, JsapiTickect.class);;
			
			Calendar cal = Calendar.getInstance();
			
			cal.add(Calendar.SECOND, (accessToken.getExpires_in()-200));
			
			accessToken.setEnd_time(cal.getTimeInMillis());
			if(null == jsApiTicketMap){
				jsApiTicketMap = new HashMap<String, JsapiTickect>();
			}
			jsApiTicketMap.put(token.getAccess_token(), accessToken);
			
		} catch (Exception e) {
			log.error(e);
		}
		
		return accessToken;
	}
	
	public static void main(String[] args) {
		AccessToken accessToken = new AccessToken();
		accessToken.setAccess_token("15_T3lC5b4eblvG1NnaUetviM7N-u5-ggGBofsXtQKBFj8AFdVdFQHIzR6f8AnjSOTjJ8ncu_s9Vf-UaxDIEEvnNroe8Z7fvu5PyKGF4CVUJAHquGtBj1oTJDp-5hkLW3stb7xJivnqFXxS87geOVEbACASZI");
		getJsApiTicket(accessToken);
	}
}
