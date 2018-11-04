package com.booking.wechat.persistence.service.room.impl;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.room.RoomAddedRelation;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.room.RoomAddedRelationDao;
@Service
public class RoomAddedRelationDaoImpl extends DaoSupport<RoomAddedRelation> implements RoomAddedRelationDao  {

	public boolean deleteByRoomId(Long roomId) {
        batchUpdate("delete from "+this.getEntityName(RoomAddedRelation.class) +" where roomId="+roomId);
		return true;
	}


}
