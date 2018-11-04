package com.booking.wechat.persistence.service.coupon;

import java.util.Date;
import java.util.List;

import com.booking.wechat.persistence.bean.coupon.CouponActivity;
import com.booking.wechat.persistence.service.base.DAO;

public interface CouponActivityDao extends DAO<CouponActivity> {

	public List<CouponActivity> selectActive(String status,String date);
	
	public void updateBusName(Long busId,String busName) ;
	
	public void updateShopName(Long shopId,String shopName) ;
	
	public List<CouponActivity> findByUser(String couponType,String status,Date date);
	
	public List<CouponActivity> findByBus(Long busId,String couponType,String status,Date date);
} 
