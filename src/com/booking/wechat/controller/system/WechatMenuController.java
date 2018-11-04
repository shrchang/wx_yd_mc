package com.booking.wechat.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.booking.wechat.client.material.MaterialClient;
import com.booking.wechat.client.menu.MenuClient;
import com.booking.wechat.client.menu.MenuUrlUtil;
import com.booking.wechat.client.model.button.BaseButton;
import com.booking.wechat.client.model.button.ClickButton;
import com.booking.wechat.client.model.button.ComplexButton;
import com.booking.wechat.client.model.button.GetMenuInfo;
import com.booking.wechat.client.model.button.GetMenus;
import com.booking.wechat.client.model.button.Menu;
import com.booking.wechat.client.model.button.ViewButton;
import com.booking.wechat.client.model.material.MaterialBean;
import com.booking.wechat.client.model.material.MaterialContentBean;
import com.booking.wechat.client.model.material.MaterialItemBean;
import com.booking.wechat.client.model.material.MaterialNewsItem;
import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.vo.BaseParamVO;
import com.booking.wechat.persistence.bean.bus.Business;
import com.booking.wechat.persistence.bean.menu.WechatMenu;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.menu.WechatMenuDao;
import com.booking.wechat.util.CompareHelper;
import com.booking.wechat.util.ListUtils;
import com.booking.wechat.util.StringUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/system/menus")
public class WechatMenuController extends BaseController{

	@Autowired
	private WechatMenuDao menuDao;
	
	@Autowired
	private BusinessDao busDao;
	
	@RequestMapping("/list")
	public String getAllList(BaseParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		WechatMenu params = new WechatMenu();
		
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
		LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
		orderby.put("orderNumber", "asc");
		QueryResult<WechatMenu> list = menuDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage(),orderby);
		model.put("bus", bus);
		model.put("list",list);
		model.put("pageInfo",param);
		return "system/menu/list";
	}
	
	@RequestMapping("/add")
	public String addBean(Map<String, Object> model) throws Exception{
		
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		
		WechatMenu params = new WechatMenu();
		params.setParentId(-1l);
		params.setBusId(user.getBusId());
		List<WechatMenu> parents = menuDao.findByExample(params,true).getRows();
		model.put("parents", parents);
		
		List<Map<String, String>> materials = getMaterial(user.getBusId());
		model.put("materials", materials);
		
		return "system/menu/add";
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ModelAndView insertBean(WechatMenu menu,String customMenuUrl){
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		menu.setBusId(user.getBusId());
		menu.setBusName(user.getBusName());
		if("".equals(menu.getType()) || menu.getParentId().longValue() == -1){
			WechatMenu params = new WechatMenu();
			params.setParentId(-1l);
			menu.setParentId(-1l);
			List<WechatMenu> menus = menuDao.findByExample(params,true).getRows();
			if(ListUtils.isNotEmpty(menus)&&menus.size()>=3){
				return ajaxDoneError("一级菜单最多创建三个");
			}
		}else{
			WechatMenu parentMenu = menuDao.find(menu.getParentId());
			if("view".equals(parentMenu.getType())){
				return ajaxDoneError("父级菜单类型不支持子菜单，请修改父级菜单");
			}
		}
		if("click".equals(menu.getType())){
			menu.setKey(menu.getName());
			menu.setUrl(null);
		}
		if(StringUtil.equals("-1", menu.getUrl())){
			menu.setUrl(null);
		}
		if(StringUtil.equals("-2", menu.getUrl())){
			menu.setUrl(customMenuUrl);
		}
		if("view".equals(menu.getType()) && null == menu.getUrl()){
			return ajaxDoneError("链接的URL不能为空");
		}
		menuDao.save(menu);
		return ajaxDoneSuccess("添加微信菜单信息成功");
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long id){
		WechatMenu params = new WechatMenu();
		params.setParentId(id);
		List<WechatMenu> menus = menuDao.findByExample(params,true).getRows();
		if(ListUtils.isNotEmpty(menus)){
			return ajaxDoneError("删除的菜单有二级菜单，请先删除二级菜单");
		}
		menuDao.delete(id);
		return ajaxDoneSuccess("删除微信菜单信息成功");
	}
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String getBean(@PathVariable Long id,Model model) throws Exception{
		WechatMenu menu = menuDao.find(id);
		model.addAttribute("menu", menu);
		
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		
		WechatMenu params = new WechatMenu();
		params.setParentId(-1l);
		params.setBusId(user.getBusId());
		List<WechatMenu> parents = menuDao.findByExample(params,true).getRows();
		model.addAttribute("parents", parents);
		
		List<Map<String, String>> materials = getMaterial(user.getBusId());
		model.addAttribute("materials", materials);
		
		return "system/menu/edit";
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public ModelAndView updateBean(@PathVariable Long id,WechatMenu menu,String customMenuUrl){
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		WechatMenu oldmenu = menuDao.find(id);
		menu.setBusId(user.getBusId());
		menu.setBusName(user.getBusName());
		menu.setMenuId(id);
		
		if("".equals(menu.getType()) || menu.getParentId().longValue() == -1){
			menu.setParentId(-1l);
			WechatMenu params = new WechatMenu();
			params.setParentId(-1l);
			params.setBusId(user.getBusId());
			List<WechatMenu> menus = menuDao.findByExample(params,true).getRows();
			int maxSize = 3;
			if(oldmenu.getParentId().longValue() == -1){
				maxSize = 4;
			}
			if(ListUtils.isNotEmpty(menus)&&menus.size()>=maxSize){
				return ajaxDoneError("一级菜单最多创建三个");
			}
		}else{
			WechatMenu parentMenu = menuDao.find(menu.getParentId());
			if("view".equals(parentMenu.getType())){
				return ajaxDoneError("父级菜单类型不支持子菜单，请修改父级菜单");
			}
		}
		if("click".equals(menu.getType())){
			menu.setKey(menu.getName());
			menu.setUrl(null);
		}
		if(StringUtil.equals("-1", menu.getUrl())){
			menu.setUrl(null);
		}
		if(StringUtil.equals("-2", menu.getUrl())){
			menu.setUrl(customMenuUrl);
		}
		if("view".equals(menu.getType()) && null == menu.getUrl()){
			return ajaxDoneError("链接的URL不能为空");
		}
		
		menuDao.update(menu);
		return ajaxDoneSuccess("修改微信菜单信息成功");
	}
	
	@RequestMapping("/findParentByBus/{busId}")
	public @ResponseBody List<Object[]> findByBus(@PathVariable Long busId){
		List<Object[]> list = new ArrayList<Object[]>();
		Object[] select = new Object[2];
		select[0] = "-1";
		select[1] = "请选择父级菜单";
		list.add(select);
		if(null != busId && busId>0){
			WechatMenu params = new WechatMenu();
			params.setParentId(-1l);
			params.setBusId(busId);
			List<WechatMenu> menus = menuDao.findByExample(params,true).getRows();
			for (WechatMenu menu : menus) {
				Object[] obj = new Object[2];
				obj[0] = menu.getMenuId();
				obj[1] = menu.getName();
				list.add(obj);
			}
		}
		return list;
	}
	
	
	private List<Map<String,String>> getMaterial(Long busId) throws Exception{
		MaterialClient client = new MaterialClient(busId);
		MaterialBean bean = client.getMaterialList();
		MaterialItemBean[] item = bean.getItem();
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		if(item==null){
			return result;
		}
		for (MaterialItemBean materialItemBean : item) {
			MaterialContentBean content = materialItemBean.getContent();
			MaterialNewsItem newsItem = content.getNews_item()[0];
			Map<String,String> map= new HashMap<String,String>();
			map.put("title", newsItem.getTitle());
			map.put("url", newsItem.getUrl());
			map.put("mediaId", materialItemBean.getMedia_id());
			result.add(map);
		}
		return result;
	}
	
	
	/**
	 * 同步微信菜单数据
	 */
	@RequestMapping(value="/syncMenus",method=RequestMethod.POST)
	public ModelAndView syncWechatMenus() {
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		
		if(null == user.getBusId()){
			return ajaxDoneError("当前登陆用户不属于任何商户");
		}
		menuDao.deleteByBusId(user.getBusId());
		
		GetMenus menu = new MenuClient(user.getBusId()).getMenu();
		GetMenuInfo[] buttons = menu.getButton();
		
		int i = 1;
		for (GetMenuInfo button : buttons) {
			WechatMenu bean = new WechatMenu();
			bean.setKey(button.getKey());
			bean.setName(button.getName());
			bean.setType(button.getType());
			bean.setUrl(button.getUrl());
			bean.setParentId(-1l);
			bean.setOrderNumber(i+"");
			bean.setBusId(user.getBusId());
			bean.setBusName(user.getBusName());
			menuDao.save(bean);
			i++;
			int j = 1;
			GetMenuInfo[] subButtons = button.getSub_button();
			for (GetMenuInfo subButton : subButtons) {
				WechatMenu subBean = new WechatMenu();
				subBean.setKey(subButton.getKey());
				subBean.setName(subButton.getName());
				subBean.setType(subButton.getType());
				subBean.setUrl(subButton.getUrl());
				subBean.setParentId(bean.getMenuId());
				subBean.setOrderNumber(i+""+j);
				subBean.setBusId(user.getBusId());
				subBean.setBusName(user.getBusName());
				menuDao.save(subBean);
				j++;
			}
		}
		return ajaxDoneSuccess("同步微信菜单信息成功");
	}
	
	
	/**
	 * 调用微信接口，创建菜单
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/createWechatMenu",method=RequestMethod.POST)
	public ModelAndView createWechatMenu(){
		
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		if(null == user.getBusId()){
			return ajaxDoneError("当前登陆用户不属于任何商户");
		}
		WechatMenu params = new WechatMenu();
		params.setParentId(-1l);
		params.setBusId(user.getBusId());
		List<WechatMenu> menus = menuDao.findByExample(params,true).getRows();
		menus = CompareHelper.singleSort(menus, "orderNumber", 0);
		Menu post = new Menu();
		BaseButton[] buttons = new BaseButton[menus.size()];
		MenuUrlUtil urlUtil = new MenuUrlUtil(user.getBusId());
		for (int i=0;i<menus.size();i++) {
			WechatMenu wechatMenu = menus.get(i);
			//URL
			if("view".equals(wechatMenu.getType())){
				ViewButton view = new ViewButton();
				view.setName(wechatMenu.getName());
				view.setType("view");
				view.setUrl(urlUtil.getCodeRequest(wechatMenu.getUrl(),MenuUrlUtil.SCOPE_BASE));
				buttons[i] = view;
			}
			//有子菜单
			else{
				ComplexButton complex = new ComplexButton();
				complex.setName(wechatMenu.getName());
				WechatMenu cparams = new WechatMenu();
				cparams.setParentId(wechatMenu.getMenuId());
				cparams.setBusId(user.getBusId());
				List<WechatMenu> complexButtons = menuDao.findByExample(cparams,true).getRows();
				complexButtons = CompareHelper.singleSort(complexButtons, "orderNumber", 0);
				BaseButton[] subButtons = new BaseButton[complexButtons.size()];
				for (int j = 0; j < complexButtons.size(); j++) {
					WechatMenu complexButton = complexButtons.get(j);
					if("view".equals(complexButton.getType())){
						ViewButton view = new ViewButton();
						view.setName(complexButton.getName());
						view.setType("view");
//						view.setUrl(complexButton.getUrl());
						view.setUrl(urlUtil.getCodeRequest(complexButton.getUrl(),MenuUrlUtil.SCOPE_BASE));
						subButtons[j] = view;
					}
					else if("click".equals(complexButton.getType())){
						ClickButton click = new ClickButton();
						click.setKey(complexButton.getKey());
						click.setName(complexButton.getName());
						click.setType("click");
						subButtons[j] = click;
					}
				}
				complex.setSub_button(subButtons);
				buttons[i] = complex;
			}
		}
		post.setButton(buttons);	
		MenuClient menuClient = new MenuClient(user.getBusId());
		String resultJson = menuClient.createMenu(JSONObject.fromObject(post).toString());
		JSONObject jsonObj = JSONObject.fromObject(resultJson);
		if("0".equals(jsonObj.getString("errcode"))){
			return ajaxDoneSuccess("发布成功！");
		}else{
			return ajaxDoneError("发布失败："+jsonObj.getString("errmsg"));
		}
	}
}
