package com.booking.wechat.controller.protal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.booking.wechat.client.model.UserBean;
import com.booking.wechat.client.user.UserClient;
import com.booking.wechat.controller.protal.vo.BookingDateVO;
import com.booking.wechat.controller.protal.vo.RoomConfigVO;
import com.booking.wechat.controller.protal.vo.RoomDateVO;
import com.booking.wechat.controller.protal.vo.RoomInfoVO;
import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.bean.order.OrderItems;
import com.booking.wechat.persistence.bean.room.Room;
import com.booking.wechat.persistence.bean.room.RoomBookingRangeConfig;
import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.shop.ShopLinkConfig;
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.persistence.service.order.OrderItemsDao;
import com.booking.wechat.persistence.service.room.RoomBookingRangeConfigDao;
import com.booking.wechat.persistence.service.room.RoomDao;
import com.booking.wechat.persistence.service.shop.BranchShopDao;
import com.booking.wechat.persistence.service.shop.ShopLinkConfigDao;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;
import com.booking.wechat.util.DateUtils;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/protal/init")
public class BookingController {


	@Autowired
	private WechatConfigDao configDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private BranchShopDao shopDao;
	
	@Autowired
	private ConstantDao constantDao;
	
	@Autowired
	private OrderItemsDao orderItemDao;
	
	@Autowired
	private RoomBookingRangeConfigDao roomConfigDao;
	
	@Autowired
	private ShopLinkConfigDao shopLinkDao;

	@RequestMapping("/{systemCode}")
	public ModelAndView getProtalPageData(@PathVariable String systemCode,String code,Long shopId){
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean) session.getAttribute("WC_USER");
		WechatConfig config = configDao.selectBySystemCode(systemCode);
		if(null == config){
			return null;
		}
		List<BranchShop> shops = null;
		if(StringUtil.isNotEmpty(code) || null == user){
			UserClient client = new UserClient(config.getBusId());
			user = client.getUserInfoByAuthorize(code);
			System.out.println("打印一下用户的openId:" + user.getOpenid());
			session.setAttribute("WC_USER", user);
		}
		ModelAndView mv = new ModelAndView("wap/booking");
		shops = shopDao.findByBusiness(config.getBusId());
		mv.addObject("shops", shops);
		session.setAttribute("systemCode",systemCode);
		if(null == shopId){
			shopId = shops.get(0).getId();
		}
		mv.addObject("shopId",shopId);
		
		String shopName = "";
		for (BranchShop branchShop : shops) {
			if(StringUtil.equals(shopId, branchShop.getId())){
				shopName = branchShop.getShopName();
				break;
			}
		}
		List<Room> rooms = roomDao.findRoomByShop(shopId);
		mv.addObject("rooms", rooms);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date startDate = cal.getTime();
		cal.add(Calendar.DATE, 30);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date endDate = cal.getTime();
		
		Constant disableDate = constantDao.findByConstantId(config.getBusId(), "DISABLE_DATE");
		
		List<OrderItems> bookData = orderItemDao.selectByBusId(config.getBusId(), startDate, endDate);
		
		
		List<RoomDateVO> dates = new ArrayList<RoomDateVO>();
		Calendar bookingDate = Calendar.getInstance();
		
		Constant maxBookingDay = constantDao.findByConstantId(config.getBusId(), "MAX_BOOKING_DAY");
		for (int i = 0; i < Integer.valueOf(maxBookingDay.getConstantValue()); i++) {
			RoomDateVO date = new RoomDateVO();
			date.setBusId(config.getBusId());
			date.setShopId(shopId);
			date.setShopName(shopName);
			date.setBookingDate(bookingDate.getTime());
			date.setWeek(bookingDate.get(Calendar.DAY_OF_WEEK)-1);
			
			List<RoomInfoVO> infos = new ArrayList<RoomInfoVO>();
			for (Room room : rooms) {
				RoomInfoVO info = new RoomInfoVO();
				info.setRoomId(room.getId());
				info.setRoomName(room.getRoomName());
				info.setDiscount(null);//TODO discount 
				List<RoomConfigVO> configVo = new ArrayList<RoomConfigVO>();
				
				List<RoomBookingRangeConfig> roomConfigs = roomConfigDao.selectByRoomId(room.getId());
				for (RoomBookingRangeConfig roomConfig : roomConfigs) {
					if(date.getWeek()==roomConfig.getWeek()){
						RoomConfigVO timeOne = new RoomConfigVO();
						timeOne.setPrice(roomConfig.getRoomPrice());
						timeOne.setTimeRange(roomConfig.getTimeRange());
						timeOne.setStatus("OK");
						timeOne.setRangeId(roomConfig.getId()+"");
						//判断是否已经被预定
						for (OrderItems item : bookData) {
							if(DateUtils.compareDate(date.getBookingDate(), item.getReserveDate()) 
									&& StringUtil.equals(room.getId(), item.getRoomId()) 
									&& StringUtil.equals(item.getTimeRange(), roomConfig.getId())){
								timeOne.setStatus("SOLD");
							}
						}
						//禁用日期
						Date disBegin = null;
						Date disEnd = null;
						if(null != disableDate){
							String begin = disableDate.getConstantValue().split("-")[0];
							String end = disableDate.getConstantValue().split("-")[1];
							disBegin = DateUtils.string2Date(begin, "yyyy/MM/dd");
							disEnd = DateUtils.string2Date(end, "yyyy/MM/dd");
						}
						boolean disable = DateUtils.between(disBegin, disEnd, bookingDate.getTime());
						if(disable){
							timeOne.setStatus("DISABLE");
						}
						configVo.add(timeOne);
					}
				}
				info.setRoomConfigs(configVo);
				infos.add(info);
			}
			date.setRoomInfo(infos);
			dates.add(date);
			bookingDate.set(Calendar.DATE, bookingDate.get(Calendar.DATE)+1);
		}
		mv.addObject("dates", dates);
		return mv;
	}
	
	@RequestMapping("/v2/{systemCode}")
	public ModelAndView getNewPage(@PathVariable String systemCode,String code,Long shopId,Long time){
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean) session.getAttribute("WC_USER");
		WechatConfig config = configDao.selectBySystemCode(systemCode);
		if(null == config){
			return null;
		}
		List<BranchShop> shops = null;
		if(StringUtil.isNotEmpty(code) || null == user){
			UserClient client = new UserClient(config.getBusId());
			user = client.getUserInfoByAuthorize(code);
			session.setAttribute("WC_USER", user);
		}
		ModelAndView mv = new ModelAndView("wap/new");
		shops = shopDao.findByBusiness(config.getBusId());
		mv.addObject("shops", shops);
		session.setAttribute("systemCode",systemCode);
		if(null == shopId){
			shopId = shops.get(0).getId();
		}
		mv.addObject("shopId",shopId);
		
		List<ShopLinkConfig> shopLinks = shopLinkDao.selectByShopId(shopId);
		mv.addObject("shopLinks",shopLinks);
		
		List<BookingDateVO> weekDates = getWeekDate();
		mv.addObject("weekDates",weekDates);
		if(null == time){
			time = weekDates.get(0).getTime();
		}
		mv.addObject("currentDate", time);
		
		List<Room> rooms = roomDao.findRoomByShop(shopId);
		mv.addObject("rooms", rooms);
		
		List<RoomInfoVO> roomInfos = getRoomInfo(rooms, config.getBusId(), shopId, time);
		mv.addObject("roomInfos",roomInfos);
		return mv;
	}
	
	private List<RoomInfoVO> getRoomInfo(List<Room> rooms,Long busId,Long shopId,Long time){
		List<RoomInfoVO> infos = new ArrayList<RoomInfoVO>();
		Constant disableDate = constantDao.findByConstantId(busId, "DISABLE_DATE");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		int week = cal.get(Calendar.DAY_OF_WEEK)-1;
		Date startDate = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date endDate = cal.getTime();
		List<OrderItems> bookData = orderItemDao.selectByBusId(busId, startDate, endDate);
		for (Room room : rooms) {
			RoomInfoVO info = new RoomInfoVO();
			info.setRoomId(room.getId());
			info.setRoomName(room.getRoomName());
			info.setDiscount(null);//TODO discount 
			List<RoomConfigVO> configVo = new ArrayList<RoomConfigVO>();
			List<RoomBookingRangeConfig> roomConfigs = roomConfigDao.selectByRoomId(room.getId(),week);
			for (RoomBookingRangeConfig roomConfig : roomConfigs) {
				RoomConfigVO timeOne = new RoomConfigVO();
				timeOne.setPrice(roomConfig.getRoomPrice());
				timeOne.setTimeRange(roomConfig.getTimeRange());
				timeOne.setStatus("OK");
				timeOne.setRangeId(roomConfig.getId()+"");
				//判断是否已经被预定
				for (OrderItems item : bookData) {
					if(DateUtils.compareDate(new Date(time), item.getReserveDate()) 
							&& StringUtil.equals(room.getId(), item.getRoomId()) 
							&& StringUtil.equals(item.getTimeRange(), roomConfig.getId())){
						timeOne.setStatus("SOLD");
					}
				}
				//禁用日期
				Date disBegin = null;
				Date disEnd = null;
				if(null != disableDate){
					String begin = disableDate.getConstantValue().split("-")[0];
					String end = disableDate.getConstantValue().split("-")[1];
					disBegin = DateUtils.string2Date(begin, "yyyy/MM/dd");
					disEnd = DateUtils.string2Date(end, "yyyy/MM/dd");
				}
				boolean disable = DateUtils.between(disBegin, disEnd, new Date(time));
				if(disable){
					timeOne.setStatus("DISABLE");
				}
				configVo.add(timeOne);
			}
			info.setRoomConfigs(configVo);
			infos.add(info);
		}
		return infos;
	}
	
	private List<BookingDateVO> getWeekDate(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		List<BookingDateVO> dates = new ArrayList<BookingDateVO>();
		for (int i=0;i<7;i++) {
			BookingDateVO date = new BookingDateVO();
			date.setDate(cal.getTime());
			date.setTime(cal.getTimeInMillis());
			date.setWeek(cal.get(Calendar.DAY_OF_WEEK)-1);
			cal.set(Calendar.DATE, cal.get(Calendar.DATE)+1);
			dates.add(date);
		}
		return dates;
	}
}
