package com.booking.wechat.controller.mobile.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.booking.wechat.persistence.bean.setmeal.SetMeal;

/**
 * 订单明细
 * @ClassName OrderContent
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月31日 上午9:22:59
 *
 */
public class OrderItem {

	private String roomName;

	private Long roomId;

	private String bookingDate;// 预订日期

	private Integer week;// 预订的周

	private String rangeId;// 预订的那个配置id

	private String timeRange;// 预订的时间段

	private BigDecimal price;// 预订的价格

	private BigDecimal bookingPriceRate;// 预订的比率

	private List<SetMeal> meals = new ArrayList<SetMeal>();// 增值套餐吧
	
	public String getRoomName() {
		return roomName;
	}

	public Long getRoomId() {
		return roomId;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public Integer getWeek() {
		return week;
	}

	public String getRangeId() {
		return rangeId;
	}

	public String getTimeRange() {
		return timeRange;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getBookingPriceRate() {
		return bookingPriceRate;
	}

	public List<SetMeal> getMeals() {
		return meals;
	}
	
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public void setRangeId(String rangeId) {
		this.rangeId = rangeId;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setBookingPriceRate(BigDecimal bookingPriceRate) {
		this.bookingPriceRate = bookingPriceRate;
	}

	public void setMeals(List<SetMeal> meals) {
		this.meals = meals;
	}

}
