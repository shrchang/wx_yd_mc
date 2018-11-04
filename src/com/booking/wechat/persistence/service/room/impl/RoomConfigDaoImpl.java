package com.booking.wechat.persistence.service.room.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.room.RoomConfig;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.room.RoomConfigDao;
import com.booking.wechat.util.ListUtils;

@Service
public class RoomConfigDaoImpl extends DaoSupport<RoomConfig> implements RoomConfigDao{

	
	public void deleteByRoomId(Long roomId) {
		batchUpdate("delete from "+this.getEntityName(RoomConfig.class) +" where roomId="+roomId);
	}

	
	public List<RoomConfig> selectByRoomId(Long roomId) {
		RoomConfig params = new RoomConfig();
        params.setRoomId(roomId);
        LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
        orderby.put("week", "asc");
        List<RoomConfig> list = findByExample(params,true,-1,-1,orderby).getRows();
        return list;  
	}

	
	public void deleteByBusId(Long busId) {
		batchUpdate("delete from "+this.getEntityName(RoomConfig.class) +" where busId="+busId);
	}

	
	public void deleteByShopId(Long shopId) {
		batchUpdate("delete from "+this.getEntityName(RoomConfig.class) +" where shopId="+shopId);
	}

	public void updateRoomName(Long roomId,String roomName){
		batchUpdate("update "+getEntityName(RoomConfig.class)+" set roomName='"+roomName+"' where roomId="+roomId);
	}

	public RoomConfig selectByWeek(Long roomId, int week) {
		String sql = "from "+getEntityName(RoomConfig.class)+" where roomId=?1 and week=?2)";
		List<RoomConfig> list = findByCustomJpql(sql,new Object[]{roomId,week});
		if(ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
}
