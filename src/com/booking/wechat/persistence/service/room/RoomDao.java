package com.booking.wechat.persistence.service.room;

import java.util.List;
import java.util.Map;

import com.booking.wechat.persistence.bean.room.Room;
import com.booking.wechat.persistence.service.base.DAO;

public interface RoomDao extends DAO<Room>{
	
	public Room selectRoomByName(String roomName);
	
	public List<Room> findRoomByBus(Long busId);
	
	public List<Room> findRoomByShop(Long shopId);
	
	public List<Room> findRoomByUser();
	
	public void deleteByShopId(Long shopId);
	
	public void updateBusName(Long busId,String busName) ;
	
	public void updateShopName(Long shopId,String shopName) ;
	
	
	/**
	 * 根据商铺id查询场地的信息
	 * @author shrChang.Liu
	 * @param shopId
	 * @return
	 * @date 2018年10月29日 下午3:40:46
	 * @return List<Object[]>
	 * @description
	 */
	public List<Map> findRoomsByShopId(Long shopId);
}
