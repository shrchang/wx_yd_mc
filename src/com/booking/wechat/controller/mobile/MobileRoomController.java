package com.booking.wechat.controller.mobile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.mobile.vo.RoomConfigVO;
import com.booking.wechat.controller.mobile.vo.RoomDateVO;
import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.bean.order.OrderItems;
import com.booking.wechat.persistence.bean.room.Room;
import com.booking.wechat.persistence.bean.room.RoomBookingRangeConfig;
import com.booking.wechat.persistence.bean.room.RoomPicture;
import com.booking.wechat.persistence.bean.setmeal.SetMeal;
import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.persistence.service.order.OrderItemsDao;
import com.booking.wechat.persistence.service.room.RoomBookingRangeConfigDao;
import com.booking.wechat.persistence.service.room.RoomDao;
import com.booking.wechat.persistence.service.room.RoomPictureDao;
import com.booking.wechat.persistence.service.setmeal.SetMealDao;
import com.booking.wechat.persistence.service.shop.BranchShopDao;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;
import com.booking.wechat.util.DateUtils;
import com.booking.wechat.util.StringUtil;

/**
 * 场地的控制器
 * @ClassName RoomController
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月29日 下午3:20:59
 *
 */
@Controller
@RequestMapping("/mobile/rooms")
public class MobileRoomController extends BaseController {
	
	@Autowired
    private WechatConfigDao configDao;//商户配置
    
	@Autowired
    private BranchShopDao branchShopDao;//商铺
    
	@Autowired
    private RoomDao roomDao;//场地
	
	@Autowired
	private RoomPictureDao roomPictureDao;//场地的图片
	
	@Autowired
	private ConstantDao constantDao;//字典配置
	
	@Autowired
	private OrderItemsDao orderItemDao;//订单数据
	
	@Autowired
	private RoomBookingRangeConfigDao roomConfigDao;//场地配置
	
	@Autowired
	private SetMealDao setMealDao;//增值套餐
	
	/**
	 * 查询商铺与商铺下面的场地（按照道理来说 应该查询到当前的商铺名称与这个商品下面的所有的场地与他的价格）
	 * 按照道理来说 应该还需要将场地的销量也加进去 后期再说 可能会影响到数据查询的问题
	 * @author shrChang.Liu
	 * @param systemCode
	 * @return
	 * @date 2018年10月29日 下午3:27:14
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> queryShopAndRooms(@RequestParam(value="systemCode") String systemCode){
		WechatConfig config = configDao.selectBySystemCode(systemCode);
		if(config == null){
			return getFailResultMap("没有找到对应的商户！");
		}
		//下面就是查询了
		List<BranchShop> shops = branchShopDao.findByBusiness(config.getBusId());
		//根据每个店铺去查询对应的场地
		for(BranchShop shop : shops){
			Long shopId = shop.getId();
			shop.setRoomInfos(roomDao.findRoomsByShopId(shopId));
		}
		return getSuccessResultMap("获取店铺列表数据成功!", shops);
	}
	
	/**
	 * 根据场地id查询对应的信息，
	 * 如价格表，说明，详情，或者还有评价（后续），还有增值套餐
	 * @author shrChang.Liu
	 * @param roomId
	 * @return
	 * @date 2018年10月29日 下午5:27:14
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/detail",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> queryRoomDetail(@RequestParam(value="id")Long roomId){
		Date start = new Date();
		System.out.println("开始时间：" + start.getTime());
		Room room = roomDao.find(roomId);
		//获取对应的图片
		List<RoomPicture> pictures = roomPictureDao.findPicturesByRoomId(roomId);
		room.setPictures(pictures);
		//还有就是获取对应的时间咯 和他对应的场地预定的东西 默认查询一个月的数据
		//需要禁用的日期
		Constant disableDate = constantDao.findByConstantId(room.getBusId(), "DISABLE_DATE");
		//这个区间已经下单的数据的这个场地已经
		List<OrderItems> bookData = orderItemDao.selectAfterTodayItemsByRoomId(roomId);
		//接下来生成一个月的数据谢谢合作 最大可预订的数量
		Constant maxBookingDay = constantDao.findByConstantId(room.getBusId(), "MAX_BOOKING_DAY");
		//查询场地的价格配置
		List<RoomBookingRangeConfig> roomConfigs = roomConfigDao.selectByRoomId(room.getId());
		Map<String,List<RoomBookingRangeConfig>> configMap = new HashMap<String, List<RoomBookingRangeConfig>>();
		for(RoomBookingRangeConfig rbrc : roomConfigs){
			if(rbrc.getRoomPrice() == null)
				continue;
			List<RoomBookingRangeConfig> roomBookingRangeConfigs = new ArrayList<RoomBookingRangeConfig>();
			if(configMap.containsKey(String.valueOf(rbrc.getWeek()))){
				roomBookingRangeConfigs = configMap.get(String.valueOf(rbrc.getWeek()));
			}
			roomBookingRangeConfigs.add(rbrc);
			//覆盖
			configMap.put(String.valueOf(rbrc.getWeek()), roomBookingRangeConfigs);
		}
		//获取预定的时间范围
		Calendar bookingDate = Calendar.getInstance();
		List<RoomDateVO> vos = new ArrayList<RoomDateVO>();
		System.out.println("查询数据耗时：" + (new Date().getTime()-start.getTime()));
		for (int i = 0; i < Integer.valueOf(maxBookingDay.getConstantValue()); i++) {
			RoomDateVO date = new RoomDateVO();
			date.setBusId(room.getBusId());
			date.setShopId(room.getShopId());
			date.setShopName(room.getShopName());
			date.setBookingDate(bookingDate.getTime());
			date.setWeek(bookingDate.get(Calendar.DAY_OF_WEEK)-1);
			date.setRoomId(roomId);
			date.setRoomName(room.getRoomName());
			//下面的就是场地的配置的了 注意了
			List<RoomBookingRangeConfig> bookingRangeConfigs = configMap.get(String.valueOf(date.getWeek()));
			//获取这个周几的配置
			List<RoomConfigVO> configVOs = new ArrayList<RoomConfigVO>();
			for(RoomBookingRangeConfig roomBookingRangeConfig : bookingRangeConfigs){
				RoomConfigVO roomConfigVO = new RoomConfigVO();
				roomConfigVO.setBookingPriceRate(roomBookingRangeConfig.getBookingPriceRate());
				roomConfigVO.setPrice(roomBookingRangeConfig.getRoomPrice());
				roomConfigVO.setTimeRange(roomBookingRangeConfig.getTimeRange());
				roomConfigVO.setStatus("OK");
				roomConfigVO.setRangeId(roomBookingRangeConfig.getId()+"");
				//判断是否已经被预定
				for (OrderItems item : bookData) {
					if(DateUtils.compareDate(date.getBookingDate(), item.getReserveDate()) 
							&& StringUtil.equals(room.getId(), item.getRoomId()) 
							&& StringUtil.equals(item.getTimeRange(), roomBookingRangeConfig.getId())){
						roomConfigVO.setStatus("SOLD");
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
					roomConfigVO.setStatus("DISABLE");
				}
				configVOs.add(roomConfigVO);
			}
			date.setConfigVOs(configVOs);
			vos.add(date);//添加到这里
			bookingDate.set(Calendar.DATE, bookingDate.get(Calendar.DATE)+1);//加1天
		}
		System.out.println("最后耗时：" + (new Date().getTime()-start.getTime()));
		room.setRoomDateVOs(vos);
		return getSuccessResultMap("获取场地详情成功！", room);
	}
	
	/**
	 * 查询套餐
	 * @author shrChang.Liu
	 * @param shopId
	 * @return
	 * @date 2018年10月30日 上午11:01:58
	 * @return Map<String,String>
	 * @description
	 */
	@RequestMapping(value="/queryMealSet",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> queryMealSetByShopId(@RequestParam(value="shopId")Long shopId){
		List<SetMeal> setMeals = setMealDao.findByShopId(shopId);
		return getSuccessResultMap("查询增值套餐成功！", setMeals);
	}

}
