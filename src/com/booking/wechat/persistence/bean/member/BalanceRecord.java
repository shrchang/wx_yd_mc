package com.booking.wechat.persistence.bean.member;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.booking.wechat.constant.Constant;

/**
 * 余额变动的记录，包含在线支付/余额支付/余额充值/代付？ 反正有钱的都是在这里可以找到记录 跟会员有关系的 可能会有退款的可能性  
 * 这里面还要区分是不是预定和尾款的操作
 * @ClassName BalanceRecord
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月26日 下午2:13:58
 *
 */
@Entity
@Table(name="wcob_balance_record")
public class BalanceRecord {

	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 是否是尾款支付  默认不是
	 */
	@Column
	private Boolean isFinalPayment=false;
	
	/**
	 * 类型 充值 在线支付 代付 余额付 这个地方可能需要斟酌一下
	 * {@link Constant}
	 */
	@Column
	private String type;
	
	/**
	 * 订单号 充值的时候是自定义的id 也不可以为空 需要通过这个去关联
	 */
	@Column
	private String orderId;
	
	/**
	 * 金额 当充值的时候为正 其余的都是负数 注意
	 */
	@Column
	private BigDecimal amount;
	
	/**
	 * 赠送金额 只有充值的时候存在这个 这个默认是0
	 */
	@Column
	private BigDecimal giftAmount=new BigDecimal("0.00");
	
	/**
	 * 会员卡号
	 */
	@Column
	private String memberCard;
	
	
	/**
	 * 会员id
	 */
	@Column
	private Long memberId;
	
	/**
	 * 微信的id
	 */
	@Column
	private String openId;
	
	/**
	 * 来源 渠道  1微信 默认 2支付宝 3其他 只有当充值的时候 这个字段才有
	 */
	@Column
	private String source;
	
	/**
	 * 是否是线下 默认是否 线下的意思就是我们平台帮忙调整的
	 */
	@Column
	private Boolean isUnderLine=false;
	
	/**
	 * 支付凭证 微信支付或者支付宝支付应该留下的凭证或者第三方的流水号
	 */
	@Column
	private String payOrder;
	
	/**
	 * 创建时间
	 */
	@Column
	private Date createDate;
	
	/**
	 * 状态 {@link Constant}
	 */
	@Column
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getOrderId() {
		return orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getGiftAmount() {
		return giftAmount;
	}

	public String getMemberCard() {
		return memberCard;
	}

	public Long getMemberId() {
		return memberId;
	}

	public String getOpenId() {
		return openId;
	}

	public String getSource() {
		return source;
	}

	public String getPayOrder() {
		return payOrder;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setGiftAmount(BigDecimal giftAmount) {
		this.giftAmount = giftAmount;
	}

	public void setMemberCard(String memberCard) {
		this.memberCard = memberCard;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setPayOrder(String payOrder) {
		this.payOrder = payOrder;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Boolean getIsUnderLine() {
		return isUnderLine;
	}

	public void setIsUnderLine(Boolean isUnderLine) {
		this.isUnderLine = isUnderLine;
	}

	public Boolean getIsFinalPayment() {
		return isFinalPayment;
	}

	public void setIsFinalPayment(Boolean isFinalPayment) {
		this.isFinalPayment = isFinalPayment;
	}
}
