package com.booking.wechat.persistence.service.room;

import java.util.List;

import com.booking.wechat.persistence.bean.room.RoomConfig;
import com.booking.wechat.persistence.service.base.DAO;

public interface RoomConfigDao extends DAO<RoomConfig>{

	
	public void deleteByRoomId(Long roomId);
	
	public List<RoomConfig> selectByRoomId(Long roomId);
	
	public void deleteByShopId(Long shopId);
	
	public void deleteByBusId(Long busId);
	
	public void updateRoomName(Long roomId,String roomName);
	
	public RoomConfig selectByWeek(Long roomId,int week);
}
