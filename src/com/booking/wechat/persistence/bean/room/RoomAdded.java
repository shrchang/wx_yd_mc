package com.booking.wechat.persistence.bean.room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 附加场地
 * @author Luoxx
 */
@Entity
@Table(name="wcob_room_added")
public class RoomAdded {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private Long busId;
	
	@Column
	private Long shopId;
	
	@Column
	private String addedName;
	
	@Column
	private String addedDesc;
	
	@Column
	private Integer addedMinPerson; //附加场地最小人数
	
	@Column
	private Integer addedMaxPerson; //附加场地最大人数

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddedName() {
		return addedName;
	}

	public void setAddedName(String addedName) {
		this.addedName = addedName;
	}

	public String getAddedDesc() {
		return addedDesc;
	}

	public void setAddedDesc(String addedDesc) {
		this.addedDesc = addedDesc;
	}

	public Integer getAddedMinPerson() {
		return addedMinPerson;
	}

	public void setAddedMinPerson(Integer addedMinPerson) {
		this.addedMinPerson = addedMinPerson;
	}

	public Integer getAddedMaxPerson() {
		return addedMaxPerson;
	}

	public void setAddedMaxPerson(Integer addedMaxPerson) {
		this.addedMaxPerson = addedMaxPerson;
	}

	public Long getBusId() {
		return busId;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	
}
