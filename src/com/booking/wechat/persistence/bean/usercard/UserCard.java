package com.booking.wechat.persistence.bean.usercard;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 会员卡
 * @author Luoxx
 *
 */
@Entity
@Table(name="wcob_user_card")
public class UserCard {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private Long busId;
	
	@Column
	private String busName;
	
	@Column
	private String cardNumber;  //VIP-DATE
	
	//微信名称
	@Column
	private String userName;
	
	//微信的openid
	@Column
	private String userId;
	
	//充值金额
	@Column
	private BigDecimal recharge;
	
	//实际金额
	@Column
	private BigDecimal totalAmount;
	
	//余额
	@Column
	private BigDecimal remainingSum;
	
	
	@Column
	private String status;
	
	//开卡时间
	@Column
	private Date createTime;
	
	//最后消费时间
	@Column
	private Date lastPayTime;
	
	//最后充值时间
	@Column
	private Date lastRechargeTime;
	
	@Column
	private String cardDesc;
	
	@Transient
	public static final String STATUS_OK = "OK"; //正常
	
	@Transient
	public static final String STATUS_AFFIRM = "AFFIRM";//待审核

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getRecharge() {
		return recharge;
	}

	public void setRecharge(BigDecimal recharge) {
		this.recharge = recharge;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getRemainingSum() {
		return remainingSum;
	}

	public void setRemainingSum(BigDecimal remainingSum) {
		this.remainingSum = remainingSum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCardDesc() {
		return cardDesc;
	}

	public void setCardDesc(String cardDesc) {
		this.cardDesc = cardDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Date getLastPayTime() {
		return lastPayTime;
	}

	public void setLastPayTime(Date lastPayTime) {
		this.lastPayTime = lastPayTime;
	}

	public Date getLastRechargeTime() {
		return lastRechargeTime;
	}

	public void setLastRechargeTime(Date lastRechargeTime) {
		this.lastRechargeTime = lastRechargeTime;
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
