package com.booking.wechat.persistence.service.user.impl;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.user.SysUserRole;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.user.SysUserRoleDao;

@Service
public class SysUserRoleDaoImpl extends DaoSupport<SysUserRole> implements SysUserRoleDao{

	
	public void deleteByUser(Long userId) {
		batchUpdate("delete from "+this.getEntityName(SysUserRole.class) +" where userId="+userId);
	}

}
