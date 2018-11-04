package com.booking.wechat.controller.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.vo.BaseParamVO;
import com.booking.wechat.persistence.bean.bus.Business;
import com.booking.wechat.persistence.bean.room.Room;
import com.booking.wechat.persistence.bean.room.RoomBookingRangeConfig;
import com.booking.wechat.persistence.bean.room.RoomPicture;
import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.common.CommonService;
import com.booking.wechat.persistence.service.room.RoomBookingRangeConfigDao;
import com.booking.wechat.persistence.service.room.RoomDao;
import com.booking.wechat.persistence.service.room.RoomPictureDao;
import com.booking.wechat.persistence.service.shop.BranchShopDao;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/system/rooms")
public class RoomController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(RoomController.class);

	private static String UPLOAD_PATH = "/upload/room";// 上传的地址
	
	private static String FILE_VISIT_PATH = "/webchatBook/images";//访问路径

	@Autowired
	private BusinessDao busDao;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private BranchShopDao shopDao;

	@Autowired
	private RoomPictureDao pictureDao;

	@Autowired
	private RoomBookingRangeConfigDao roomConfigDao;

	@RequestMapping("/list")
	public String getAllList(BaseParamVO param, Map<String, Object> model) throws UnsupportedEncodingException {
		Room params = new Room();

		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		List<Business> bus = busDao.getBusinessByUser();
		// 没有系统管理员权限，根据商户查询
		if (!subject.hasRole("admin")) {
			params.setBusId(user.getBusId());
		}
		if (StringUtil.isNotEmpty(param.getSearchKey())) {
			params.setRoomName(param.getSearchKey());
		}
		if (null != param.getBusId() && param.getBusId() > 0) {
			params.setBusId(param.getBusId());
		}
		if (null != param.getShopId() && param.getShopId() > 0) {
			params.setShopId(param.getShopId());
		}
		QueryResult<Room> list = roomDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage());
		model.put("bus", bus);

		List<BranchShop> shops = shopDao.getBranchShopByUser();
		model.put("shops", shops);

		model.put("list", list);
		model.put("pageInfo", param);
		return "system/room/list";
	}

	@RequestMapping("/add")
	public String add(Map<String, Object> model) {
		List<Business> bus = busDao.getBusinessByUser();
		model.put("bus", bus);
		return "system/room/add";
	}
	
	/**
	 * 删除场地的图片
	 * @author shrChang.Liu
	 * @param id
	 * @return
	 * @date 2018年10月17日 下午2:44:17
	 * @return Map<String,String>
	 * @description
	 */
	@RequestMapping(value="/picture/del",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delRoomPicture(@RequestParam("id")Long id){
		pictureDao.delete(id);
		return getSuccessResultMap("删除场地图片成功!");
	}

	/**
	 * 上传对象
	 * 
	 * @author shrChang.Liu
	 * @param file
	 * @return
	 * @date 2018年10月16日 下午3:16:28
	 * @return Map<String,String>
	 * @description
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> upload(@RequestParam("file") MultipartFile file,
			@RequestParam(value="roomId",required=false)Long roomId) {
		Map<String, String> res = new HashMap<String, String>();
		try {
			File saveFile = new File(UPLOAD_PATH);
			if (!saveFile.exists()) {
				saveFile.mkdirs();
			}
			logger.info("原始文件名：" + file.getOriginalFilename());
			String oldName = file.getOriginalFilename();
			String fileName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
			logger.info("新文件名：" + fileName);
			String filePath = UPLOAD_PATH + "/" + fileName;//真实地址
			FileOutputStream fos = new FileOutputStream(filePath);
			IOUtils.copy(file.getInputStream(), fos);
			String visitPath = FILE_VISIT_PATH+"/"+fileName;
			res.put("path", visitPath);
			res.put("realPath", filePath);
			res.put("fileName", fileName);
			res.put("oldName", oldName);
			res.put("retCode", "200");
			res.put("retMsg", "上传成功");
			if(roomId != null){
				RoomPicture rp = new RoomPicture();
				rp.setPictureDesc("图片上传成功");
				rp.setPictureName(oldName);
				rp.setPictureRealName(fileName);
				rp.setPictureRealPath(filePath);
				rp.setPictureSrc(visitPath);
				rp.setRoomId(roomId);
				pictureDao.save(rp);
				res.put("id", String.valueOf(rp.getId()));
			}
		} catch (Exception e) {
			logger.error("上传文件报错：",e);
			res.put("retCode", "500");
			res.put("retMsg", "上传失败");
		}
		return res;
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ModelAndView add(@RequestBody Room room) {
		Business bus = busDao.find(room.getBusId());
		if (null == bus) {
			return ajaxDoneError("没有找到商户信息");
		}

		List<Room> rooms = roomDao.findRoomByBus(room.getBusId());
		if (rooms.size() >= bus.getMaxRoom()) {// 商户最大场地数已经大于或者等于限制
			return ajaxDoneError("商户的最大场地数超出限制，不能再创建场地");
		}
		roomDao.save(room);
		for (int i = 0; i < 7; i++) {
			if (StringUtil.isNotEmpty(room.getTimeOne())) {
				saveRoomBookingConfig(room, i, room.getTimeOne());
			}
			if (StringUtil.isNotEmpty(room.getTimeTwo())) {
				saveRoomBookingConfig(room, i, room.getTimeTwo());
			}
			if (StringUtil.isNotEmpty(room.getTimeThree())) {
				saveRoomBookingConfig(room, i, room.getTimeThree());
			}
		}
		// 保存图片配置
		if (room.getPictures().size() > 0) {
			for (RoomPicture rp : room.getPictures()) {
				rp.setRoomId(room.getId());
				pictureDao.save(rp);
			}
		}

		return ajaxDoneSuccess("添加场地信息成功");
	}

	private void saveRoomBookingConfig(Room room, int i, String timeRange) {
		RoomBookingRangeConfig config = new RoomBookingRangeConfig();
		config.setRoomId(room.getId());
		config.setBusId(room.getBusId());
		config.setShopId(room.getShopId());
		config.setShopName(room.getShopName());
		config.setRoomName(room.getRoomName());
		config.setBookingPriceRate(room.getBookingPriceRate());
		config.setWeek(i);
		config.setTimeRange(timeRange);
		roomConfigDao.save(config);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long id) {
		roomDao.delete(id);
		roomConfigDao.deleteByRoomId(id);
		pictureDao.deleteByRoomId(id);
		return ajaxDoneSuccess("删除场地信息成功");
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editRoom(@PathVariable Long id, Model model) {
		Room room = roomDao.find(id);
		room.setPictures(pictureDao.findPicturesByRoomId(id));
		model.addAttribute("room", room);
		List<Business> bus = busDao.getBusinessByUser();
		model.addAttribute("bus", bus);
		List<BranchShop> shops = shopDao.findByBusiness(room.getBusId());
		model.addAttribute("shops", shops);
		return "system/room/edit";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public ModelAndView updateBean(@PathVariable Long id, Room room) {
		room.setId(id);
		Room oldRoom = roomDao.find(id);
		boolean updateName = StringUtil.equals(oldRoom.getRoomName(), room.getRoomName());
		roomDao.update(room);
		if (!updateName) {
			CommonService comm = new CommonService();
			comm.updateRoomName(room.getId(), room.getRoomName());
		}
		return ajaxDoneSuccess("修改场地信息成功");
	}

	@RequestMapping("/findByShop/{shopId}")
	public @ResponseBody List<Object[]> findByBus(@PathVariable Long shopId) {
		List<Object[]> list = new ArrayList<Object[]>();
		Object[] select = new Object[2];
		select[0] = "-1";
		select[1] = "请选择场地";
		list.add(select);
		if (null != shopId && shopId > 0) {
			List<Room> rooms = roomDao.findRoomByShop(shopId);
			for (Room room : rooms) {
				Object[] shop = new Object[2];
				shop[0] = room.getId();
				shop[1] = room.getRoomName();
				list.add(shop);
			}
		}
		return list;
	}
}
