package com.booking.wechat.persistence.bean.coupon;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 优惠活动
 * 
 * @author Luoxx
 * 
 */
@Entity
@Table(name = "wcob_coupon_activity")
public class CouponActivity {

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
	private String couponName;

	@Column
	private String couponType; // 优惠类型：现金、折扣、周期(提前minDay天--maxDay天优惠折扣)

	@Column
	private BigDecimal freeMoney; // 免费金额

	@Column
	private BigDecimal discount; // 折扣

	@Column
	private String status; // 状态

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")  
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")  
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@Column
	private String couponDesc;
	
	@Column
	private Integer beforeDay;//提前预定天数
	
	@Transient
	public static final String CASH_TYPE = "CASH"; //现金优惠
	@Transient
	public static final String DISCOUNT_TYPE = "DISCOUNT";//折扣优惠
	@Transient
	public static final String PERIOD_TYPE = "PERIOD";//周期优惠
	
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
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public BigDecimal getFreeMoney() {
		return freeMoney;
	}
	public void setFreeMoney(BigDecimal freeMoney) {
		this.freeMoney = freeMoney;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getCouponDesc() {
		return couponDesc;
	}
	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
	}
	public Integer getBeforeDay() {
		return beforeDay;
	}
	public void setBeforeDay(Integer beforeDay) {
		this.beforeDay = beforeDay;
	}
}
