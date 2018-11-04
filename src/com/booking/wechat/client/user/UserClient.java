package com.booking.wechat.client.user;


import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.booking.wechat.client.BaseClient;
import com.booking.wechat.client.model.UserBean;
import com.booking.wechat.restlet.HttpClientUtils;
import com.booking.wechat.token.WeixinToken;
import com.booking.wechat.util.EmojiFilter;

public class UserClient extends BaseClient{
	
	public UserClient(Long busId) {
		super(busId);
	}


	public List<UserBean> getUserList(){
		List<UserBean> userList = new ArrayList<UserBean>();
		String strb = null;
		String baseUser = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+WeixinToken.getAccessToken(config).getAccess_token();
		
		try {
			strb = HttpClientUtils.httpGet(baseUser);
			JSONObject json = JSONObject.fromObject(strb);
			JSONObject openIdObj = (JSONObject)json.get("data");
			JSONArray openIds = openIdObj.getJSONArray("openid");
			JSONObject postBody = new JSONObject();
			JSONArray user_list = new JSONArray();
			
			//TODO 这里官方说只能最多批量获取100条数据
			for (int i=0;i<openIds.size();i++) {
				JSONObject user = new JSONObject();
				user.put("openid", openIds.getString(i));
				user.put("lang", "zh-CN");
				user_list.add(user);
			}
			postBody.put("user_list", user_list);
			
			String userInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token="+WeixinToken.getAccessToken(config).getAccess_token();
			String userJson = HttpClientUtils.httpPost(userInfoUrl,postBody.toString());
			
			JSONObject userInfoJson = JSONObject.fromObject(userJson);
			JSONArray userInfoArrayJson = userInfoJson.getJSONArray("user_info_list");
			for (int i = 0; i < userInfoArrayJson.size(); i++) {
				UserBean bean = (UserBean) JSONObject.toBean(userInfoArrayJson.getJSONObject(i),UserBean.class);
				bean.setNickname(EmojiFilter.filterEmoji(bean.getNickname()));
				userList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	
	public UserBean getUserInfo(String openId){
		String userInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+WeixinToken.getAccessToken(config).getAccess_token()+"&openid="+openId+"&lang=zh_CN";
		String userJson = HttpClientUtils.httpGet(userInfoUrl);
		JSONObject jsonObj = JSONObject.fromObject(userJson);
		UserBean bean = (UserBean) JSONObject.toBean(jsonObj,UserBean.class);
		bean.setNickname(EmojiFilter.filterEmoji(bean.getNickname()));//过滤EMOJI表情，BAE不支持
		return bean;
	}
	
	public UserBean getUserInfoByAuthorize(String code){
		String getTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+config.getAppId()+"&secret="+config.getSecret()+"&code="+code+"&grant_type=authorization_code";
		String token = HttpClientUtils.httpGet(getTokenUrl);
		try {
			JSONObject tokenJson = JSONObject.fromObject(token);
			String openId = tokenJson.getString("openid");
			return getUserInfo(openId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
