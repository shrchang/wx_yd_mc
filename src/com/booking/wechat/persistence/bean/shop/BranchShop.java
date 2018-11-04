package com.booking.wechat.persistence.bean.shop;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.booking.wechat.persistence.bean.room.Room;

/**
 * 店铺表
 * @author xixi_
 *
 */
@Entity
@Table(name="wcob_branch_shop")
public class BranchShop {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private Long busId;
	
	@Column
	private String busName;
	
	@Column
	private String shopName;
	
	@Column
	private String shopProvince;
	
	@Column
	private String shopCity;
	
	@Column
	private String shopCounty;
	
	@Column(length=512)
	private String shopAddress;
	
	@Column(length=512)
	private String shopDesc;
	
	@Transient
	private List<Room> rooms;
	
	@Transient
    private List<Map> roomInfos;//详细的

	public List<Map> getRoomInfos() {
		return roomInfos;
	}

	public void setRoomInfos(List<Map> roomInfos) {
		this.roomInfos = roomInfos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBusId() {
		return busId;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopProvince() {
		return shopProvince;
	}

	public void setShopProvince(String shopProvince) {
		this.shopProvince = shopProvince;
	}

	public String getShopCity() {
		return shopCity;
	}

	public void setShopCity(String shopCity) {
		this.shopCity = shopCity;
	}

	public String getShopCounty() {
		return shopCounty;
	}

	public void setShopCounty(String shopCounty) {
		this.shopCounty = shopCounty;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getShopDesc() {
		return shopDesc;
	}

	public void setShopDesc(String shopDesc) {
		this.shopDesc = shopDesc;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

}
