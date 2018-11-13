package com.booking.wechat.persistence.bean.share;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 分享应答部分 一个人也可以一个页面应答多次
 * @ClassName ShareResponse
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年11月13日 下午9:53:27
 *
 */
@Entity
@Table(name="wcob_share_request")
public class ShareResponse {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 分享请求id
	 */
	@Column
	private Long requestId;
	
	/**
	 * 用户id 唯一的
	 */
	@Column
	private String openId;
	
	/**
	 * 用户名
	 */
	@Column
	private String userName;
	
	/**
	 * 浏览次数
	 */
	@Column
	private Integer count=1;
	
	/**
	 * 创建时间
	 */
	@Column
	private Date createTime;
	
	/**
	 * 最后的一次浏览时间
	 */
	@Column
	private Date lastUpdateTime;

	public Long getId() {
		return id;
	}

	public Long getRequestId() {
		return requestId;
	}

	public String getOpenId() {
		return openId;
	}

	public String getUserName() {
		return userName;
	}

	public Integer getCount() {
		return count;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
