package com.booking.wechat.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.booking.wechat.message.resp.Article;
import com.booking.wechat.message.resp.NewsMessage;
import com.booking.wechat.util.MessageUtil;

/**
 * 定制图文消息处理
 * @author Luoxx
 *
 */
public class ProcessCustomerMessage {

	/**
	 * 封装图文消息，并返回对应的xml格式
	 * @param map 微信请求的参数
	 * @return
	 */
	public String customeMessage(Map<String, String> map){
		NewsMessage news = new NewsMessage();
		news.setFromUserName(map.get("ToUserName"));
		news.setToUserName(map.get("FromUserName"));
		news.setCreateTime(new Date().getTime());
		news.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		List<Article> items = new ArrayList<Article>();
		Article item = new Article();
		item.setTitle("图文消息标题");
		item.setDescription("图文消息描述图文消息描述图文消息描述图文消息描述图文消息描述图文消息描述图文消息描述图文消息描述图文消息描述图文消息描述");
		item.setUrl("http://mp.weixin.qq.com/s?__biz=MzAwNTE3ODE3OA==&mid=208556058&idx=1&sn=2fdd8ee163e36630163d591e19344de4#rd");
		item.setPicUrl("http://car0.autoimg.cn/car/upload/2015/2/18/u_2015021808472681326411.jpg");
		
		items.add(item);
		news.setArticles(items);
		news.setArticleCount(items.size());
		return MessageUtil.newsMessageToXml(news);
	}
}
