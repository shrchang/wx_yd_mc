package com.booking.wechat.persistence.service.room.impl;

import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.room.Room;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.room.RoomDao;
import com.booking.wechat.util.ListUtils;
@Service
public class RoomDaoImpl extends DaoSupport<Room> implements RoomDao{


	public Room selectRoomByName(String roomName) {
        Room param = new Room();
        param.setRoomName(roomName);
        List<Room> list = this.findByExample(param,true).getRows();
        if(ListUtils.isNotEmpty(list)){
        	return list.get(0);
        }
        return null;
	}

	
	public List<Room> findRoomByBus(Long busId) {
		Room param = new Room();
        param.setBusId(busId);
        List<Room> list = this.findByExample(param,true).getRows();
		return list;
	}

	public List<Room> findRoomByShop(Long shopId) {
		Room param = new Room();
        param.setShopId(shopId);
        List<Room> list = this.findByExample(param,true).getRows();
		return list;
	}

	public List<Room> findRoomByUser() {
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		Room params = new Room();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			params.setBusId(user.getBusId());
		}
		List<Room> list = findByExample(params,true).getRows();
		return list;
	}

	public void deleteByShopId(Long shopId) {
		batchUpdate("delete from "+this.getEntityName(Room.class) +" where shopId="+shopId);
	}
	
	public void updateBusName(Long busId,String busName) {
		batchUpdate("update "+getEntityName(Room.class)+" set busName='"+busName+"' where busId="+busId);
	}
	
	public void updateShopName(Long shopId,String shopName){
		batchUpdate("update "+getEntityName(Room.class)+" set shopName='"+shopName+"' where shopId="+shopId);
	}


	public List<Map> findRoomsByShopId(Long shopId) {
		String sql = "select a.*,MAX(b.roomPrice) as maxPrice,MIN(b.roomPrice) as minPrice from wcob_room a "
				+ "INNER  JOIN wcob_room_booking_range_config b on b.roomId=a.id "
				+ "where a.shopId="+shopId+" "
				+ "GROUP BY a.id";
		return queryByNativeSQL(sql);
	}
}
