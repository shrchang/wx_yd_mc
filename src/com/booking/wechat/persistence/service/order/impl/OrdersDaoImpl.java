package com.booking.wechat.persistence.service.order.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.order.OrderPayItems;
import com.booking.wechat.persistence.bean.order.Orders;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.order.OrdersDao;
import com.booking.wechat.util.ListUtils;

@Service
public class OrdersDaoImpl extends DaoSupport<Orders> implements OrdersDao {
	
	public List<Orders> selectOrdersByRoomId(Long roomId) {
        List<Orders> list = super.findByCustomJpql("from "+getEntityName(Orders.class)+" where roomId="+roomId+" and status<>'CANCELLED' and status<>'DELETED'");
        return list;  
	}
	
	public List<Orders> selectByRoomAdded(Long roomAddedId, String reserveDate,String reserveTime) {
		String sql = "select o from "+getEntityName(Orders.class)+" o where o.roomAddedId=?1 and o.reserveDate=?2 and o.reserveTime=?3 and o.status<>'CANCELLED' and o.status<>'DELETED'";
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(reserveDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Orders> list = super.findByCustomJpql(sql,new Object[]{roomAddedId,date,reserveTime});
        return list;  
	}

	public List<Orders> selectByAdmin() {
        List<Orders> list = findByCustomJpql("from "+getEntityName(Orders.class)+" where status<>'DELETED' order by orderDate desc");
        
        return list;
	}

	public List<Orders> checkOrders(Long roomId, String reserveDate,String reserveTime) {
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(reserveDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        String sql ="FROM "+getEntityName(Orders.class)+" where roomId=?1 and reserveDate=?2 and reserveTime=?3 and status<>'CANCELLED' and status<>'DELETED'";
        List<Orders> list = findByCustomJpql(sql, new Object[]{roomId, date, reserveTime});
        return list;
	}

	public boolean updateSwiftNumber(Long id, String swiftNumber) {
		
		String sql = "update "+getEntityName(Orders.class)+" set swiftNumber="+swiftNumber+" where id="+id;
		
		batchUpdate(sql);
		
		return true;
	}

	public List<Orders> selectByStatus(String status) {
        Orders params = new Orders();
        params.setStatus(status);
        List<Orders> list = findByExample(params,true).getRows();
        return list;  
	}

	public List<Orders> selectByOpenId(String openId) {
//		LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
//        orderby.put("orderDate", "desc");
//        Orders params = new Orders();
//        params.setUserToken(openId);
        String hql = "from "+getEntityName(Orders.class)+" t where t.userToken=?1 or t.id in (select item.orderId from "+getEntityName(OrderPayItems.class)+" item where item.payForAnotherId=?2) and status<>'DELETED' order by t.orderDate desc";
        List<Orders> list = findByCustomJpql(hql, new Object[]{openId,openId});
        return list; 
	}

	@Deprecated
	public List<Orders> selectByCard(String cardNumber) {
        LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
        orderby.put("orderDate", "desc");
        Orders params = new Orders();
//        params.setUserCardNumber(cardNumber);
        List<Orders> list = findByExample(params,true,-1,-1,orderby).getRows();
        return list; 
	}

	public Orders selectByOrderNumber(String orderNumber) {
        Orders params = new Orders();
        params.setOrderNumber(orderNumber);
        List<Orders> list = findByExample(params,true).getRows();
        if(ListUtils.isNotEmpty(list)){
        	return list.get(0);
        }
        return null; 
	}
	
	
	
	public void updateBusName(Long busId,String busName) {
		batchUpdate("update "+getEntityName(Orders.class)+" set busName='"+busName+"' where busId="+busId);
	}
	
	
	public void updateShopName(Long shopId,String shopName){
		batchUpdate("update "+getEntityName(Orders.class)+" set shopName='"+shopName+"' where shopId="+shopId);
	}

}
