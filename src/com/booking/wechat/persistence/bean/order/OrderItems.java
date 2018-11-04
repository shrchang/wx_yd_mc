package com.booking.wechat.persistence.bean.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "wcob_order_items")
public class OrderItems {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private Long orderId;
	
	@Column
	private Long busId;
	
	@Column
	private Long shopId;
	
	@Column
	private String shopName;
	
	@Column
	private Long roomId;
	
	@Column
	private String roomName;
	
	@Column
	private Long roomAddedId; // 是否有附加场地
	
	@Column
	private String roomAddedName;
	
	@Column
	private Date reserveDate; //预定日期
	
	@Column
	private String reserveTime; //预定时间
	
	@Column(scale = 2)
	private BigDecimal roomPrice; //场地单价
	
	@Column(scale = 2)
	private BigDecimal bookingPrice; //定金
	
	@Column
	private Long couponActivityId; // 是否有选择优惠活动
	
	@Column
	private String couponActivityName;
	
	@Column
	private String timeRange; // one two three
	
	@Transient
	private String personRange;
	
	@Transient
	private List<OrderItemsSetMeal> meals = new ArrayList<OrderItemsSetMeal>();
	
	public List<OrderItemsSetMeal> getMeals() {
		return meals;
	}

	public void setMeals(List<OrderItemsSetMeal> meals) {
		this.meals = meals;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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

	public Long getRoomAddedId() {
		return roomAddedId;
	}

	public void setRoomAddedId(Long roomAddedId) {
		this.roomAddedId = roomAddedId;
	}

	public String getRoomAddedName() {
		return roomAddedName;
	}

	public void setRoomAddedName(String roomAddedName) {
		this.roomAddedName = roomAddedName;
	}

	public Date getReserveDate() {
		return reserveDate;
	}

	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}

	public String getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(String reserveTime) {
		this.reserveTime = reserveTime;
	}

	public BigDecimal getRoomPrice() {
		return roomPrice;
	}

	public void setRoomPrice(BigDecimal roomPrice) {
		this.roomPrice = roomPrice;
	}

	public BigDecimal getBookingPrice() {
		return bookingPrice;
	}

	public void setBookingPrice(BigDecimal bookingPrice) {
		this.bookingPrice = bookingPrice;
	}

	public Long getCouponActivityId() {
		return couponActivityId;
	}

	public void setCouponActivityId(Long couponActivityId) {
		this.couponActivityId = couponActivityId;
	}

	public String getCouponActivityName() {
		return couponActivityName;
	}

	public void setCouponActivityName(String couponActivityName) {
		this.couponActivityName = couponActivityName;
	}

	public String getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getPersonRange() {
		return personRange;
	}

	public void setPersonRange(String personRange) {
		this.personRange = personRange;
	}

	public Long getBusId() {
		return busId;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	} 
}
