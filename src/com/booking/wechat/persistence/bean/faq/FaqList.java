package com.booking.wechat.persistence.bean.faq;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="wcob_faq_list")
public class FaqList {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private Long busId;
	
	@Column
	private String busName;
	
	@Column
	private String faqNumber; //问题编码
	
	@Column
	private Long parentId; //问题上级菜单
	
	@Column
	private String faqType; //问题分类
	
	@Column
	private String faqTitle;  //问题标题
	
	@Column(length=1024)
	private String faqContext;  //问题内容

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFaqNumber() {
		return faqNumber;
	}

	public void setFaqNumber(String faqNumber) {
		this.faqNumber = faqNumber;
	}


	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getFaqType() {
		return faqType;
	}

	public void setFaqType(String faqType) {
		this.faqType = faqType;
	}

	public String getFaqTitle() {
		return faqTitle;
	}

	public void setFaqTitle(String faqTitle) {
		this.faqTitle = faqTitle;
	}

	public String getFaqContext() {
		return faqContext;
	}

	public void setFaqContext(String faqContext) {
		this.faqContext = faqContext;
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
