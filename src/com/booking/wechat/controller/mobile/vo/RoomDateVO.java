package com.booking.wechat.controller.mobile.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 固定当前日期往后推迟30天的配置信息
 * 
 * @author xixi_
 *
 */
public class RoomDateVO {

	private Date bookingDate;// 预定的日期

	private Integer week;// 周几

	private String weekStr;// 周几

	private Long busId;// 商户id

	private Long shopId;// 商铺id

	private String shopName;// 商铺名称

	private Long roomId;// 场地id

	private String roomName;// 场地的名称
	
	private List<RoomConfigVO> configVOs;//时间配置

	public List<RoomConfigVO> getConfigVOs() {
		return configVOs;
	}

	public void setConfigVOs(List<RoomConfigVO> configVOs) {
		this.configVOs = configVOs;
	}

	public Long getRoomId() {
		return roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	@JsonFormat(pattern="yyyy-MM-dd")
	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
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
		return weekStr;
	}

	public void setWeekStr(String weekStr) {
		this.weekStr = weekStr;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

}
