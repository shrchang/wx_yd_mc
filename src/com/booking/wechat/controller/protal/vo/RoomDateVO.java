package com.booking.wechat.controller.protal.vo;

import java.util.Date;
import java.util.List;

/**
 * 组装预定时间的内容 这里按照对应的时间配置来进行预定操作的 可配置的
 * @ClassName RoomDateVO
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月30日 上午10:31:12
 *
 */
public class RoomDateVO {

	private Date bookingDate;
	
	private Integer week;
	
	private String weekStr;
	
	private Long busId;
	
	private Long shopId;
	
	private String shopName;
	
	private List<RoomInfoVO> roomInfo;

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public List<RoomInfoVO> getRoomInfo() {
		return roomInfo;
	}

	public void setRoomInfo(List<RoomInfoVO> roomInfo) {
		this.roomInfo = roomInfo;
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
