package com.booking.wechat.controller.mobile;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.booking.wechat.client.model.UserBean;
import com.booking.wechat.client.user.UserClient;
import com.booking.wechat.controller.BaseController;
import com.booking.wechat.persistence.bean.member.MemberCard;
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.member.MemberCardDao;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;

import net.sf.json.JSONObject;

/**
 * 移动端登录的 应该默认获取微信的权限去登录才行
 * @ClassName LoginController
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月18日 上午9:46:26
 *
 */
@Controller
@RequestMapping("/mobile/config")
public class ConfigController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(ConfigController.class);
	
	@Autowired
	private WechatConfigDao configDao;
	
	@Autowired
	private MemberCardDao memberDao;//会员卡dao
	
	
	/**
	 * 获取不同商户编号的微信配置
	 * @author shrChang.Liu
	 * @param systemCode
	 * @return
	 * @date 2018年10月18日 上午9:53:07
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getWechatConfigByCode(@RequestParam(value="systemcode") String systemCode){
		WechatConfig config = configDao.selectBySystemCode(systemCode);
		return getSuccessResultMap("微信配置获取成功！",config);
	}
	
	/**
	 * 获取微信用户的数据
	 * @author shrChang.Liu
	 * @param systemCode
	 * @param code
	 * @return
	 * @date 2018年10月18日 上午10:19:42
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value="/getWxUser",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getWxUser(@RequestParam(value="systemcode")String systemCode,@RequestParam(value="code")String code){
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean) session.getAttribute("WC_USER");
		WechatConfig config = configDao.selectBySystemCode(systemCode);
		if(user == null && StringUtils.isNotBlank(code)){
			UserClient client = new UserClient(config.getBusId());
			user = client.getUserInfoByAuthorize(code);
			if(user  != null){
				logger.info("微信返回的数据：" + JSONObject.fromObject(user).toString());
			}
			session.setAttribute("WC_USER", user);
			//如果这个地方没有找到用户就需要将用户信息保存起来
			MemberCard card = memberDao.findByOpenId(user.getOpenid());
			if(card == null){
				card = new MemberCard();
				card.setBalance(new BigDecimal("0.00"));
				card.setBusId(config.getBusId());
				card.setBusName(config.getBusName());
				card.setCardNo("");//生成会员卡号  先默认长为空 个人建议使用手机号码作为会员号
				card.setCity(user.getCity());
				card.setCountry(user.getCountry());
				card.setDuePoint(new BigDecimal("0.00"));
				card.setHeadImgurl(user.getHeadimgurl());
				card.setLevel(0);//默认0是游客
				card.setNickName(user.getNickname());
				card.setUserName(user.getNickname());
				card.setOpenId(user.getOpenid());
				card.setPhoneNo("");
				card.setPoint(new BigDecimal("0.00"));
				card.setProvince(user.getProvince());
				card.setSex(user.getSex());
				card.setStatus(MemberCard.OK);
				memberDao.save(card);
			}
			return getSuccessResultMap("获取微信用户信息成功！", user);
		}else{
			return getFailResultMap("获取微信用户信息失败！");
		}
	}

}
