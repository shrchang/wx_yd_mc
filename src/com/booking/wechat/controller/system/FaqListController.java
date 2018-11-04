package com.booking.wechat.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.vo.BaseParamVO;
import com.booking.wechat.persistence.bean.bus.Business;
import com.booking.wechat.persistence.bean.faq.FaqList;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.faq.FaqListDao;

@Controller
@RequestMapping("/system/faqs")
public class FaqListController extends BaseController{

	@Autowired
	private FaqListDao faqDao;
	
	@Autowired
	private BusinessDao busDao;
	
	@RequestMapping("/list")
	public String getAllList(BaseParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		FaqList params = new FaqList();
		
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		List<Business> bus = busDao.getBusinessByUser();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			params.setBusId(user.getBusId());
		}
		
		if(null != param.getBusId() && param.getBusId()>0){
			params.setBusId(param.getBusId());
		}
		QueryResult<FaqList> list = faqDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		model.put("bus", bus);
		model.put("list",list);
		model.put("pageInfo",param);
		return "system/faq/list";
	}
	
	@RequestMapping("/add")
	public String add(Map<String, Object> model){
		List<Business> bus = busDao.getBusinessByUser();
		model.put("bus", bus);
		return "system/faq/add";
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView insert(FaqList faq){
		Business bus = busDao.find(faq.getBusId());
		if(null== bus){
			return ajaxDoneError("没有找到商户信息");
		}
		setFaqList(faq);
		faqDao.save(faq);
		return ajaxDoneSuccess("添加自动回复配置信息成功");
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long id){
		faqDao.delete(id);
		return ajaxDoneSuccess("删除自动回复配置信息成功");
	}
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String getBean(@PathVariable Long id,Model model){
		List<Business> bus = busDao.getBusinessByUser();
		model.addAttribute("bus", bus);
		FaqList faq = faqDao.find(id);
		model.addAttribute("faq", faq);
		List<FaqList> parents = faqDao.findByBusiness(faq.getBusId(), true);
		model.addAttribute("parents",parents);
		return "system/faq/edit";
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public ModelAndView updateBean(@PathVariable Long id,FaqList faq){
		faq.setId(id);
		setFaqList(faq);
		faqDao.update(faq);
		return ajaxDoneSuccess("修改自动回复配置信息成功");
	}
	
	@RequestMapping("/findByBus/{busId}")
	public @ResponseBody List<Object[]> findByBus(@PathVariable Long busId){
		List<Object[]> list = new ArrayList<Object[]>();
		Object[] select = new Object[2];
		select[0] = "0";
		select[1] = "请选择父级分类";
		list.add(select);
		if(null != busId && busId>0){
			List<FaqList> faqs = faqDao.findByBusiness(busId,true);
			for (FaqList faq : faqs) {
				Object[] faqObj = new Object[2];
				faqObj[0] = faq.getId();
				faqObj[1] = faq.getFaqType();
				list.add(faqObj);
			}
		}
		return list;
	}
	
	private void setFaqList(FaqList faq){
		if(faq.getParentId().longValue()==0){
			faq.setFaqTitle(null);
			faq.setFaqContext(null);
		}else{
			faq.setFaqType(null);
		}
	}
}
