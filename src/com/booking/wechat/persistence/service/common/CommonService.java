package com.booking.wechat.persistence.service.common;

import com.booking.wechat.persistence.service.base.DaoFactory;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.persistence.service.constant.impl.ConstantDaoImpl;
import com.booking.wechat.persistence.service.coupon.CouponActivityDao;
import com.booking.wechat.persistence.service.coupon.impl.CouponActivityDaoImpl;
import com.booking.wechat.persistence.service.faq.FaqListDao;
import com.booking.wechat.persistence.service.faq.impl.FaqListDaoImpl;
import com.booking.wechat.persistence.service.menu.WechatMenuDao;
import com.booking.wechat.persistence.service.menu.impl.WechatMenuDaoImpl;
import com.booking.wechat.persistence.service.order.OrdersDao;
import com.booking.wechat.persistence.service.order.impl.OrdersDaoImpl;
import com.booking.wechat.persistence.service.room.RoomBookingRangeConfigDao;
import com.booking.wechat.persistence.service.room.RoomDao;
import com.booking.wechat.persistence.service.room.impl.RoomBookingRangeConfigDaoImpl;
import com.booking.wechat.persistence.service.room.impl.RoomDaoImpl;
import com.booking.wechat.persistence.service.setmeal.SetMealDao;
import com.booking.wechat.persistence.service.setmeal.impl.SetMealDaoImpl;
import com.booking.wechat.persistence.service.shop.BranchShopDao;
import com.booking.wechat.persistence.service.shop.impl.BranchShopDaoImpl;
import com.booking.wechat.persistence.service.user.SysUserDao;
import com.booking.wechat.persistence.service.user.impl.SysUserDaoImpl;
import com.booking.wechat.persistence.service.usercard.PayItemsDao;
import com.booking.wechat.persistence.service.usercard.UserCardDao;
import com.booking.wechat.persistence.service.usercard.impl.PayItemsDaoImpl;
import com.booking.wechat.persistence.service.usercard.impl.UserCardDaoImpl;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;
import com.booking.wechat.persistence.service.wechat.impl.WechatConfigDaoImpl;

public class CommonService {

	private UserCardDao cardDao = DaoFactory.getDao(UserCardDaoImpl.class);
	
	private PayItemsDao itemDao = DaoFactory.getDao(PayItemsDaoImpl.class);
	
	private BranchShopDao shopDao = DaoFactory.getDao(BranchShopDaoImpl.class);
	
	private RoomDao roomDao = DaoFactory.getDao(RoomDaoImpl.class);
	
	private RoomBookingRangeConfigDao configDao = DaoFactory.getDao(RoomBookingRangeConfigDaoImpl.class);
	
	private CouponActivityDao couponDao = DaoFactory.getDao(CouponActivityDaoImpl.class);
	
	private WechatConfigDao wechatDao = DaoFactory.getDao(WechatConfigDaoImpl.class);
	
	private ConstantDao constantDao = DaoFactory.getDao(ConstantDaoImpl.class);
	
	private SysUserDao userDao = DaoFactory.getDao(SysUserDaoImpl.class);
	
	private WechatMenuDao  menuDao = DaoFactory.getDao(WechatMenuDaoImpl.class);
	
	private FaqListDao faqDao = DaoFactory.getDao(FaqListDaoImpl.class);
	
	private OrdersDao ordersDao = DaoFactory.getDao(OrdersDaoImpl.class);
	
	private SetMealDao setMealDao = DaoFactory.getDao(SetMealDaoImpl.class);
	
	public void updateBusName(Long busId,String busName){
		cardDao.updateBusName(busId, busName);
		itemDao.updateBusName(busId, busName);
		shopDao.updateBusName(busId, busName);
		roomDao.updateBusName(busId, busName);
		couponDao.updateBusName(busId, busName);
		wechatDao.updateBusName(busId, busName);
		constantDao.updateBusName(busId, busName);
		userDao.updateBusName(busId, busName);
		menuDao.updateBusName(busId, busName);
		faqDao.updateBusName(busId, busName);
		ordersDao.updateBusName(busId, busName);
		setMealDao.updateBusName(busId, busName);
	}
	
	public void updateShopName(Long shopId,String shopName){
		roomDao.updateShopName(shopId, shopName);
		couponDao.updateShopName(shopId, shopName);
		ordersDao.updateShopName(shopId, shopName);
		setMealDao.updateShopName(shopId, shopName);
	}
	
	public void updateRoomName(Long roomId,String roomName){
		configDao.updateRoomName(roomId, roomName);
	}
}
