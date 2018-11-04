package com.booking.wechat.persistence.bean.member;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 积分使用记录，签到积分、消费积分、积分抵扣
 * @ClassName PointRecord
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月26日 下午2:32:44
 *
 */
@Entity
@Table(name="wcob_point_record")
public class PointRecord {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 会员卡号
	 */
	@Column
	private String memberCard;
	
	/**
	 * 会员id
	 */
	@Column
	private String memberId;
	
	/**
	 * 微信用户标识
	 */
	@Column
	private String openId;
	
	/**
	 * 订单号 只有当添加消费积分 消费抵现的时候才有 积分易物到时可能需要关联具体的订单
	 * 1 签到记录是没有订单号的 只有时间 如果是有签到 一天只能签到一次 注意 这个时间建议使用服务器的时间去比较 不然会出现很多的问题
	 */
	@Column
	private String orderId;
	
	/**
	 * 类型 1 消费积分 2 签到积分 3 积分抵现 4积分易物
	 */
	@Column
	private String type;
	
	/**
	 * 积分数量 如果是1 2 的时候正数 其余是负数
	 */
	@Column
	private BigDecimal ponitNum;
	
	/**
	 * 创建时间
	 */
	@Column
	private Date createDate;
	
	/**
	 * 状态 0 失败 1成功
	 */
	@Column
	private String status;

	public Long getId() {
		return id;
	}

	public String getMemberCard() {
		return memberCard;
	}

	public String getMemberId() {
		return memberId;
	}

	public String getOpenId() {
		return openId;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getType() {
		return type;
	}

	public BigDecimal getPonitNum() {
		return ponitNum;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getStatus() {
		return status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMemberCard(String memberCard) {
		this.memberCard = memberCard;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPonitNum(BigDecimal ponitNum) {
		this.ponitNum = ponitNum;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
