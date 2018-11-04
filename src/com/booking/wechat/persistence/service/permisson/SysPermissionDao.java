package com.booking.wechat.persistence.service.permisson;

import java.util.List;
import java.util.Set;

import com.booking.wechat.persistence.bean.permission.SysPermission;
import com.booking.wechat.persistence.service.base.DAO;

public interface SysPermissionDao  extends DAO<SysPermission>{

	/**
     * 根据用户查找其权限
     * @param username
     * @return
     */
    public Set<String> findPermissions(Long id);
    
    public SysPermission findByCode(String permissionCode);
    
    public List<SysPermission> findByRole(Long roleId);
}
