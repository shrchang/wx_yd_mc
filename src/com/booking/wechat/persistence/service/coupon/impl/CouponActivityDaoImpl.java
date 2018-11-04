package com.booking.wechat.persistence.service.coupon.impl;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.coupon.CouponActivity;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.coupon.CouponActivityDao;
import com.booking.wechat.util.DateUtils;

@Service
public class CouponActivityDaoImpl extends DaoSupport<CouponActivity> implements CouponActivityDao  {

	public List<CouponActivity> selectActive(String status,String date) {
        List<CouponActivity> list = findByCustomJpql("from "+getEntityName(CouponActivity.class)+" where endDate>='"+date+"' and startDate<='"+date+"' and status=?1",new Object[]{status});
        return list;  
	}
	
	
	public void updateBusName(Long busId,String busName) {
		batchUpdate("update "+getEntityName(CouponActivity.class)+" set busName='"+busName+"' where busId="+busId);
	}
	
	
	public void updateShopName(Long shopId,String shopName){
		batchUpdate("update "+getEntityName(CouponActivity.class)+" set shopName='"+shopName+"' where shopId="+shopId);
	}

	
	public List<CouponActivity> findByUser(String couponType,String status,Date date) {
		String dateStr = DateUtils.date2String(date,DateUtils.PATTERN_DATE);
		String sql = "from "+getEntityName(CouponActivity.class)+" where endDate>='"+dateStr+"' and startDate<='"+dateStr+"' and status=?1 and couponType=?2";
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			sql += " and busId="+user.getBusId();
		}
		List<CouponActivity> list = findByCustomJpql(sql,new Object[]{status,couponType});
        return list;  
	}

	
	public List<CouponActivity> findByBus(Long busId, String couponType,
			String status, Date date) {
		String dateStr = DateUtils.date2String(date,DateUtils.PATTERN_DATE);
		String sql = "from "+getEntityName(CouponActivity.class)+" where endDate>='"+dateStr+"' and startDate<='"+dateStr+"' and status=?1 and couponType=?2 and busId=?3";
		List<CouponActivity> list = findByCustomJpql(sql,new Object[]{status,couponType,busId});
        return list;  
	}
}
