package com.booking.wechat.realm;

import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.permisson.SysPermissionDao;
import com.booking.wechat.persistence.service.role.SysRoleDao;
import com.booking.wechat.persistence.service.user.SysUserDao;

public class MyRealm extends AuthorizingRealm {

	@Autowired
	private SysUserDao userDao;
	
	@Autowired
	private SysRoleDao roleDao;
	
	@Autowired
	private SysPermissionDao permDao;
	
	 /** 
     * 为当前登录的Subject授予角色和权限 
     * @see  经测试:本例中该方法的调用时机为需授权资源被访问时 
     * @see  经测试:并且每次访问需授权资源时都会执行该方法中的逻辑,这表明本例中默认并未启用AuthorizationCache 
     */  
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String currentUsername = (String)super.getAvailablePrincipal(principals);  
//      //为当前用户设置角色和权限  
        SysUser sysUser = userDao.findByUserId(currentUsername);
        SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();  
        Set<String> roles = roleDao.findRoles(sysUser.getId());
        simpleAuthorInfo.addRoles(roles);  
        Set<String> perms = permDao.findPermissions(sysUser.getId());
        simpleAuthorInfo.addStringPermissions(perms);  
        return simpleAuthorInfo;  
	}

	/** 
     * 验证当前登录的Subject 
     * @see  经测试:本例中该方法的调用时机为LoginController.login()方法中执行Subject.login()时 
     */  
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		String userId = (String)authcToken.getPrincipal();  
		SysUser sysUser = userDao.findByUserId(userId);
        if(sysUser == null) {  
            throw new UnknownAccountException();//没找到帐号  
        }  
//        if(sysUser.isLocked()) {  
//            throw new LockedAccountException(); //帐号锁定  5次错误账号锁定
//        }  
        //AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(  
                sysUser.getUserId(), //用户名  
                sysUser.getUserPassword(), //密码  
                ByteSource.Util.bytes(sysUser.getCredentialsSalt()),//salt=userId+salt  
                getName()  //realm name  
        );  
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("sysUser", sysUser);
        return authenticationInfo;  
	}
	
	@Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
