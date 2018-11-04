package com.booking.wechat.controller.protal;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 场地视图
 * @author luoxx
 *
 */
public class RoomDateView {

	private Date date;
	
	private Integer week;
	
	private String timeOne;// 预订时间范围
	
	private String timeTwo;
	
	private String timeThree;

	private BigDecimal timeOnePrice;// 场地价格

	private BigDecimal timeTwoPrice;
	
	private BigDecimal timeThreePrice;
	
	private Double bookingPriceRate;
	
	private String timeOneStatus; //正常（OK）、已定（SOLD ）、不可定（DISABLE）
	
	private String timeTwoStatus;
	
	private String timeThreeStatus;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public String getTimeOne() {
		return timeOne;
	}

	public void setTimeOne(String timeOne) {
		this.timeOne = timeOne;
	}

	public String getTimeTwo() {
		return timeTwo;
	}

	public void setTimeTwo(String timeTwo) {
		this.timeTwo = timeTwo;
	}

	public String getTimeThree() {
		return timeThree;
	}

	public void setTimeThree(String timeThree) {
		this.timeThree = timeThree;
	}

	public BigDecimal getTimeOnePrice() {
		return timeOnePrice;
	}

	public void setTimeOnePrice(BigDecimal timeOnePrice) {
		this.timeOnePrice = timeOnePrice;
	}

	public BigDecimal getTimeTwoPrice() {
		return timeTwoPrice;
	}

	public void setTimeTwoPrice(BigDecimal timeTwoPrice) {
		this.timeTwoPrice = timeTwoPrice;
	}

	public BigDecimal getTimeThreePrice() {
		return timeThreePrice;
	}

	public void setTimeThreePrice(BigDecimal timeThreePrice) {
		this.timeThreePrice = timeThreePrice;
	}

	public Double getBookingPriceRate() {
		return bookingPriceRate;
	}

	public void setBookingPriceRate(Double bookingPriceRate) {
		this.bookingPriceRate = bookingPriceRate;
	}

	public String getTimeOneStatus() {
		return timeOneStatus;
	}

	public void setTimeOneStatus(String timeOneStatus) {
		this.timeOneStatus = timeOneStatus;
	}

	public String getTimeTwoStatus() {
		return timeTwoStatus;
	}

	public void setTimeTwoStatus(String timeTwoStatus) {
		this.timeTwoStatus = timeTwoStatus;
	}

	public String getTimeThreeStatus() {
		return timeThreeStatus;
	}

	public void setTimeThreeStatus(String timeThreeStatus) {
		this.timeThreeStatus = timeThreeStatus;
	}
}
