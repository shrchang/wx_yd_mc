package com.booking.wechat.persistence.bean.order;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "wcob_order_pay_items")
public class OrderPayItems {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private Long orderId;
	
	@Column
	private String payType;//支付类型：预定支付、付全款
	
	@Column
	private String payMode; //支付方式：会员卡支付、会员卡代付、微信支付、支付宝 
	
	@Column
	private BigDecimal payMoney;//支付金额
	
	@Column
	private String payForAnotherId; // 代付用户的openId

	@Column
	private String payForAnotherName; // 代付用户的名字
	
	@Column
	private String swiftNumber; // 付款的流水号，需要回填

	@Column
	private String prepay_id; // 微信支付ID
	
	@Column
	private String userCardNumber; // 代付会员卡支付账户
	
	@Column
	private String status;  //支付状态
	
	@Transient
	public static final String TYPE_BOOKING = "booking_money"; //预定支付
	
	@Transient
	public static final String TYPE_FINAL = "final_money";  //尾款支付
	
	@Transient
	public static final String MODE_CARD = "card"; //会员卡支付
	
	@Transient
	public static final String MODE_TENAPY = "tenpay";  //微信支付
	
	@Transient
	public static final String MODE_ALIPAY = "alipay"; //支付宝支付
	
	@Transient
	public static final String MODE_REPLACE = "replace";  //会员卡代付

	@Transient
	public static final String STATUS_PAID = "paid"; //已支付
	
	@Transient
	public static final String STATUS_UNPAID = "unpaid";  //未支付
	
	@Transient
	public static final String STATUS_AFFIRM = "affirm";  //待确认--支付宝流水号
	
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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayForAnotherId() {
		return payForAnotherId;
	}

	public void setPayForAnotherId(String payForAnotherId) {
		this.payForAnotherId = payForAnotherId;
	}

	public String getPayForAnotherName() {
		return payForAnotherName;
	}

	public void setPayForAnotherName(String payForAnotherName) {
		this.payForAnotherName = payForAnotherName;
	}

	public String getSwiftNumber() {
		return swiftNumber;
	}

	public void setSwiftNumber(String swiftNumber) {
		this.swiftNumber = swiftNumber;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepayId) {
		prepay_id = prepayId;
	}

	public String getUserCardNumber() {
		return userCardNumber;
	}

	public void setUserCardNumber(String userCardNumber) {
		this.userCardNumber = userCardNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	
}
