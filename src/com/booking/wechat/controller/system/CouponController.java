package com.booking.wechat.controller.system;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.vo.BaseParamVO;
import com.booking.wechat.persistence.bean.bus.Business;
import com.booking.wechat.persistence.bean.coupon.CouponActivity;
import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.coupon.CouponActivityDao;
import com.booking.wechat.persistence.service.shop.BranchShopDao;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/system/coupons")
public class CouponController extends BaseController{

	@Autowired
	private BusinessDao busDao;
	
	@Autowired
	private CouponActivityDao couponDao;
	
	@Autowired
	private BranchShopDao shopDao;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));//true:允许输入空值，false:不能为空值
	}
	
	@RequestMapping("/list")
	public String getAllList(BaseParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		CouponActivity params = new CouponActivity();
		
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		List<Business> bus = busDao.getBusinessByUser();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			params.setBusId(user.getBusId());
		}
		if(StringUtil.isNotEmpty(param.getSearchKey())){
			params.setCouponName(param.getSearchKey());
		}
		if(null != param.getBusId() && param.getBusId()>0){
			params.setBusId(param.getBusId());
		}
		if(null != param.getShopId() && param.getShopId()>0){
			params.setShopId(param.getShopId());
		}
		QueryResult<CouponActivity> list = couponDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		model.put("bus", bus);
		
		List<BranchShop> shops = shopDao.getBranchShopByUser();
		model.put("shops", shops);
		
		model.put("list",list);
		model.put("pageInfo",param);
		return "system/coupon/list";
	}
	
	@RequestMapping("/add")
	public String add(Map<String, Object> model){
		List<Business> bus = busDao.getBusinessByUser();
		model.put("bus", bus);
		return "system/coupon/add";
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView insert(CouponActivity coupon){
		Business bus = busDao.find(coupon.getBusId());
		if(null== bus){
			return ajaxDoneError("没有找到商户信息");
		}
		setCouponBean(coupon);
		couponDao.save(coupon);
		return ajaxDoneSuccess("添加优惠信息成功");
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long id){
		couponDao.delete(id);
		return ajaxDoneSuccess("删除优惠信息成功");
	}
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable Long id,Model model){
		CouponActivity coupon = couponDao.find(id);
		model.addAttribute("coupon", coupon);
		List<Business> bus = busDao.getBusinessByUser();
		model.addAttribute("bus", bus);
		List<BranchShop> shops = shopDao.findByBusiness(coupon.getBusId());
		model.addAttribute("shops", shops);
		return "system/coupon/edit";
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public ModelAndView update(@PathVariable Long id,CouponActivity coupon){
		coupon.setId(id);
		setCouponBean(coupon);
		couponDao.update(coupon);
		return ajaxDoneSuccess("修改优惠信息成功");
	}

	/**
	 * 不同类型，设置不同的值
	 * @param coupon
	 */
	private void setCouponBean(CouponActivity coupon) {
		if(StringUtil.equals(coupon.getCouponType(), CouponActivity.CASH_TYPE)){
			coupon.setBeforeDay(null);
			coupon.setDiscount(null);
		}else if(StringUtil.equals(coupon.getCouponType(), CouponActivity.DISCOUNT_TYPE)){
			coupon.setBeforeDay(null);
			coupon.setFreeMoney(null);
		}if(StringUtil.equals(coupon.getCouponType(), CouponActivity.PERIOD_TYPE)){
			coupon.setFreeMoney(null);
			coupon.setStartDate(null);
			coupon.setEndDate(null);
		}
		coupon.setStatus("OK");
	}
	
}
