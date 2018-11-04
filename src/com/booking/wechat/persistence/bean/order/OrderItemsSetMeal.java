package com.booking.wechat.persistence.bean.order;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 订单详情关联的套餐 怕会修改套餐的价格 这里面把所有的东西都保存到这里面
 * @ClassName OrderItemsSetMeal
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月31日 上午10:29:36
 *
 */
@Entity
@Table(name = "wcob_order_items_setmeal")
public class OrderItemsSetMeal {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private Long itemId;
	
	@Column
	private Long orderId;
	
	@Column
	private Long setMealId;
	
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

	public Long getItemId() {
		return itemId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getSetMealId() {
		return setMealId;
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

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setSetMealId(Long setMealId) {
		this.setMealId = setMealId;
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
