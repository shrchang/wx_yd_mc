package com.booking.wechat.client.model.button;

/**
 * 查询菜单的详情类
 * @author Luoxx
 *
 */
public class GetMenuInfo {

	private String type;
	private String name;
	private String url;
	private String key;
	private GetMenuInfo[] sub_button;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public GetMenuInfo[] getSub_button() {
		return sub_button;
	}
	public void setSub_button(GetMenuInfo[] subButton) {
		sub_button = subButton;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
}
