package com.booking.wechat.persistence.service.shop.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.shop.BranchShopDao;

@Service
public class BranchShopDaoImpl extends DaoSupport<BranchShop> implements BranchShopDao{

	
	public List<BranchShop> findByBusiness(Long busId) {
		BranchShop params = new BranchShop();
		params.setBusId(busId);
		return findByExample(params,true).getRows();
	}

	
	public List<BranchShop> getBranchShopByUser() {
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		BranchShop params = new BranchShop();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			params.setBusId(user.getBusId());
		}
		List<BranchShop> list = findByExample(params,true).getRows();
		return list;
	}

	
	
	public void updateBusName(Long busId,String busName) {
		batchUpdate("update "+getEntityName(BranchShop.class)+" set busName='"+busName+"' where busId="+busId);
	}
}
