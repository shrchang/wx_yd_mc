package com.booking.wechat.init;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.bean.role.SysRole;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.bean.user.SysUserRole;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.persistence.service.role.SysRoleDao;
import com.booking.wechat.persistence.service.user.SysUserDao;
import com.booking.wechat.persistence.service.user.SysUserRoleDao;
import com.booking.wechat.util.ListUtils;
import com.booking.wechat.util.PasswordHelper;

public class InitSystemData implements ApplicationListener<ApplicationEvent>{

	@Autowired
	private ConstantDao constantDao;
	
	@Autowired
	private SysUserDao userDao;
	
	@Autowired
	private SysRoleDao roleDao;
	
	@Autowired
	private SysUserRoleDao userRoleDao;
	

	
	public void onApplicationEvent(ApplicationEvent arg0) {
		Constant webSite = constantDao.findByConstantId(null, "WEB_SITE");
		if(null == webSite){
			webSite = new Constant();
			webSite.setConstantId("WEB_SITE");
			webSite.setConstantGroup("SYSTEM_CONFIG");
			webSite.setConstantName("系统访问根目录");
			webSite.setConstantValue("http://wechatsite.wicp.net/booking/");
			constantDao.save(webSite);
		}
		
		List<SysUser> users = userDao.getAll();
		if(ListUtils.isEmpty(users)){
			SysRole adminRole = new SysRole();
			adminRole.setRoleCode("admin");
			adminRole.setRoleDesc("系统管理员");
			adminRole.setAvailable("true");
			roleDao.save(adminRole);
			
			SysRole busRole = new SysRole();
			busRole.setRoleCode("business");
			busRole.setRoleDesc("商户管理员");
			busRole.setAvailable("true");
			roleDao.save(busRole);
			
			SysUser user = new SysUser();
			user.setUserId("admin");
			user.setUserPassword("passw0rd");
			user.setUserName("系统管理员");
			user.setCreateDate(new Date());
			PasswordHelper pdh = new PasswordHelper();
			pdh.encryptPassword(user);
			user.setStatus("OK");
			userDao.save(user);
			
			SysUserRole userRole = new SysUserRole();
			userRole.setRoleId(adminRole.getId());
			userRole.setUserId(user.getId());
			userRoleDao.save(userRole);
		}
	}
}
