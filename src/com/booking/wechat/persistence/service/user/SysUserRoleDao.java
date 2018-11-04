package com.booking.wechat.persistence.service.user;

import com.booking.wechat.persistence.bean.user.SysUserRole;
import com.booking.wechat.persistence.service.base.DAO;

public interface SysUserRoleDao extends DAO<SysUserRole>{

	public void deleteByUser(Long userId);
}
