package com.booking.wechat.persistence.bean.constant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "wcob_constant")
public class Constant{
    
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="BUS_ID")
	private Long busId;
	
	@Column(name="BUS_NAME")
	private String busName;
	
    @Column(name = "CONSTANT_GROUP", nullable = false, length = 32)
    private String constantGroup;
    
    @Column(name = "CONSTANT_ID",  nullable = false, length = 32)
    private String constantId;
    
    @Column(name = "CONSTANT_NAME", length = 64)
    private String constantName;
    
    @Column(name = "CONSTANT_VALUE", length = 4096)
    private String constantValue;

    public String getConstantId() {
        return constantId;
    }

    public void setConstantId(String constantId) {
        this.constantId = constantId;
    }

    public String getConstantName() {
        return constantName;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }

    public String getConstantGroup() {
        return constantGroup;
    }

    public void setConstantGroup(String constantGroup) {
        this.constantGroup = constantGroup;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
