package com.booking.wechat.persistence.service.role;

import java.util.List;
import java.util.Set;

import com.booking.wechat.persistence.bean.role.SysRole;
import com.booking.wechat.persistence.service.base.DAO;

public interface SysRoleDao extends DAO<SysRole> {


	/**
     * 根据用户查找其角色
     * @param username
     * @return
     */
    public Set<String> findRoles(Long id);
    
    public SysRole findByCode(String code);
    
    public List<SysRole> findByUser(Long userId);
}
