package com.booking.wechat.persistence.service.role.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.role.SysRole;
import com.booking.wechat.persistence.bean.user.SysUserRole;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.role.SysRoleDao;
import com.booking.wechat.util.ListUtils;

@Service
public class SysRoleDaoImpl extends DaoSupport<SysRole> implements SysRoleDao{


	public Set<String> findRoles(Long id) {
		String sql = "from "+getEntityName(SysRole.class)+" sr where sr.id in (select sur.roleId from "+getEntityName(SysUserRole.class)+" sur where sur.userId =?1)";
		List<SysRole> list = findByCustomJpql(sql,new Object[]{id});
		Set<String> set = new HashSet<String>();
		for (SysRole sysRole : list) {
			set.add(sysRole.getRoleCode());
		}
		return set;
	}

	public SysRole findByCode(String code) {
		String sql = "from "+getEntityName(SysRole.class)+" where roleCode=?1";
		List<SysRole> list = findByCustomJpql(sql,new Object[]{code});
		if(ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	public List<SysRole> findByUser(Long userId) {
		String sql = "from "+getEntityName(SysRole.class)+" role where role.id in(select ur.roleId from "+getEntityName(SysUserRole.class)+" ur where ur.userId=?1)";
		List<SysRole> list = findByCustomJpql(sql,new Object[]{userId});
		return list;
	}
}
