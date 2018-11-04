package com.booking.wechat.persistence.service.user.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.user.SysUserDao;
import com.booking.wechat.util.ListUtils;

@Service
public class SysUserDaoImpl extends DaoSupport<SysUser> implements SysUserDao {

	
	public SysUser findByUserId(String userId) {
		List<SysUser> list = findByCustomJpql("from "+getEntityName(SysUser.class) +" where userId=?1",new Object[]{userId});
		if(ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	
	public void updateBusName(Long busId, String busName) {
		batchUpdate("update "+getEntityName(SysUser.class)+" set busName='"+busName+"' where busId="+busId);
	}

}
