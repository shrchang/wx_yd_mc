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
 * 会员卡变更记录
 * @author Luoxx
 *
 */
@Entity
@Table(name="wcob_card_change_records")
public class CardChangeRecords {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@Column
	private Long userCardId;
	
	
	@Column
	private Date changeDate;;
	
	
	@Column
	private String changeType; //充值、消费、月结赠送
	
	
	@Column
	private BigDecimal changeMoney;
	
	//变更后总金额
	
	@Column
	private BigDecimal totalAmount;
	
	//变更后余额
	
	@Column
	private BigDecimal remainingSum;
	
	@Column
	private String status;
	
	@Column
	private String payToken; //微信支付token
	
	@Column
	private String recordDesc;
	
	@Column
	private String roomName;
	
	@Transient
	public static final String TYPE_RECHARGE = "RECHARGE"; //充值
	
	@Transient
	public static final String TYPE_CONSUME = "CONSUME";//消费
	
	@Transient
	public static final String TYPE_MONTH_GRANT = "MONTH_GRANT";//月结赠送

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserCardId() {
		return userCardId;
	}

	public void setUserCardId(Long userCardId) {
		this.userCardId = userCardId;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public BigDecimal getChangeMoney() {
		return changeMoney;
	}

	public void setChangeMoney(BigDecimal changeMoney) {
		this.changeMoney = changeMoney;
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

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayToken() {
		return payToken;
	}

	public void setPayToken(String payToken) {
		this.payToken = payToken;
	}

	public String getRecordDesc() {
		return recordDesc;
	}

	public void setRecordDesc(String recordDesc) {
		this.recordDesc = recordDesc;
	}
}
