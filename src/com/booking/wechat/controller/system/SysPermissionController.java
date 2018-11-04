package com.booking.wechat.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.vo.PermissionParamVO;
import com.booking.wechat.persistence.bean.permission.SysPermission;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.permisson.SysPermissionDao;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/system/permissions")
public class SysPermissionController extends BaseController{

	@Autowired
	private SysPermissionDao perDao;
	
	@RequestMapping("/list")
	public String getAllList(PermissionParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		SysPermission params = new SysPermission();
		if(StringUtil.isNotEmpty(param.getPermissionCode())){
			params.setPermissionCode(param.getPermissionCode());
		}
		if(StringUtil.isNotEmpty(param.getPermissionDesc())){
			params.setPermissionDesc(param.getPermissionDesc());
		}
		QueryResult<SysPermission> list = perDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		
		model.put("list",list);
		model.put("pageInfo",param);
		return "system/permission/list";
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView  delete(@PathVariable Long id){
		perDao.delete(id);
		return ajaxDoneSuccess("删除权限信息成功");
	}
	
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView insertBean(SysPermission bean){
		SysPermission isExits = perDao.findByCode(bean.getPermissionCode());
		if(null != isExits){
			return ajaxDoneError("该权限已经存在");
		}
		bean.setAvailable("true");
		perDao.save(bean);
		return ajaxDoneSuccess("添加权限信息成功");
	}
}
