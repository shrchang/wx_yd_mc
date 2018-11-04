package com.booking.wechat.persistence.bean.menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信菜单表
 * @author Luoxx
 *
 */
@Entity
@Table(name="wcob_wechat_menu")
public class WechatMenu {

	@Id
	@Column(name="menu_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long menuId;
	
	@Column(name="bus_id")
	private Long busId;
	
	@Column(name="bus_name",length=32)
	private String busName;
	
	@Column(name="parent_id")
	private Long parentId;
	
	@Column(name="menu_name",length=32)
	private String name;
	
	@Column(name="menu_url",length=256)	
	private String url;
	
	@Column(name="menu_type",length=32)	
	private String type;
	
	@Column(name="menu_key",length=128)	
	private String key;
	
	@Column(name="order_number",length=128)	
	private String orderNumber;
	
	@Column(name="material_media_id",length=128)	
	private String materialMediaId;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getMaterialMediaId() {
		return materialMediaId;
	}

	public void setMaterialMediaId(String materialMediaId) {
		this.materialMediaId = materialMediaId;
	}

	public Long getBusId() {
		return busId;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}
	
}
