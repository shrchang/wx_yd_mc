package com.booking.wechat.persistence.service.bus.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.bus.Business;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.bus.BusinessDao;

@Service
public class BusinessDaoImpl extends DaoSupport<Business> implements BusinessDao{

	
	public List<Object[]> getBusinessAndRoom(){
		String sql = "select b.id,b.busName,b.busCity,COUNT(r.id) as roomCount from tb_business b LEFT JOIN tb_room as r on(b.id=r.busId) GROUP BY r.busId";
		List<Object[]> list = super.queryByNativeSql(sql);
		return list;
	}

	
	public List<Business> getBusinessByUser() {
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		List<Business> bus = null;
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			bus = new ArrayList<Business>();
			Business bean = find(user.getBusId());
			bus.add(bean);
		}else{
			bus = getAll();
		}
		return bus;
	}
}
