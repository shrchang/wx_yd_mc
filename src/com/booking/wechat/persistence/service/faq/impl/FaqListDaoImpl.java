package com.booking.wechat.persistence.service.faq.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.faq.FaqList;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.faq.FaqListDao;

@Service
public class FaqListDaoImpl extends DaoSupport<FaqList> implements FaqListDao{

	
	public void updateBusName(Long busId,String busName) {
		batchUpdate("update "+getEntityName(FaqList.class)+" set busName='"+busName+"' where busId="+busId);
	}

	
	public List<FaqList> getFaqListParentByUser() {
		FaqList entity = new FaqList();
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			entity.setBusId(user.getBusId());
		}
		entity.setParentId(0l);
		List<FaqList> list = findByExample(entity,true).getRows();
		return list;
	}

	
	public List<FaqList> findByBusiness(Long busId,boolean parent) {
		FaqList entity = new FaqList();
		entity.setBusId(busId);
		if(parent){
			entity.setParentId(0l);
		}
		List<FaqList> list = findByExample(entity,true).getRows();
		return list;
	}
}
