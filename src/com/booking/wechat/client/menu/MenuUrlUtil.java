package com.booking.wechat.client.menu;

import java.net.URLEncoder;

import com.booking.wechat.client.BaseClient;

public class MenuUrlUtil extends BaseClient{


	public MenuUrlUtil(Long busId) {
		super(busId);
	}

	/**
	 * 只能获取openId，不需要用户确认
	 */
	public static String SCOPE_BASE = "snsapi_base";
	
	/**
	 * 获取详细信息，需要用户确认
	 */
	public static String SCOPE_INFO = "snsapi_userinfo";
	
	/**
	 * 自定义菜单的链接
	 * @param url 
	 * @param scope  snsapi_base
	 * @return
	 */
	public String getCodeRequest(String url, String scope) {
		String result = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=";
		result +=urlEnodeUTF8(config.getAppId());
		result += "&redirect_uri="+urlEnodeUTF8(url)+"&response_type=code&scope="+scope+"&state=STATE#wechat_redirect";
		return result;
	}

	private static String urlEnodeUTF8(String str) {
		String result = str;
		try {
			result = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
