package com.booking.wechat.persistence.bean.member;

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

/**
 * 新的会员体系
 * 
 * @ClassName MemberCard
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月26日 上午10:59:00
 *
 */
@Entity
@Table(name = "wcob_member_card")
public class MemberCard {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 用户名 默认是微信名称 可修改用于显示
	 */
	@Column
	private String userName;

	/**
	 * 用户名 微信名称
	 */
	@Column
	private String nickName;

	/**
	 * 微信openId
	 */
	@Column
	private String openId;

	/**
	 * 手机号码
	 */
	@Column
	private String phoneNo;

	/**
	 * 商户id
	 */
	@Column
	private Long busId;

	/**
	 * 会员卡号
	 */
	@Column
	private String cardNo;

	/**
	 * 余额
	 */
	@Column
	private BigDecimal balance;

	/**
	 * 可用积分 积分默认应该是消费积分 目前，后面可能会出现签到积分
	 */
	@Column
	private BigDecimal point;

	/**
	 * 即将过期的积分 积分的可用周期， 1.按照京东来算是每年的1月1号清零 所以这个字段可以默认不填 2还有一种可能
	 * 就是按照积分生成记录按周期计算有效期 如1月1号产生的积分在3月1号后过期 有效期2个月，需要提示用户当前这一周或者一个月即将过期的积分数量
	 */
	@Column
	private BigDecimal duePoint;

	/**
	 * 性别
	 */
	@Column
	private Integer sex;

	/**
	 * 城市
	 */
	@Column
	private String city;

	/**
	 * 省份
	 */
	@Column
	private String province;

	/**
	 * 国家
	 */
	@Column
	private String country;

	/**
	 * 头像路径
	 */
	@Column
	private String headImgurl;

	/**
	 * 状态 
	 */
	@Column
	private String status;

	/**
	 * 级别 0游客 1普通 2V1 3V2 4V3 这个是按照充值还是消费来算？？？ 需要讨论 个人觉得应该是消费
	 */
	@Column
	private Integer level;

	/**
	 * 生日 1990-04-05 可用于生日折扣 后续 需要界面上自己填写的 也可以默认是空
	 */
	@Temporal(TemporalType.DATE)
	@Column
	private Date birthDay;
	
	/**
	 * 创建时间
	 */
	@Column
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	@Column
	private Date modifyTime;
	

	/**
	 * 商户名称
	 */
	@Transient
	private String busName;
	@Transient
	public static final String OK = "ok";//好的
	@Transient
	public static final String ERROR = "error";//错误的

	
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getNickName() {
		return nickName;
	}

	public String getOpenId() {
		return openId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public Long getBusId() {
		return busId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public Integer getSex() {
		return sex;
	}

	public String getCity() {
		return city;
	}

	public String getProvince() {
		return province;
	}

	public String getCountry() {
		return country;
	}

	public String getHeadImgurl() {
		return headImgurl;
	}

	public String getStatus() {
		return status;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public String getBusName() {
		return busName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setHeadImgurl(String headImgurl) {
		this.headImgurl = headImgurl;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public BigDecimal getPoint() {
		return point;
	}

	public BigDecimal getDuePoint() {
		return duePoint;
	}

	public Integer getLevel() {
		return level;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setPoint(BigDecimal point) {
		this.point = point;
	}

	public void setDuePoint(BigDecimal duePoint) {
		this.duePoint = duePoint;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
