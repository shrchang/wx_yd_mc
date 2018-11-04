package com.booking.wechat.persistence.service.role.impl;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.role.SysRolePermssion;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.role.SysRolePermssionDao;

@Service
public class SysRolePermssionDaoImpl extends DaoSupport<SysRolePermssion> implements SysRolePermssionDao{

	
	public void deleteByRoleId(Long roleId) {
		batchUpdate("delete from "+this.getEntityName(SysRolePermssion.class) +" where roleId="+roleId);
	}

}
