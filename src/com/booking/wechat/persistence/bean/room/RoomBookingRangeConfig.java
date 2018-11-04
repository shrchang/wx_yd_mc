package com.booking.wechat.persistence.bean.room;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 场地预定范围配置
 * @author Luoxx
 *
 */
@Entity
@Table(name="wcob_room_booking_range_config")
public class RoomBookingRangeConfig {

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
	private String shopName;
	
	@Column
	private String timeRange;
	
	@Column
	private Double bookingPriceRate; // 预订定金比率
	
	@Column
	private Integer week; // 星期几 （1-7）
	
	@Column
	private BigDecimal roomPrice;
	
	@Column
	private Double discount;//该时间段的折扣，暂时预留

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

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

	public Double getBookingPriceRate() {
		return bookingPriceRate;
	}

	public void setBookingPriceRate(Double bookingPriceRate) {
		this.bookingPriceRate = bookingPriceRate;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public BigDecimal getRoomPrice() {
		return roomPrice;
	}

	public void setRoomPrice(BigDecimal roomPrice) {
		this.roomPrice = roomPrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
}
