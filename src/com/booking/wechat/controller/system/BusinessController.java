package com.booking.wechat.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

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
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.common.CommonService;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/system/bus")
public class BusinessController extends BaseController{

	@Autowired
	private BusinessDao busDao;
	
	@Autowired
	private ConstantDao constantDao;
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long id){
		busDao.delete(id);
		return ajaxDoneSuccess("删除商户信息成功");
	}
	
	@RequestMapping("/list")
	public String getAllList(BaseParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		Business params = new Business();
		if(StringUtil.isNotEmpty(param.getSearchKey())){
			params.setBusName(param.getSearchKey());
		}
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		if(StringUtil.isNotEmpty(param.getOrderField())){
			orderby.put(param.getOrderField(), param.getOrderDirection());
		}
		QueryResult<Business> list = busDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage(),orderby);
		
		model.put("list",list);
		model.put("pageInfo",param);
		return "system/bus/list";
	}
	
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String getBusiness(@PathVariable Long id,Model model){
		Business bus = busDao.find(id);
		model.addAttribute("bus", bus);
		return "system/bus/edit";
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public ModelAndView updateBus(@PathVariable Long id,Business bus){
		Business oldBus = busDao.find(id);
		boolean updateName = StringUtil.equals(oldBus.getBusName(), bus.getBusName());
		bus.setId(id);
		bus.setLastUpdateTime(new Date());
		bus.setCreateTime(oldBus.getCreateTime());
		busDao.update(bus);
		if(!updateName){
			CommonService comm = new CommonService();
			comm.updateBusName(bus.getId(), bus.getBusName());
		}
		return ajaxDoneSuccess("修改商户信息成功");
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView addFaqList(Business bus){
		bus.setCreateTime(new Date());
		busDao.save(bus);
		
		initSysConfigData(bus);
		return ajaxDoneSuccess("添加商户信息成功");
	}
	
	private void initSysConfigData(Business bus){
		Constant mb = new Constant();
		mb.setBusId(bus.getId());
		mb.setBusName(bus.getBusName());
		mb.setConstantId("MONTHLY_BALANCE");
		mb.setConstantGroup("PADIAN");
		mb.setConstantName("月结赠送趴点比率");
		mb.setConstantValue("0.06");
		constantDao.save(mb);
		
		Constant cr = new Constant();
		cr.setBusId(bus.getId());
		cr.setBusName(bus.getBusName());
		cr.setConstantId("CONSUMPTION_RETURNS");
		cr.setConstantGroup("PADIAN");
		cr.setConstantName("趴点消费返还比率");
		cr.setConstantValue("0.05");
		constantDao.save(cr);
		
		Constant mmc = new Constant();
		mmc.setBusId(bus.getId());
		mmc.setBusName(bus.getBusName());
		mmc.setConstantId("MIN_MONTH_CONSUMPTION");
		mmc.setConstantGroup("PADIAN");
		mmc.setConstantName("消费之后，月结赠送趴点的月数");
		mmc.setConstantValue("3");
		constantDao.save(mmc);
		
		
		Constant outTime = new Constant();
		outTime.setBusId(bus.getId());
		outTime.setBusName(bus.getBusName());
		outTime.setConstantId("ORDER_OUT_TIME");
		outTime.setConstantGroup("ORDERS");
		outTime.setConstantName("订单支付超时时间，以分为单位");
		outTime.setConstantValue("15");
		constantDao.save(outTime);
		
		Constant coupon = new Constant();
		coupon.setBusId(bus.getId());
		coupon.setBusName(bus.getBusName());
		coupon.setConstantId("COUPON_TYPE");
		coupon.setConstantGroup("COUPON_CONFIG");
		coupon.setConstantName("优惠类型：PERIOD(周期)，DISCOUNT(普通)");
		coupon.setConstantValue("DISCOUNT");
		constantDao.save(coupon);
		
		Constant aliPayAccount = new Constant();
		aliPayAccount.setBusId(bus.getId());
		aliPayAccount.setBusName(bus.getBusName());
		aliPayAccount.setConstantId("ALIPAY_ACCOUNT");
		aliPayAccount.setConstantGroup("ALIPAY");
		aliPayAccount.setConstantName("支付宝账号");
		aliPayAccount.setConstantValue("");
		constantDao.save(aliPayAccount);
		
		Constant aliPayName = new Constant();
		aliPayName.setBusId(bus.getId());
		aliPayName.setBusName(bus.getBusName());
		aliPayName.setConstantId("ALIPAY_NAME");
		aliPayName.setConstantGroup("ALIPAY");
		aliPayName.setConstantName("支付宝名称");
		aliPayName.setConstantValue("");
		constantDao.save(aliPayName);
		
		
		Constant subscribe = new Constant();
		subscribe.setBusId(bus.getId());
		subscribe.setBusName(bus.getBusName());
		subscribe.setConstantId("SUBSCRIBE");
		subscribe.setConstantGroup("WECHAT_CONFIG");
		subscribe.setConstantName("微信关注欢迎语");
		subscribe.setConstantValue("欢迎关注");
		constantDao.save(subscribe);
		
		Constant disableRoom = new Constant();
		disableRoom.setBusId(bus.getId());
		disableRoom.setBusName(bus.getBusName());
		disableRoom.setConstantId("DISABLE_DATE");
		disableRoom.setConstantGroup("DISABLE_ROOM");
		disableRoom.setConstantName("不可预定日期");
		disableRoom.setConstantValue("2016/02/4-2016/02/18");
		constantDao.save(disableRoom);
		
		
		Constant maxBookingDay = new Constant();
		maxBookingDay.setBusId(bus.getId());
		maxBookingDay.setBusName(bus.getBusName());
		maxBookingDay.setConstantId("MAX_BOOKING_DAY");
		maxBookingDay.setConstantGroup("BOOKING");
		maxBookingDay.setConstantName("从当前日期往后可预订天数");
		maxBookingDay.setConstantValue("30");
		constantDao.save(maxBookingDay);
	}
}
