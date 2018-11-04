package com.booking.wechat.persistence.service.user;

import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.DAO;

public interface SysUserDao extends DAO<SysUser>{

	public SysUser findByUserId(String userId);
	
	public void updateBusName(Long busId,String busName);
}
