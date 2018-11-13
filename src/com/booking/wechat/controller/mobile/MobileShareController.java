package com.booking.wechat.controller.mobile;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.booking.wechat.Enum.ShareRequestStatusEnum;
import com.booking.wechat.client.model.UserBean;
import com.booking.wechat.controller.BaseController;
import com.booking.wechat.persistence.bean.share.ShareRequest;
import com.booking.wechat.persistence.bean.share.ShareResponse;
import com.booking.wechat.persistence.service.share.ShareRequestDao;
import com.booking.wechat.persistence.service.share.ShareResponseDao;

/**
 * 分享的控制器
 * @ClassName MobileShareController
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年11月13日 下午10:15:49
 *
 */
@Controller
@RequestMapping("/mobile/share")
public class MobileShareController extends BaseController {
	
	
	@Autowired
	private ShareRequestDao shareRequestDao;
	
	@Autowired
	private ShareResponseDao shareResponseDao;
	
	/**
	 * 修改分享请求 失败或者成功 成功次数加1 失败则不管 只修改状态
	 * @author shrChang.Liu
	 * @param id 分享请求的id
	 * @param success 是否分享成功 
	 * @param  source 来源是什么？  朋友圈还是个人
	 * @return
	 * @date 2018年11月13日 下午10:23:13
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/modify",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> modifyShareRequest(@RequestParam(value="id")Long id,
			@RequestParam("sucess")Boolean success,
			@RequestParam("source")String source){
		ShareRequest request = shareRequestDao.find(id);
		request.setSource(source);
		if(success){
			request.setCount(request.getCount()+1);
			request.setStatus(ShareRequestStatusEnum.SUCCESS.toString());
		}else{
			request.setStatus(ShareRequestStatusEnum.FAILED.toString());
		}
		shareRequestDao.doSave(request);
		return getSuccessResultMap(success ? "分享成功！" : "分享失败！");
	}
	
	/**
	 * 这是有阅读的情况存在了，这里面到时可能需要加入很多逻辑 比如第一次阅读有积分或者优惠卷 后面就没有了 这个逻辑到后面去处理
	 * @author shrChang.Liu
	 * @return
	 * @date 2018年11月13日 下午10:53:51
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/record",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addShareResponse(@RequestParam("requestId")Long requestId){
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean) session.getAttribute("WC_USER");
		if(user == null){
			return getFailResultMap("微信用户未获取！");
		}
		ShareResponse response = shareResponseDao.findResponseByShareIdAndOpenId(requestId, user.getOpenid());
		response.setUserName(user.getNickname());
		if(response.getId() == null){
			//可能后续还有因为这个东西去购买的逻辑 这个到时再说吧
			//TODO 这代表这是第一次浏览噢 积分什么的操作都可以直接在这里面进行操作
		}
		shareResponseDao.doSave(response);
		return getSuccessResultMap("获取分享记录成功！");
	}
}
