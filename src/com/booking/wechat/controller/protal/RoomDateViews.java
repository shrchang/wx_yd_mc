package com.booking.wechat.controller.protal;

import java.util.List;

public class RoomDateViews {

	private Long roomId;
	
	private List<RoomDateView> views;

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public List<RoomDateView> getViews() {
		return views;
	}

	public void setViews(List<RoomDateView> views) {
		this.views = views;
	}
}
