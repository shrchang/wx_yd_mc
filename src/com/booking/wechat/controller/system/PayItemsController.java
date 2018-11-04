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
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.bean.usercard.PayItems;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.usercard.PayItemsDao;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/system/payItems")
public class PayItemsController extends BaseController{

	@Autowired
	private PayItemsDao payItemDao;
	
	@Autowired
	private BusinessDao busDao;
	
	@RequestMapping("/list")
	public String getAllList(BaseParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		PayItems params = new PayItems();
		
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		List<Business> bus = busDao.getBusinessByUser();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			params.setBusId(user.getBusId());
		}
		
		if(StringUtil.isNotEmpty(param.getSearchKey())){
			params.setPayInfo(param.getSearchKey());
		}
		if(null != param.getBusId() && param.getBusId()>0){
			params.setBusId(param.getBusId());
		}
		QueryResult<PayItems> list = payItemDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		model.put("bus", bus);
		model.put("list",list);
		model.put("pageInfo",param);
		return "system/payItem/list";
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long id){
		payItemDao.delete(id);
		return ajaxDoneSuccess("删除充值信息成功");
	}
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String getPayItem(@PathVariable Long id,Model model){
		List<Business> bus = busDao.getBusinessByUser();
		model.addAttribute("bus", bus);
		PayItems payItem = payItemDao.find(id);
		model.addAttribute("payItem",payItem);
		return "system/payItem/edit";
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public ModelAndView updateBean(@PathVariable Long id,PayItems item){
		item.setId(id);
		payItemDao.update(item);
		return ajaxDoneSuccess("修改充值信息成功");
	}
	
	@RequestMapping("/add")
	public String addPayItem(Map<String, Object> model){
		List<Business> bus = busDao.getBusinessByUser();
		model.put("bus", bus);
		return "system/payItem/add";
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView insertPayItem(PayItems item){
		Business bus = busDao.find(item.getBusId());
		if(null== bus){
			return ajaxDoneError("没有找到商户信息");
		}
		payItemDao.save(item);
		return ajaxDoneSuccess("添加充值信息成功");
	}
}
