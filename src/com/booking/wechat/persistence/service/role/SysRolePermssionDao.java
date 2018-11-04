package com.booking.wechat.persistence.service.role;

import com.booking.wechat.persistence.bean.role.SysRolePermssion;
import com.booking.wechat.persistence.service.base.DAO;

public interface SysRolePermssionDao extends DAO<SysRolePermssion>{

	public void deleteByRoleId(Long roleId);
}
