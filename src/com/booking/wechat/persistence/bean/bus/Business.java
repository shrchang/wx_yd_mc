package com.booking.wechat.persistence.bean.bus;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name="wcob_business")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Business {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String busName; //商户们
	
	@Column(length=256)
	private String businessLicence; //营业执照
	
	@Column
	private String busPerson; //法人
	
	@Column
	private String identityCard;//法人身份证
	
	@Column
	private String phoneNumber; //联系电话
	
	@Column
	private String publicAccount;
	
	@Column
	private String bankType;
	
	@Column
	private String busProvince;
	
	@Column
	private String busCity;
	
	@Column
	private String busCounty;
	
	@Column(length=512)
	private String busAddress;
	
	@Column
	private Integer maxShop;
	
	@Column
	private Integer maxRoom;
	
	@Column
	private Date createTime;
	
	@Column
	private Date lastUpdateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getBusinessLicence() {
		return businessLicence;
	}

	public void setBusinessLicence(String businessLicence) {
		this.businessLicence = businessLicence;
	}

	public String getBusPerson() {
		return busPerson;
	}

	public void setBusPerson(String busPerson) {
		this.busPerson = busPerson;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPublicAccount() {
		return publicAccount;
	}

	public void setPublicAccount(String publicAccount) {
		this.publicAccount = publicAccount;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getBusProvince() {
		return busProvince;
	}

	public void setBusProvince(String busProvince) {
		this.busProvince = busProvince;
	}

	public String getBusCity() {
		return busCity;
	}

	public void setBusCity(String busCity) {
		this.busCity = busCity;
	}

	public String getBusCounty() {
		return busCounty;
	}

	public void setBusCounty(String busCounty) {
		this.busCounty = busCounty;
	}

	public String getBusAddress() {
		return busAddress;
	}

	public void setBusAddress(String busAddress) {
		this.busAddress = busAddress;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public int getMaxShop() {
		return maxShop;
	}

	public void setMaxShop(Integer maxShop) {
		this.maxShop = maxShop;
	}

	public int getMaxRoom() {
		return maxRoom;
	}

	public void setMaxRoom(Integer maxRoom) {
		this.maxRoom = maxRoom;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
}
