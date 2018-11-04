package com.booking.wechat.persistence.service.permisson.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.permission.SysPermission;
import com.booking.wechat.persistence.bean.role.SysRole;
import com.booking.wechat.persistence.bean.role.SysRolePermssion;
import com.booking.wechat.persistence.bean.user.SysUserRole;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.permisson.SysPermissionDao;
import com.booking.wechat.util.ListUtils;

@Service
public class SysPermissionDaoImpl extends DaoSupport<SysPermission> implements SysPermissionDao{
	
	
	public Set<String> findPermissions(Long id) {
		StringBuffer sql = new StringBuffer("from "+getEntityName(SysPermission.class)+" sp where sp.id in (");
		sql.append("select srp.permissionId from "+getEntityName(SysRolePermssion.class)+" srp where srp.roleId in (");
		sql.append("select sr.id from "+getEntityName(SysRole.class)+" sr where sr.id in (");
		sql.append("select sur.roleId from "+getEntityName(SysUserRole.class)+" sur where sur.userId=?1)))");
		List<SysPermission> list = findByCustomJpql(sql.toString(),new Object[]{id});
		Set<String> set = new HashSet<String>();
		for (SysPermission sysPermission : list) {
			set.add(sysPermission.getPermissionCode());
		}
		return set;
	}

	public SysPermission findByCode(String permissionCode) {
		String sql = "from "+getEntityName(SysPermission.class)+" where permissionCode=?1";
		List<SysPermission> list = findByCustomJpql(sql,new Object[]{permissionCode});
		if(ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	public List<SysPermission> findByRole(Long roleId) {
		String sql = "from "+getEntityName(SysPermission.class)+" sp where sp.id in (select srp.permissionId from "+getEntityName(SysRolePermssion.class)+" srp where srp.roleId=?1)";
		List<SysPermission> list = findByCustomJpql(sql,new Object[]{roleId});
		return list;
	}
}
