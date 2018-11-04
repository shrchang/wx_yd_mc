package com.booking.wechat.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
import com.booking.wechat.persistence.bean.room.Room;
import com.booking.wechat.persistence.bean.room.RoomBookingRangeConfig;
import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.room.RoomBookingRangeConfigDao;
import com.booking.wechat.persistence.service.room.RoomDao;
import com.booking.wechat.persistence.service.shop.BranchShopDao;

@Controller
@RequestMapping("/system/roomConfigs")
public class RoomConfigController extends BaseController{

	@Autowired
	private BusinessDao busDao;
	
	@Autowired
	private BranchShopDao shopDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private RoomBookingRangeConfigDao configDao;
	
	@RequestMapping("/list")
	public String getAllList(BaseParamVO param,Integer week, Map<String, Object> model) throws UnsupportedEncodingException{
		RoomBookingRangeConfig params = new RoomBookingRangeConfig();
		
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		List<Business> bus = busDao.getBusinessByUser();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			params.setBusId(user.getBusId());
		}
		if(null != week && !"-1".equals(week)){
			params.setWeek(week);
		}
		model.put("bus", bus);
		
		List<BranchShop> shops = shopDao.getBranchShopByUser();
		if(null != param.getBusId() && param.getBusId()>0){
			params.setBusId(param.getBusId());
			shops = shopDao.findByBusiness(param.getBusId());
		}
		List<Room> rooms = null;
		if(null != param.getShopId() && param.getShopId()>0){
			params.setShopId(param.getShopId());
			rooms = roomDao.findRoomByShop(param.getShopId());
		}
		if(null != param.getRoomId() && param.getRoomId()>0){
			params.setRoomId(param.getRoomId());
		}
		model.put("shops", shops);
		model.put("rooms",rooms);
		
		QueryResult<RoomBookingRangeConfig> list = configDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		model.put("list",list);
		model.put("pageInfo",param);
		model.put("week", week);
		return "system/roomConfig/list";
	}
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String editRoomConfig(@PathVariable Long id,Model model){
		RoomBookingRangeConfig rc = configDao.find(id);
		model.addAttribute("rc", rc);
		return "system/roomConfig/edit";
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public ModelAndView updateBean(@PathVariable Long id,RoomBookingRangeConfig rc){
		RoomBookingRangeConfig bean = configDao.find(id);
		bean.setTimeRange(rc.getTimeRange());
		bean.setRoomPrice(rc.getRoomPrice());
		bean.setBookingPriceRate(rc.getBookingPriceRate());
		configDao.update(bean);
		return ajaxDoneSuccess("修改场地配置信息成功");
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView  delete(@PathVariable Long id){
		configDao.delete(id);
		return ajaxDoneSuccess("删除场地配置信息成功");
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView add(RoomBookingRangeConfig roomConfig,String[] weeks){
		if(null == roomConfig.getBusId()){
			BranchShop shop = shopDao.find(roomConfig.getShopId());
			roomConfig.setBusId(shop.getBusId());
		}
		String timeRangeArray = roomConfig.getTimeRange();
		for (String week : weeks) {
			StringTokenizer token = new StringTokenizer(timeRangeArray, "|"); 
			while (token.hasMoreTokens()){
				roomConfig.setWeek(Integer.valueOf(week));
				roomConfig.setId(null);
				roomConfig.setTimeRange(token.nextToken());
				configDao.save(roomConfig);
			}
		}
		return ajaxDoneSuccess("添加场地配置信息成功");
	}
	
	
	@RequestMapping("/add")
	public String add(Map<String, Object> model){
		Subject subject = SecurityUtils.getSubject();
		//没有系统管理员权限，根据商户查询
		if(subject.hasRole("admin")){
			List<Business> bus = busDao.getBusinessByUser();
			model.put("bus", bus);
		}else{
			List<BranchShop> shops = shopDao.getBranchShopByUser();
			model.put("shops", shops);
		}
		return "system/roomConfig/add";
	}
}
