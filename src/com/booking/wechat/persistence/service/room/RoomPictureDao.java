package com.booking.wechat.persistence.service.room;

import java.util.List;

import com.booking.wechat.persistence.bean.room.RoomPicture;
import com.booking.wechat.persistence.service.base.DAO;

public interface RoomPictureDao extends DAO<RoomPicture>{

	/**
	 * 根据场地id查询他对应的图片
	 * @author shrChang.Liu
	 * @param roomId
	 * @return
	 * @date 2018年10月17日 上午11:18:30
	 * @return List<RoomPicture>
	 * @description
	 */
	public List<RoomPicture> findPicturesByRoomId(Long roomId);
	
	/**
	 * 根据场地id清楚图片数据
	 * @author shrChang.Liu
	 * @param roomId
	 * @date 2018年10月17日 上午11:21:02
	 * @return void
	 * @description
	 */
	public void deleteByRoomId(Long roomId);
}
