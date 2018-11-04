package com.booking.wechat.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
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
import com.booking.wechat.controller.vo.BaseParamVO;
import com.booking.wechat.persistence.bean.bus.Business;
import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/system/constant")
public class ConstantController extends BaseController{

	@Autowired
	private ConstantDao constantDao;
	
	@Autowired
	private BusinessDao busDao;
	
	@RequestMapping("/list")
	public String getAllList(BaseParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		Constant params = new Constant();
		
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		List<Business> bus = busDao.getBusinessByUser();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			params.setBusId(user.getBusId());
		}
		
		if(StringUtil.isNotEmpty(param.getSearchKey())){
			params.setConstantName(param.getSearchKey());
		}
		if(null != param.getBusId() && param.getBusId()>0){
			params.setBusId(param.getBusId());
		}
		QueryResult<Constant> list = constantDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		model.put("bus", bus);
		model.put("list",list);
		model.put("pageInfo",param);
		return "system/constant/list";
	}
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String getBean(@PathVariable Long id,Model model){
		List<Business> bus = busDao.getBusinessByUser();
		model.addAttribute("bus", bus);
		Constant constant = constantDao.find(id);
		model.addAttribute("constant", constant);
		return "system/constant/edit";
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public ModelAndView updateBean(@PathVariable Long id,Constant constant){
		constant.setId(id);
		constantDao.update(constant);
		return ajaxDoneSuccess("修改配置信息成功");
	}
	
	@RequestMapping("/add")
	public String addConstant(Map<String, Object> model){
		List<Business> bus = busDao.getBusinessByUser();
		model.put("bus", bus);
		return "system/constant/add";
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView insertBean(Constant constant){
		constantDao.save(constant);
		return ajaxDoneSuccess("添加配置信息成功");
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long id){
		constantDao.delete(id);
		return ajaxDoneSuccess("删除配置信息成功");
	}
}
