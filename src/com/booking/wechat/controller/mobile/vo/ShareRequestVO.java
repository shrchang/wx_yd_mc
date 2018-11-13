package com.booking.wechat.controller.mobile.vo;

/**
 * 分享配置返回实体
 * @ClassName ShareRequestVO
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年11月13日 下午10:45:55
 *
 */
public class ShareRequestVO {
	
	private Long shareId;
	
	private JssdkConfig config;

	public Long getShareId() {
		return shareId;
	}

	public JssdkConfig getConfig() {
		return config;
	}

	public void setShareId(Long shareId) {
		this.shareId = shareId;
	}

	public void setConfig(JssdkConfig config) {
		this.config = config;
	}
}
