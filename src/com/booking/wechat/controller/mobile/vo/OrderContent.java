package com.booking.wechat.controller.mobile.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单内容
 * @ClassName OrderContent
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月31日 上午10:47:17
 *
 */
public class OrderContent {
	
	private Long busId;//商户id

	private String busName;//商户名称

	private Long shopId;//商铺id

	private String shopName;//商铺名称
	
	private Long couponActivityId;//优惠活动的id
	
	private String couponActivityName;//优惠活动的名称
	
	private String phoneNumber;//手机号码
	
	private String recommend;//推荐人卡号 也是手机号码 这个暂时是没有用的
	
	//这个是用于微信转发的时候 带在后面的转发那个的人弄出来的链接 个人觉得应该绑定他的会员号 因为这个地方需要去查询会员并且分红的
	private String openId;//这个是推荐微信任的openId 可能存在也可能不存在注意
	 
	private List<OrderItem> items = new ArrayList<OrderItem>();//具体的订单
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public Long getBusId() {
		return busId;
	}

	public String getBusName() {
		return busName;
	}

	public Long getShopId() {
		return shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public Long getCouponActivityId() {
		return couponActivityId;
	}

	public String getCouponActivityName() {
		return couponActivityName;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void setCouponActivityId(Long couponActivityId) {
		this.couponActivityId = couponActivityId;
	}

	public void setCouponActivityName(String couponActivityName) {
		this.couponActivityName = couponActivityName;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
	
}
