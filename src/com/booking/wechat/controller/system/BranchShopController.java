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
import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.common.CommonService;
import com.booking.wechat.persistence.service.room.RoomBookingRangeConfigDao;
import com.booking.wechat.persistence.service.room.RoomDao;
import com.booking.wechat.persistence.service.shop.BranchShopDao;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/system/shops")
public class BranchShopController extends BaseController{

	@Autowired
	private BranchShopDao shopDao;
	
	@Autowired
	private BusinessDao busDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private RoomBookingRangeConfigDao configDao;
	
	@RequestMapping("/list")
	public String getAllList(BaseParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		BranchShop params = new BranchShop();
		
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		List<Business> bus = busDao.getBusinessByUser();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			params.setBusId(user.getBusId());
		}
		
		if(StringUtil.isNotEmpty(param.getSearchKey())){
			params.setShopName(param.getSearchKey());
		}
		if(null != param.getBusId() && param.getBusId()>0){
			params.setBusId(param.getBusId());
		}
		QueryResult<BranchShop> list = shopDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		model.put("bus", bus);
		model.put("list",list);
		model.put("pageInfo",param);
		return "system/shop/list";
	}
	
	@RequestMapping("/add")
	public String addShop(Map<String, Object> model){
		List<Business> bus = busDao.getBusinessByUser();
		model.put("bus", bus);
		return "system/shop/add";
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView insertShop(BranchShop shop){
		Business bus = busDao.find(shop.getBusId());
		if(null== bus){
			return ajaxDoneError("没有找到商户信息");
		}
		
		List<BranchShop> shops = shopDao.findByBusiness(shop.getBusId());
		if(shops.size()>=bus.getMaxRoom()){//商户最大场分店已经大于或者等于限制
			return ajaxDoneError("商户的最大分店数超出限制，不能再创建分店");
		}
		shopDao.save(shop);
		
		return ajaxDoneSuccess("添加分店信息成功");
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long id){
		shopDao.delete(id);
		configDao.deleteByShopId(id);
		roomDao.deleteByShopId(id);
		return ajaxDoneSuccess("删除分店信息成功");
	}
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String getBusiness(@PathVariable Long id,Model model){
		List<Business> bus = busDao.getBusinessByUser();
		model.addAttribute("bus", bus);
		BranchShop shop = shopDao.find(id);
		model.addAttribute("shop", shop);
		return "system/shop/edit";
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public ModelAndView updateBus(@PathVariable Long id,BranchShop shop){
		BranchShop oldShop = shopDao.find(id);
		boolean updateName = StringUtil.equals(oldShop.getShopName(), shop.getShopName());
		shop.setId(id);
		shopDao.update(shop);
		if(!updateName){
			CommonService comm = new CommonService();
			comm.updateShopName(shop.getId(), shop.getShopName());
		}
		return ajaxDoneSuccess("修改分店信息成功");
	}
	
	@RequestMapping("/findByBus/{busId}")
	public @ResponseBody List<Object[]> findByBus(@PathVariable Long busId){
		List<Object[]> list = new ArrayList<Object[]>();
		Object[] select = new Object[2];
		select[0] = "-1";
		select[1] = "请选择分店";
		list.add(select);
		if(null != busId && busId>0){
			List<BranchShop> shops = shopDao.findByBusiness(busId);
			for (BranchShop branchShop : shops) {
				Object[] shop = new Object[2];
				shop[0] = branchShop.getId();
				shop[1] = branchShop.getShopName();
				list.add(shop);
			}
		}
		return list;
	}
}
