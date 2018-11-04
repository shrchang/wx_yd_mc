package com.booking.wechat.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.vo.RoleParamVO;
import com.booking.wechat.controller.vo.ZTreeVO;
import com.booking.wechat.persistence.bean.permission.SysPermission;
import com.booking.wechat.persistence.bean.role.SysRole;
import com.booking.wechat.persistence.bean.role.SysRolePermssion;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.permisson.SysPermissionDao;
import com.booking.wechat.persistence.service.role.SysRoleDao;
import com.booking.wechat.persistence.service.role.SysRolePermssionDao;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/system/roles")
public class SysRoleController extends BaseController{

	@Autowired
	private SysRoleDao roleDao;
	
	@Autowired
	private SysPermissionDao permDao;
	
	@Autowired
	private SysRolePermssionDao rolePerDao;
	
	@RequestMapping("/list")
	public String getAllList(RoleParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		SysRole params = new SysRole();
		if(StringUtil.isNotEmpty(param.getRoleCode())){
			params.setRoleCode(param.getRoleCode());
		}
		if(StringUtil.isNotEmpty(param.getRoleDesc())){
			params.setRoleDesc(param.getRoleDesc());
		}
		QueryResult<SysRole> list = roleDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		
		model.put("list",list);
		model.put("pageInfo",param);
		return "system/role/list";
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView  delete(@PathVariable Long id){
		rolePerDao.deleteByRoleId(id);
		roleDao.delete(id);
		return ajaxDoneSuccess("删除权限信息成功");
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView insert(SysRole bean){
		SysRole isExits = roleDao.findByCode(bean.getRoleCode());
		if(null != isExits){
			return ajaxDoneError("该角色已经存在");
		}
		bean.setAvailable("true");
		roleDao.save(bean);
		return ajaxDoneSuccess("添加角色信息成功");
	}
	
	@RequestMapping("/setPermissions/{roleId}")
	public String setPermissions(@PathVariable Long roleId, Map<String, Object> model) throws UnsupportedEncodingException{
		List<SysPermission>  list = permDao.getAll();
		List<SysPermission> perms = permDao.findByRole(roleId);
		List<ZTreeVO> treeList = new ArrayList<ZTreeVO>();
		for (SysPermission sysPermission : list) {
			ZTreeVO vo = new ZTreeVO();
			vo.setId(sysPermission.getId());
			vo.setPid(0l);
			vo.setName(sysPermission.getPermissionDesc());
			vo.setValue(sysPermission.getPermissionCode());
			for (SysPermission per : perms) {
				if(StringUtil.equalsIgnorCase(per.getId(), sysPermission.getId())){
					vo.setChecked(true);
					break;
				}
			}
			treeList.add(vo);
		}
		String treeData = JSONArray.fromObject(treeList).toString();
		model.put("treeData", treeData);
		model.put("roleId", roleId);
		return "system/role/setPermission";
	}
	
	@RequestMapping(value="/insertPermissions/{roleId}",method=RequestMethod.POST)
	public ModelAndView insertPermissions(@PathVariable Long roleId,String permissionIds){
		rolePerDao.deleteByRoleId(roleId);
		if(StringUtil.isNotEmpty(permissionIds)){
			String[] pers = permissionIds.split(",");
			for (String perId : pers) {
				SysRolePermssion srp = new SysRolePermssion();
				srp.setRoleId(roleId);
				srp.setPermissionId(Long.valueOf(perId));
				rolePerDao.save(srp);
			}
		}
		return ajaxDoneSuccess("设置角色权限信息成功");
	}
}
