package com.booking.wechat.persistence.bean.room;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wcob_room_config")
public class RoomConfig {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	
	@Column
	private Long busId;
	
	@Column
	private Long shopId;

	@Column
	private Long roomId;
	
	@Column
	private String roomName;
	
	@Column
	private Integer week; // 星期几 （1-7）

	@Column
	private String timeOne;// 预订时间范围
	
	@Column
	private String timeTwo;
	
	@Column
	private String timeThree;

	@Column
	private BigDecimal timeOnePrice;// 场地价格

	@Column
	private BigDecimal timeTwoPrice;
	
	@Column
	private BigDecimal timeThreePrice;
	
	@Column
	private BigDecimal timeOneAddedPrice;// 附加场地价格

	@Column
	private BigDecimal timeTwoAddedPrice;
	
	@Column
	private BigDecimal timeThreeAddedPrice;

	@Column
	private Double bookingPriceRate; // 预订定金比率

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
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

	public BigDecimal getTimeOnePrice() {
		return timeOnePrice;
	}

	public void setTimeOnePrice(BigDecimal timeOnePrice) {
		this.timeOnePrice = timeOnePrice;
	}

	public BigDecimal getTimeTwoPrice() {
		return timeTwoPrice;
	}

	public void setTimeTwoPrice(BigDecimal timeTwoPrice) {
		this.timeTwoPrice = timeTwoPrice;
	}

	public BigDecimal getTimeThreePrice() {
		return timeThreePrice;
	}

	public void setTimeThreePrice(BigDecimal timeThreePrice) {
		this.timeThreePrice = timeThreePrice;
	}

	public BigDecimal getTimeOneAddedPrice() {
		return timeOneAddedPrice;
	}

	public void setTimeOneAddedPrice(BigDecimal timeOneAddedPrice) {
		this.timeOneAddedPrice = timeOneAddedPrice;
	}

	public BigDecimal getTimeTwoAddedPrice() {
		return timeTwoAddedPrice;
	}

	public void setTimeTwoAddedPrice(BigDecimal timeTwoAddedPrice) {
		this.timeTwoAddedPrice = timeTwoAddedPrice;
	}

	public BigDecimal getTimeThreeAddedPrice() {
		return timeThreeAddedPrice;
	}

	public void setTimeThreeAddedPrice(BigDecimal timeThreeAddedPrice) {
		this.timeThreeAddedPrice = timeThreeAddedPrice;
	}

	public Double getBookingPriceRate() {
		return bookingPriceRate;
	}

	public void setBookingPriceRate(Double bookingPriceRate) {
		this.bookingPriceRate = bookingPriceRate;
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

}
