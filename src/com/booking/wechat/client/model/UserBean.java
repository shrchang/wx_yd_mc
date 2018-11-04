package com.booking.wechat.client.model;

import java.util.List;

/**
 * 微信的用户返回信息：http://mp.weixin.qq.com/wiki/14/bb5031008f1494a59c6f71fa0f319c66.html
 * @author Luoxx
 *
 */
public class UserBean {
	private int subscribe;
	private String openid;
	private String nickname;
	private int sex;
	private String city;
	private String country;
	private String province;
	private String language;
	private String headimgurl;
	private String subscribe_time;
	private String unionid;
	private String remark;
	private int groupid;
	private List tagid_list;
	public int getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getSubscribe_time() {
		return subscribe_time;
	}
	public void setSubscribe_time(String subscribeTime) {
		subscribe_time = subscribeTime;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public List getTagid_list() {
		return tagid_list;
	}
	public void setTagid_list(List tagidList) {
		tagid_list = tagidList;
	}
	
}
