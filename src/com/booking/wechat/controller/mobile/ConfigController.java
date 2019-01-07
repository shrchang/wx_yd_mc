package com.booking.wechat.controller.mobile;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;
import java.util.UUID;

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

import com.booking.wechat.Enum.ShareRequestStatusEnum;
import com.booking.wechat.Enum.ShareRequestTypeEnum;
import com.booking.wechat.client.model.UserBean;
import com.booking.wechat.client.user.UserClient;
import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.mobile.vo.JssdkConfig;
import com.booking.wechat.controller.mobile.vo.ShareRequestVO;
import com.booking.wechat.persistence.bean.member.MemberCard;
import com.booking.wechat.persistence.bean.share.ShareRequest;
import com.booking.wechat.persistence.bean.wechat.WechatConfig;
import com.booking.wechat.persistence.service.member.MemberCardDao;
import com.booking.wechat.persistence.service.share.ShareRequestDao;
import com.booking.wechat.persistence.service.wechat.WechatConfigDao;
import com.booking.wechat.ticket.JsapiTickect;
import com.booking.wechat.ticket.WeixinTicket;
import com.booking.wechat.token.AccessToken;
import com.booking.wechat.token.WeixinToken;

import net.sf.json.JSONObject;

/**
 * 移动端登录的 应该默认获取微信的权限去登录才行
 * 
 * @ClassName LoginController
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月18日 上午9:46:26
 *
 */
@Controller
@RequestMapping("/mobile/config")
public class ConfigController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(ConfigController.class);

	@Autowired
	private WechatConfigDao configDao;

	@Autowired
	private MemberCardDao memberDao;// 会员卡dao
	
	@Autowired
	private ShareRequestDao shareRequestDao;//分享请求dao

	/**
	 * 获取不同商户编号的微信配置
	 * 
	 * @author shrChang.Liu
	 * @param systemCode
	 * @return
	 * @date 2018年10月18日 上午9:53:07
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getWechatConfigByCode(@RequestParam(value = "systemcode") String systemCode) {
		WechatConfig config = configDao.selectBySystemCode(systemCode);
		return getSuccessResultMap("微信配置获取成功！", config);
	}

	/**
	 * 获取微信用户的数据
	 * 
	 * @author shrChang.Liu
	 * @param systemCode
	 * @param code
	 * @return
	 * @date 2018年10月18日 上午10:19:42
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value = "/getWxUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getWxUser(@RequestParam(value = "systemcode") String systemCode,
			@RequestParam(value = "code") String code) {
		Session session = SecurityUtils.getSubject().getSession();
		UserBean user = (UserBean) session.getAttribute("WC_USER");
		WechatConfig config = configDao.selectBySystemCode(systemCode);
		if (user == null && StringUtils.isNotBlank(code)) {
			UserClient client = new UserClient(config.getBusId());
			user = client.getUserInfoByAuthorize(code);
			if (user != null) {
				logger.info("微信返回的数据：" + JSONObject.fromObject(user).toString());
			}
			session.setAttribute("WC_USER", user);
			// 如果这个地方没有找到用户就需要将用户信息保存起来
			MemberCard card = memberDao.findByOpenId(user.getOpenid());
			if (card == null) {
				card = new MemberCard();
				card.setBalance(new BigDecimal("0.00"));
				card.setBusId(config.getBusId());
				card.setBusName(config.getBusName());
				card.setCardNo("");// 生成会员卡号 先默认长为空 个人建议使用手机号码作为会员号
				card.setCity(user.getCity());
				card.setCountry(user.getCountry());
				card.setDuePoint(new BigDecimal("0.00"));
				card.setHeadImgurl(user.getHeadimgurl());
				card.setLevel(0);// 默认0是游客
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
		} else {
			return getFailResultMap("获取微信用户信息失败！");
		}
	}

	/**
	 * 获取微信jssdk的配置参数
	 * 
	 * @author shrChang.Liu
	 * @param systemCode
	 * @param url 不能包含#后面的内容
	 * @param type {@link ShareRequestTypeEnum} 可以为空的
	 * @param id 根据类型来对应的id 可以为空
	 * @return
	 * @date 2018年11月11日 下午9:13:39
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(value = "/getJsSdkConfig", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryJssdkConfig(@RequestParam(value = "systemcode") String systemCode,
			@RequestParam(value = "url") String url,
			@RequestParam(value="name")String name,
			@RequestParam(value="openId")String openId,
			@RequestParam(value="parentId",required=false,defaultValue="0")Long parentId,
			@RequestParam(value="type",required=false)String type,
			@RequestParam(value="resourceId",required=false,defaultValue="0")Long resourceId) {
		WechatConfig config = configDao.selectBySystemCode(systemCode);
		if (config != null) {
			AccessToken token = WeixinToken.getAccessToken(config);
			JsapiTickect tickect = WeixinTicket.getJsApiTicket(token);
			ShareRequestVO requestVO = new ShareRequestVO();
			JssdkConfig jssdkConfig = new JssdkConfig();
			jssdkConfig.setAppId(config.getAppId());
			jssdkConfig.setJsapi_ticket(tickect.getTicket());
			jssdkConfig.setNonceStr(create_nonce_str());
			jssdkConfig.setTimestamp(create_timestamp());
			jssdkConfig.setUrl(url);
			jssdkConfig.setSignature(sign(jssdkConfig));
			//这个地方开始添加一个插入一条新的记录 就是关于当前用户的
//			Session session = SecurityUtils.getSubject().getSession();
//			UserBean user = (UserBean) session.getAttribute("WC_USER");
			if(openId != null){
				if(StringUtils.isBlank(type)){
					type = ShareRequestTypeEnum.NORMAL.toString();
				}
				MemberCard card = memberDao.findByOpenId(openId);
				ShareRequest request = shareRequestDao.findRequestByOpenIdAndResource(openId, type, resourceId);
				request.setName(name);
				request.setParentId(parentId);
				request.setUrl(url);
				request.setStatus(ShareRequestStatusEnum.INIT.toString());
				request.setUserName(card.getNickName());
				shareRequestDao.doSave(request);
				requestVO.setShareId(request.getId());
			}
			requestVO.setConfig(jssdkConfig);
			return getSuccessResultMap("获取Jssdk配置成功！", requestVO);
		}
		return getFailResultMap("获取JsSdk配置失败！");
	}

	/**
	 * 获取签名
	 * 
	 * @author shrChang.Liu
	 * @param jssdkConfig
	 * @return
	 * @date 2018年11月11日 下午9:17:31
	 * @return String
	 * @description
	 */
	private static String sign(JssdkConfig jssdkConfig) {
		String signature = null;
		// 注意这里参数名必须全部小写，且必须有序
		String str = "jsapi_ticket=" + jssdkConfig.getJsapi_ticket() + "&noncestr=" + jssdkConfig.getNonceStr()
				+ "&timestamp=" + jssdkConfig.getTimestamp() + "&url=" + jssdkConfig.getUrl();
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(str.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return signature;
	}
	
	/**
	 * 转义
	 * @author shrChang.Liu
	 * @param hash
	 * @return
	 * @date 2018年11月11日 下午9:20:06
	 * @return String
	 * @description
	 */
	private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

}
