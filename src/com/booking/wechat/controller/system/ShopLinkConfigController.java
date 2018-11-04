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
import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.shop.ShopLinkConfig;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.shop.BranchShopDao;
import com.booking.wechat.persistence.service.shop.ShopLinkConfigDao;

@Controller
@RequestMapping("/system/shoplinks")
public class ShopLinkConfigController extends BaseController{

	@Autowired
	private BusinessDao busDao;
	
	@Autowired
	private ShopLinkConfigDao shopLinkDao;
	
	@Autowired
	private BranchShopDao shopDao;
	
	@RequestMapping("/list")
	public String getAllList(BaseParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		ShopLinkConfig params = new ShopLinkConfig();
		
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
		if(null != param.getShopId() && param.getShopId()>0){
			params.setShopId(param.getShopId());
		}
		QueryResult<ShopLinkConfig> list = shopLinkDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		model.put("bus", bus);
		
		List<BranchShop> shops = shopDao.getBranchShopByUser();
		model.put("shops", shops);
		
		model.put("list",list);
		model.put("pageInfo",param);
		return "system/shop/configList";
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long id){
		shopLinkDao.delete(id);
		return ajaxDoneSuccess("删除分店信息成功");
	}
	
	@RequestMapping("/add")
	public String add(Map<String, Object> model){
		List<Business> bus = busDao.getBusinessByUser();
		model.put("bus", bus);
		return "system/shop/addConfig";
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView add(ShopLinkConfig config){
		Business bus = busDao.find(config.getBusId());
		if(null== bus){
			return ajaxDoneError("没有找到商户信息");
		}
		
		List<ShopLinkConfig> configs = shopLinkDao.selectByShopId(config.getShopId());
		if(configs.size()>=4){
			return ajaxDoneError("分店链接最多只能创建4个");
		}
		shopLinkDao.save(config);
		
		return ajaxDoneSuccess("添加分店链接信息成功");
	}
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable Long id,Model model){
		ShopLinkConfig linkConfig = shopLinkDao.find(id);
		model.addAttribute("linkConfig", linkConfig);
		return "system/shop/editConfig";
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public ModelAndView updateBean(@PathVariable Long id,ShopLinkConfig config){
		ShopLinkConfig linkConfig = shopLinkDao.find(id);
		linkConfig.setLinkName(config.getLinkName());
		linkConfig.setLinkPath(config.getLinkPath());
		shopLinkDao.update(linkConfig);
		return ajaxDoneSuccess("修改分店链接信息成功");
	}
}
