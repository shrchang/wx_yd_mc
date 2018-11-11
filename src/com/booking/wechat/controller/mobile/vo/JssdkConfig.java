package com.booking.wechat.controller.mobile.vo;

/**
 * jssdk需要用到的配置文件
 * @ClassName JssdkConfig
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年11月11日 下午9:08:42
 *
 */
public class JssdkConfig {
	
	private String appId;
	
	private String jsapi_ticket;
	
	private String nonceStr;
	
	private String timestamp;
	
	private String signature;
	
	private String url;

	public String getAppId() {
		return appId;
	}

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getSignature() {
		return signature;
	}

	public String getUrl() {
		return url;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
