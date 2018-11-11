package com.booking.wechat.persistence.bean.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 订单
 * 
 * @author Luoxx
 * 
 */
@Entity
@Table(name = "wcob_orders")
public class Orders {

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
	private Date orderDate;//下单时间
	
	@Column
	private String roomNames;
	
	@Column
	private String bookingDates;//预定日期
	
	@Column(scale = 0)
	private BigDecimal orderMoney; // 定金
	
	@Column(scale = 0)
	private BigDecimal roomPrice; // 场地价格
	
	@Column
	private String userToken;

	@Column
	private String userName;

	@Column
	private String orderType; // 订单类型：普通订单、代付订单 如果是代付订单?

	@Column
	private String status; // 未付款、已付款、已消费、已取消

	@Column
	private String orderNumber; // 订单编号

	@Column(scale = 0)
	private BigDecimal beforeRoomPrice; // 未打折的价格

	@Column(scale = 0)
	private BigDecimal otherPrice;// 其他消费：例如啤酒 这个到时可以使用增值服务

	@Column(length = 256)
	private String orderDesc;

	@Column
	private String recommend;// 推荐人卡号

	@Column
	private String phoneNumber;// 预定手机号
	
	@Column
	private Long couponActivityId; // 是否有选择优惠活动
	
	@Column
	private String couponActivityName;
	
	@Column
	private String shareOpenId;//分享人的微信唯一标识
	

	@Transient
	public static final String UNPAID = "UNPAID";// 未付款

	@Transient
	public static final String PAID = "PAID";// 已付款
	
	@Transient
	public static final String AFFIRM = "AFFIRM"; // 已付款，并提交流水号后待确认
	
	@Transient
	public static final String CANCELLED = "CANCELLED";// 已取消
	
	@Transient
	public static final String FINISH = "FINISH";// 已消费
	
	@Transient
	public static final String DELETED = "DELETED";// 已删除

	@Transient
	private List<OrderPayItems> payItems;
	
	@Transient
	private List<OrderItems> orderItems;
	
	@Transient
	private String timeOut;//超时时间
	
	public String getShareOpenId() {
		return shareOpenId;
	}

	public void setShareOpenId(String shareOpenId) {
		this.shareOpenId = shareOpenId;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getRoomPrice() {
		return roomPrice;
	}

	public void setRoomPrice(BigDecimal roomPrice) {
		this.roomPrice = roomPrice;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public BigDecimal getBeforeRoomPrice() {
		return beforeRoomPrice;
	}

	public void setBeforeRoomPrice(BigDecimal beforeRoomPrice) {
		this.beforeRoomPrice = beforeRoomPrice;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Orders) {
			Orders eq = (Orders) obj;
			if (this.getId().longValue() == eq.getId().longValue()) {
				return true;
			}
		}
		return false;
	}

	public BigDecimal getOtherPrice() {
		return otherPrice;
	}

	public void setOtherPrice(BigDecimal otherPrice) {
		this.otherPrice = otherPrice;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<OrderPayItems> getPayItems() {
		return payItems;
	}

	public void setPayItems(List<OrderPayItems> payItems) {
		this.payItems = payItems;
	}

	public List<OrderItems> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItems> orderItems) {
		this.orderItems = orderItems;
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

	public String getRoomNames() {
		return roomNames;
	}

	public void setRoomNames(String roomNames) {
		this.roomNames = roomNames;
	}

	public String getBookingDates() {
		return bookingDates;
	}

	public void setBookingDates(String bookingDates) {
		this.bookingDates = bookingDates;
	}

}
