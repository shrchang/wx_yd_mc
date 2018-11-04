package com.booking.wechat.persistence.bean.room;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.booking.wechat.controller.mobile.vo.RoomDateVO;

/**
 * 场地
 * @author Luoxx
 *
 */
@Entity
@Table(name="wcob_room")
public class Room {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private Long busId;
	
	@Column
	private String busName;
	
	@Column
	private Long shopId;
	
	@Column
	private String shopName;
	
	@Column
	private String roomName;
	
	@Column
	private String roomDesc;
	
	@Column
	private Integer minPerson;
	
	@Column
	private Integer maxPerson;
	
	@Transient
	private BigDecimal maxPrice; //最大价格
	
	@Transient
	private BigDecimal minPrice; //最小价格
	
	@Transient
	private String timeOne;
	
	@Transient
	private String timeTwo;
	
	@Transient
	private String timeThree;
	
	@Transient
	private Double bookingPriceRate; //预定价格比率
	
	@Transient
	private java.util.List<RoomPicture> pictures = new ArrayList<RoomPicture>();//场地的图片
	
	@Transient
	private java.util.List<RoomDateVO> roomDateVOs = new ArrayList<RoomDateVO>();//场地的订场时间配置
	
	public java.util.List<RoomDateVO> getRoomDateVOs() {
		return roomDateVOs;
	}

	public void setRoomDateVOs(java.util.List<RoomDateVO> roomDateVOs) {
		this.roomDateVOs = roomDateVOs;
	}

	public java.util.List<RoomPicture> getPictures() {
		return pictures;
	}

	public void setPictures(java.util.List<RoomPicture> pictures) {
		this.pictures = pictures;
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

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public Integer getMinPerson() {
		return minPerson;
	}

	public void setMinPerson(Integer minPerson) {
		this.minPerson = minPerson;
	}

	public Integer getMaxPerson() {
		return maxPerson;
	}

	public void setMaxPerson(Integer maxPerson) {
		this.maxPerson = maxPerson;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getTimeOne() {
		return timeOne;
	}

	public void setTimeOne(String timeOne) {
		this.timeOne = timeOne;
	}

	public String getTimeTwo() {
		return timeTwo;
	}

	public void setTimeTwo(String timeTwo) {
		this.timeTwo = timeTwo;
	}

	public String getTimeThree() {
		return timeThree;
	}

	public void setTimeThree(String timeThree) {
		this.timeThree = timeThree;
	}

	public Double getBookingPriceRate() {
		return bookingPriceRate;
	}

	public void setBookingPriceRate(Double bookingPriceRate) {
		this.bookingPriceRate = bookingPriceRate;
	}
}
