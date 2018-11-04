package com.booking.wechat.persistence.service.room;

import java.util.List;

import com.booking.wechat.persistence.bean.room.RoomBookingRangeConfig;
import com.booking.wechat.persistence.service.base.DAO;

public interface RoomBookingRangeConfigDao extends DAO<RoomBookingRangeConfig>{

	public List<RoomBookingRangeConfig> selectByRoomId(Long roomId);
	
	public List<RoomBookingRangeConfig> selectByRoomId(Long roomId,int week);
	
	public void deleteByRoomId(Long roomId);
	
	public void deleteByShopId(Long shopId);
	
	public void deleteByBusId(Long busId);
	
	public void updateRoomName(Long roomId,String roomName);
}
