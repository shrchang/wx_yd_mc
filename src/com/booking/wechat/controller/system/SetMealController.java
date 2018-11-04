package com.booking.wechat.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.booking.wechat.persistence.bean.setmeal.SetMeal;
import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.setmeal.SetMealDao;
import com.booking.wechat.persistence.service.shop.BranchShopDao;
import com.booking.wechat.util.StringUtil;

/**
 * 增值套餐的控制器
 * @ClassName SetMealController
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月17日 下午3:31:38
 *
 */
@Controller
@RequestMapping("/system/setmeal")
public class SetMealController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(RoomController.class);
	
	@Autowired
	private BusinessDao busDao;
	
	@Autowired
	private SetMealDao setmealDao;
	
	@Autowired
	private BranchShopDao shopDao;
	
	/**
	 * 主界面跳转
	 * @author shrChang.Liu
	 * @param param
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 * @date 2018年10月17日 下午3:33:13
	 * @return String
	 * @description
	 */
	@RequestMapping("/list")
	public String getAllList(BaseParamVO param, Map<String, Object> model) throws UnsupportedEncodingException {
		SetMeal params = new SetMeal();

		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		List<Business> bus = busDao.getBusinessByUser();
		// 没有系统管理员权限，根据商户查询
		if (!subject.hasRole("admin")) {
			params.setBusId(user.getBusId());
		}
		if (StringUtil.isNotEmpty(param.getSearchKey())) {
			params.setMealName(param.getSearchKey());
		}
		if (null != param.getBusId() && param.getBusId() > 0) {
			params.setBusId(param.getBusId());
		}
		if (null != param.getShopId() && param.getShopId() > 0) {
			params.setShopId(param.getShopId());
		}
		QueryResult<SetMeal> list = setmealDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		model.put("bus", bus);

		List<BranchShop> shops = shopDao.getBranchShopByUser();
		model.put("shops", shops);

		model.put("list", list);
		model.put("pageInfo", param);
		return "system/setmeal/list";
	}
	
	/**
	 * 新增套餐界面跳转
	 * @author shrChang.Liu
	 * @param model
	 * @return
	 * @date 2018年10月17日 下午3:40:10
	 * @return String
	 * @description
	 */
	@RequestMapping("/add")
	public String add(Map<String, Object> model) {
		List<Business> bus = busDao.getBusinessByUser();
		model.put("bus", bus);
		return "system/setmeal/add";
	}
	
	/**
	 * 修改界面跳转
	 * @author shrChang.Liu
	 * @param id
	 * @param model
	 * @return
	 * @date 2018年10月17日 下午3:40:58
	 * @return String
	 * @description
	 */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editSetMeal(@PathVariable Long id, Model model) {
		SetMeal room = setmealDao.find(id);
		model.addAttribute("setmeal", room);
		List<Business> bus = busDao.getBusinessByUser();
		model.addAttribute("bus", bus);
		List<BranchShop> shops = shopDao.findByBusiness(room.getBusId());
		model.addAttribute("shops", shops);
		return "system/setmeal/edit";
	}
	
	/**
	 * 删除套餐
	 * @author shrChang.Liu
	 * @param id
	 * @return
	 * @date 2018年10月17日 下午3:42:25
	 * @return ModelAndView
	 * @description
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long id) {
		setmealDao.delete(id);
		//其实这里应该要查询是否参数是否已经被使用过 这个需要改造订单的内容
		return ajaxDoneSuccess("删除增值套餐信息成功");
	}
	
	/**
	 * 添加增值套餐
	 * @author shrChang.Liu
	 * @param meal
	 * @return
	 * @date 2018年10月17日 下午3:54:56
	 * @return ModelAndView
	 * @description
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ModelAndView add(SetMeal meal) {
		Business bus = busDao.find(meal.getBusId());
		if (null == bus) {
			return ajaxDoneError("没有找到商户信息");
		}
		setmealDao.save(meal);
		return ajaxDoneSuccess("添加套餐信息成功");
	}
	
	/**
	 * 修改套餐信息
	 * @author shrChang.Liu
	 * @param id
	 * @param seMeal
	 * @return
	 * @date 2018年10月17日 下午3:58:09
	 * @return ModelAndView
	 * @description
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public ModelAndView updateBean(@PathVariable Long id, SetMeal seMeal) {
		seMeal.setId(id);
		SetMeal old= setmealDao.find(id);
		boolean updateName = StringUtil.equals(old.getMealName(), seMeal.getMealName());
		setmealDao.update(seMeal);
		if (!updateName) {//修改了名称的话 需要在其他地方也要修改引用的这个名称的地方
			
		}
		return ajaxDoneSuccess("修改场地信息成功");
	}
	
	
}
