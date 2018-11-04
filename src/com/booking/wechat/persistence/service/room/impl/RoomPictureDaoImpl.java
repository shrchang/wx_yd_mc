package com.booking.wechat.persistence.service.room.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.room.RoomPicture;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.room.RoomPictureDao;

@Service
public class RoomPictureDaoImpl extends DaoSupport<RoomPicture> implements RoomPictureDao{

	public List<RoomPicture> findPicturesByRoomId(Long roomId) {
		RoomPicture entity = new RoomPicture();
		entity.setRoomId(roomId);
		return findByExample(entity,true).getRows();
	}

	public void deleteByRoomId(Long roomId) {
		batchUpdate("delete from "+this.getEntityName(RoomPicture.class) +" where roomId="+roomId);
	}
}
