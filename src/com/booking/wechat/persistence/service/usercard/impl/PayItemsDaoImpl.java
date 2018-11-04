package com.booking.wechat.persistence.service.usercard.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.bean.usercard.PayItems;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.usercard.PayItemsDao;

@Service
public class PayItemsDaoImpl  extends DaoSupport<PayItems> implements PayItemsDao{

	
	public List<PayItems> getPayItemsByUser() {
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		PayItems params = new PayItems();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			params.setBusId(user.getBusId());
		}
		List<PayItems> list = findByExample(params,true).getRows();
		return list;
	}

	
	
	public void updateBusName(Long busId,String busName) {
		batchUpdate("update "+getEntityName(PayItems.class)+" set busName='"+busName+"' where busId="+busId);
	}
}
