package com.booking.wechat.persistence.bean.share;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 分享请求(可以分享多次)
 * @ClassName ShareEntity
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年11月13日 下午9:37:59
 *
 */
@Entity
@Table(name="wcob_share_request")
public class ShareRequest {
	
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 分享的名称（比如场地/场馆/测试/推荐/方案/拼团/砍价/等等）
	 */
	@Column
	private String name;
	
	
	/**
	 * 分享的微信用户openid
	 */
	@Column
	private String openId;
	
	/**
	 * 微信用户的名字
	 */
	@Column
	private String userName;
	
	/**
	 * 分享类型(场馆、场地、推荐页面、方案、拼团、砍价)
	 */
	@Column
	private String type;
	
	/**
	 * 类型对应的资源id如场地id 场馆id 方案id 拼团id 砍价单id 默认为0
	 */
	@Column
	private Long resourceId=0L;
	
	/**
	 * 分享的地址
	 */
	@Column
	private String url;
	
	/**
	 * 分享的次数 这必须是成功的次数
	 */
	@Column
	private Integer count=0;
	
	/**
	 * 创建的时间
	 */
	@Column
	private Date createTime;
	
	/**
	 * 最后分享的时间（个人可以分享多次）
	 */
	@Column
	private Date lastUpdateTime;
	
	/**
	 * 分享的途径 微信用户/朋友圈
	 */
	@Column
	private String source;
	
	/**
	 * 状态 因为有可能会失败的 如果是失败的话
	 */
	@Column
	private String status;
	
	/**
	 * 父的id
	 */
	@Column
	private Long parentId;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getOpenId() {
		return openId;
	}

	public String getUserName() {
		return userName;
	}

	public String getType() {
		return type;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public String getUrl() {
		return url;
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

	public String getSource() {
		return source;
	}

	public String getStatus() {
		return status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public void setSource(String source) {
		this.source = source;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
