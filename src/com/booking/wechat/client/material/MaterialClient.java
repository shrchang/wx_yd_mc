package com.booking.wechat.client.material;

import net.sf.json.JSONObject;

import com.booking.wechat.client.BaseClient;
import com.booking.wechat.client.model.material.MaterialBean;
import com.booking.wechat.restlet.HttpClientUtils;
import com.booking.wechat.token.WeixinToken;

/**
 * 获取素材信息，添加在微信后台添加
 * @author Luoxx
 *
 */
public class MaterialClient extends BaseClient{
	
	public MaterialClient(Long busId) {
		super(busId);
	}

	public MaterialBean getMaterialList() throws Exception{
		String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token="+WeixinToken.getAccessToken(config).getAccess_token();
		JSONObject post = new JSONObject();
		post.put("type", "news");
		post.put("offset", "0");
		post.put("count", "20");
		String result = HttpClientUtils.httpPost(url,post.toString());
		MaterialBean bean = (MaterialBean) JSONObject.toBean(JSONObject.fromObject(result), MaterialBean.class);
		return bean;
	}
	
	public String getMaterial(String mediaId) throws Exception{
		String url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token="+WeixinToken.getAccessToken(config).getAccess_token();
		JSONObject post = new JSONObject();
		post.put("media_id", mediaId);
		String result = HttpClientUtils.httpPost(url,post.toString());
		return result;
	}
}
