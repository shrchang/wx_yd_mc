package com.booking.wechat.controller.protal.vo;

import java.util.Date;

import com.booking.wechat.util.DateUtils;

/**
 * 默认七天预定
 * @author xixi_
 *
 */
public class BookingDateVO {

	private Long time;
	
	private int week;
	
	private String weekStr;
	
	private Date date;

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public String getWeekStr() {
		switch (week) {
		case 1:
			weekStr = "周一"; 
			break;
		case 2:
			weekStr = "周二"; 
			break;
		case 3:
			weekStr = "周三"; 
			break;
		case 4:
			weekStr = "周四"; 
			break;
		case 5:
			weekStr = "周五"; 
			break;
		case 6:
			weekStr = "周六"; 
			break;
		case 0:
			weekStr = "周日"; 
			break;
		default:
			break;
		}
		if(DateUtils.compareDate(date, new Date())){
			return "今天";
		}
		return weekStr;
	}

	public void setWeekStr(String weekStr) {
		this.weekStr = weekStr;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
