package com.booking.wechat.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.vo.RoleVO;
import com.booking.wechat.controller.vo.UserParamVO;
import com.booking.wechat.persistence.bean.bus.Business;
import com.booking.wechat.persistence.bean.role.SysRole;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.bean.user.SysUserRole;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.role.SysRoleDao;
import com.booking.wechat.persistence.service.user.SysUserDao;
import com.booking.wechat.persistence.service.user.SysUserRoleDao;
import com.booking.wechat.util.PasswordHelper;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/system/users")
public class SysUserController extends BaseController{

	@Autowired
	private SysUserDao userDao;
	
	@Autowired
	private SysRoleDao roleDao;
	
	@Autowired
	private SysUserRoleDao userRoleDao;
	
	
	@Autowired
	private BusinessDao busDao;
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView  delete(@PathVariable Long id){
		userDao.delete(id);
		return ajaxDoneSuccess("删除用户信息成功");
	}
	
	@RequestMapping(value="/resetPwd/{id}",method=RequestMethod.POST)
	public ModelAndView  resetPwd(@PathVariable Long id){
		SysUser bean = userDao.find(id);
		if(null == bean){
			return ajaxDoneError("没有找到用户信息");
		}
		bean.setUserPassword("88888888");
		PasswordHelper pdh = new PasswordHelper();
		pdh.encryptPassword(bean);
		userDao.update(bean);
		return ajaxDoneSuccess("重置密码成功");
	}
	
	@RequestMapping(value="/edit/{id}")
	public String  editUser(@PathVariable Long id,Model model){
		SysUser user = userDao.find(id);
		model.addAttribute("user", user);
		List<SysRole> roles = roleDao.getAll();
		List<SysRole> userRoles = roleDao.findByUser(id);
		
		List<RoleVO> rv = new ArrayList<RoleVO>();
		
		for (SysRole role : roles) {
			RoleVO vo = new RoleVO();
			vo.setId(role.getId());
			vo.setRoleName(role.getRoleDesc());
			for (SysRole ur : userRoles) {
				if(StringUtil.equals(role.getId(), ur.getId())){
					vo.setChecked(true);
					break;
				}
			}
			rv.add(vo);
		}
		
		List<Business> bus = busDao.getAll();
		model.addAttribute("bus", bus);
		model.addAttribute("roles", rv);
		return "/system/user/editUser";
	}
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public ModelAndView  updateUser(SysUser user,String[] userRoles){
		SysUser bean = userDao.find(user.getId());
		bean.setEmail(user.getEmail());
		bean.setPhoneNumber(user.getPhoneNumber());
		bean.setUserName(user.getUserName());
		bean.setBusId(user.getBusId());
		bean.setBusName(user.getBusName());
		userDao.update(bean);
		
		userRoleDao.deleteByUser(user.getId());
		for (String roleId : userRoles) {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(user.getId());
			userRole.setRoleId(Long.valueOf(roleId));
			userRoleDao.save(userRole);
		}
		return ajaxDoneSuccess("修改用户信息成功");
	}
	
	@RequestMapping(value="/updatePwd",method=RequestMethod.POST)
	public ModelAndView doPost(HttpServletRequest request) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String oldUserPassword = request.getParameter("oldPassw0rd");
		String newUserPassword = request.getParameter("newUserPassword");
		Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userId,oldUserPassword);
        try {
        	subject.login(token);
            
            PasswordHelper passHelper = new PasswordHelper();
            Session session = SecurityUtils.getSubject().getSession();
            SysUser user = (SysUser) session.getAttribute("sysUser");
            user.setUserName(userName);
            user.setUserPassword(newUserPassword);
            passHelper.encryptPassword(user);
            userDao.update(user);
            return ajaxDoneSuccess("修改用户信息成功");
        }catch (AuthenticationException e) {
            token.clear();
            return ajaxDoneError("修改失败，请检查密码信息是否正确");
        }
	}
	
	@RequestMapping(value="/add")
	public String  editUser(Model model){
		List<SysRole> roles = roleDao.getAll();
		model.addAttribute("roles", roles);
		List<Business> bus = busDao.getAll();
		model.addAttribute("bus", bus);
		return "/system/user/addUser";
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView insertUser(SysUser user,String[] userRoles){
		SysUser isExits = userDao.findByUserId(user.getUserId());
		if(null != isExits){
			return ajaxDoneError("登陆名已经存在");
		}
		user.setCreateDate(new Date());
		PasswordHelper pdh = new PasswordHelper();
		pdh.encryptPassword(user);
		user.setStatus("OK");
		userDao.save(user);
		
		for (String roleId : userRoles) {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(user.getId());
			userRole.setRoleId(Long.valueOf(roleId));
			userRoleDao.save(userRole);
		}
		return ajaxDoneSuccess("添加用户信息成功");
	}
	
	@RequestMapping("/list")
	public String getAllUser(UserParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		SysUser params = new SysUser();
		if(StringUtil.isNotEmpty(param.getUserId())){
			params.setUserId(param.getUserId());
		}
		if(StringUtil.isNotEmpty(param.getUserName())){
			params.setUserName(param.getUserName());
		}
		QueryResult<SysUser> userList = userDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		
		model.put("userList",userList);
		model.put("pageInfo",param);
		return "system/user/list";
	}
	
	
	@RequestMapping(method=RequestMethod.POST,value="/login")
	public String adminLogin(SysUser currUser,Map<String,String> map) throws Exception{
		Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(currUser.getUserId(),currUser.getUserPassword());
        try {
            user.login(token);
            return "redirect:/system/index.jsp";
        }catch (AuthenticationException e) {
            token.clear();
            map.put("error", "用户名或密码错误");
            return "/system/login";
        }
	}
	
	@RequestMapping("/logout")
	public String loginOut(HttpServletRequest request) {
		request.getSession().removeAttribute("user");
		Subject user = SecurityUtils.getSubject();
		user.logout();
		return "redirect:/system/login.jsp";
	}
}
