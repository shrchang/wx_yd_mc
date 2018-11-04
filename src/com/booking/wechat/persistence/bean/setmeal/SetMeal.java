package com.booking.wechat.persistence.bean.setmeal;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 增值套餐---店铺
 * @ClassName RoomSetMeal
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月17日 下午3:24:10
 *
 */
@Entity
@Table(name="wcob_shop_set_meal")
public class SetMeal {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@Column
	private Long busId;//商户id
	
	@Column
	private String busName;//商户的名称
	
	@Column
	private Long shopId;//店铺id 
	
	@Column
	private String shopName;//店铺的名称
	
	@Column
	private String mealName;//套餐的名称
	
	@Column
	private BigDecimal mealPrice;//套餐的价格
	
	@Column
	@Lob
	private String mealDesc;//套餐描述
	
	@Column
	private String mealSrc;//套餐图片描述路径

	public Long getId() {
		return id;
	}

	public Long getBusId() {
		return busId;
	}

	public String getBusName() {
		return busName;
	}

	public Long getShopId() {
		return shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public String getMealName() {
		return mealName;
	}

	public BigDecimal getMealPrice() {
		return mealPrice;
	}

	public String getMealDesc() {
		return mealDesc;
	}

	public String getMealSrc() {
		return mealSrc;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	public void setMealPrice(BigDecimal mealPrice) {
		this.mealPrice = mealPrice;
	}

	public void setMealDesc(String mealDesc) {
		this.mealDesc = mealDesc;
	}

	public void setMealSrc(String mealSrc) {
		this.mealSrc = mealSrc;
	}
}
