package com.booking.wechat.controller.mobile;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.booking.wechat.controller.BaseController;
import com.booking.wechat.persistence.bean.room.Room;
import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.room.RoomDao;
import com.booking.wechat.persistence.service.shop.BranchShopDao;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;

/**
 * 商品控制器
 * @ClassName MobileShopController
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年11月4日 下午7:44:38
 *
 */
@Controller
@RequestMapping("/mobile/shops")
public class MobileShopController extends BaseController {
	
	@Autowired
    private WechatConfigDao configDao;//商户配置
    
	@Autowired
    private BranchShopDao branchShopDao;//商铺
    
	@Autowired
    private RoomDao roomDao;//场地

	/**
	 * 查询商铺列表
	 * @author shrChang.Liu
	 * @param systemCode
	 * @return
	 * @date 2018年11月4日 下午7:46:46
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> queryShops(@RequestParam("systemCode")String systemCode){
		WechatConfig config = configDao.selectBySystemCode(systemCode);
		if(config == null){
			return getFailResultMap("没有找到对应的商户！");
		}
		List<BranchShop> shops = branchShopDao.findByBusiness(config.getBusId());
		return getSuccessResultMap("获取商铺成功!",shops);
	}
	
	/**
	 * 查询商铺下面的场地
	 * @author shrChang.Liu
	 * @param shopId
	 * @return
	 * @date 2018年11月4日 下午7:48:50
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/queryRooms",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> queryRoomByShopId(@RequestParam("shopId")Long shopId){
		List<Room> rooms = roomDao.findRoomByShop(shopId);
		return getSuccessResultMap("获取场地成功！",rooms);
	}
}
