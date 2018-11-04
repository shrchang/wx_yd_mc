package com.booking.wechat.persistence.service.room;

import com.booking.wechat.persistence.bean.room.RoomAddedRelation;
import com.booking.wechat.persistence.service.base.DAO;

public interface RoomAddedRelationDao extends DAO<RoomAddedRelation> {

	public boolean deleteByRoomId(Long roomId);
}
