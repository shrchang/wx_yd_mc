package com.booking.wechat.client.menu;

import net.sf.json.JSONObject;

import com.booking.wechat.client.BaseClient;
import com.booking.wechat.client.model.button.BaseButton;
import com.booking.wechat.client.model.button.ClickButton;
import com.booking.wechat.client.model.button.ComplexButton;
import com.booking.wechat.client.model.button.GetMenus;
import com.booking.wechat.client.model.button.Menu;
import com.booking.wechat.client.model.button.ViewButton;
import com.booking.wechat.restlet.HttpClientUtils;
import com.booking.wechat.token.WeixinToken;

/**
 * 创建自定义菜单
 * @author Luoxx
 *
 */
public class MenuClient extends BaseClient{

	public MenuClient(Long busId) {
		super(busId);
	}

	public String getMenuData() throws Exception{
		Menu menu = new Menu();
		ViewButton yuding = new ViewButton();
		yuding.setName("预订");
		yuding.setType("view");
		MenuUrlUtil urlUtil = new MenuUrlUtil(busId);
		
		yuding.setUrl(urlUtil.getCodeRequest("http://wechatsite.wicp.net/wechatsite/index.jsp", MenuUrlUtil.SCOPE_BASE));
		
		ViewButton order = new ViewButton();
		order.setName("我的订单");
		order.setType("view");
		order.setUrl(urlUtil.getCodeRequest("http://wechatsite.wicp.net/wechatsite/myOrders.jsp", MenuUrlUtil.SCOPE_BASE));
		
		ComplexButton userCenter = new ComplexButton();
		userCenter.setName("会员中心");
		ClickButton chaxun = new ClickButton();
		chaxun.setKey("查询余额");
		chaxun.setName("查询余额");
		chaxun.setType("click");
		
		ClickButton vip = new ClickButton();
		vip.setKey("成为会员");
		vip.setName("成为会员");
		vip.setType("click");
		
		ClickButton news = new ClickButton();
		news.setKey("路线指引");
		news.setName("路线指引");
		news.setType("click");
		
		userCenter.setSub_button(new BaseButton[]{chaxun,vip,news});
		
		menu.setButton(new BaseButton[]{yuding,order,userCenter});
		String json = JSONObject.fromObject(menu).toString();
		return json;
	}
	
	public String createMenu(String postBody){
		String strb = null;
		String accessToken = WeixinToken.getAccessToken(config).getAccess_token();
		String createMenu = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
		try {
			strb = HttpClientUtils.httpPost(createMenu, postBody);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strb;
	}
	
	public GetMenus getMenu(){
		String accessToken = WeixinToken.getAccessToken(config).getAccess_token();
		String getMenuUrl = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="+accessToken;
		String strb = null;
		try {
			strb = HttpClientUtils.httpGet(getMenuUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jsonObj = JSONObject.fromObject(strb);
		GetMenus menu = (GetMenus) JSONObject.toBean(jsonObj.getJSONObject("menu"), GetMenus.class);
		return menu;
	}
	
	public String deleteMenu(){
		String strb = null;
		String accessToken = WeixinToken.getAccessToken(config).getAccess_token();
		String deleteUrl = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+accessToken;
		
		try {
			strb = HttpClientUtils.httpGet(deleteUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strb;
	}
	
}
