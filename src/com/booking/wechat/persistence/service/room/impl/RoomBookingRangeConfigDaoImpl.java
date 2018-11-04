package com.booking.wechat.persistence.service.room.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.room.RoomBookingRangeConfig;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.room.RoomBookingRangeConfigDao;

@Service
public class RoomBookingRangeConfigDaoImpl  extends DaoSupport<RoomBookingRangeConfig> implements RoomBookingRangeConfigDao{

	
	public List<RoomBookingRangeConfig> selectByRoomId(Long roomId) {
		RoomBookingRangeConfig params = new RoomBookingRangeConfig();
        params.setRoomId(roomId);
        LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
        orderby.put("week", "asc");
        orderby.put("timeRange", "asc");
        List<RoomBookingRangeConfig> list = findByExample(params,true,-1,-1,orderby).getRows();
        return list;  
	}

	
	public List<RoomBookingRangeConfig> selectByRoomId(Long roomId, int week) {
		RoomBookingRangeConfig params = new RoomBookingRangeConfig();
        params.setRoomId(roomId);
        params.setWeek(week);
        LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
        orderby.put("week", "asc");
        orderby.put("timeRange", "asc");
        List<RoomBookingRangeConfig> list = findByExample(params,true,-1,-1,orderby).getRows();
        return list;  
	}

	
	public void deleteByBusId(Long busId) {
		batchUpdate("delete from "+this.getEntityName(RoomBookingRangeConfig.class) +" where busId="+busId);
	}

	
	public void deleteByRoomId(Long roomId) {
		batchUpdate("delete from "+this.getEntityName(RoomBookingRangeConfig.class) +" where roomId="+roomId);
	}

	
	public void deleteByShopId(Long shopId) {
		batchUpdate("delete from "+this.getEntityName(RoomBookingRangeConfig.class) +" where shopId="+shopId);
	}

	
	public void updateRoomName(Long roomId, String roomName) {
		batchUpdate("update "+getEntityName(RoomBookingRangeConfig.class)+" set roomName='"+roomName+"' where roomId="+roomId);
	}
}
