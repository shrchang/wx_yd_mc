package com.booking.wechat.controller.protal.vo;

import java.math.BigDecimal;
import java.util.List;

public class RoomInfoVO {

	private Long roomId;
	
	private String roomName;
	
	private BigDecimal  discount;
	
	private List<RoomConfigVO> roomConfigs;

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public List<RoomConfigVO> getRoomConfigs() {
		return roomConfigs;
	}

	public void setRoomConfigs(List<RoomConfigVO> roomConfigs) {
		this.roomConfigs = roomConfigs;
	}
}
