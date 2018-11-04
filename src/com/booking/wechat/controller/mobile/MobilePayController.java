package com.booking.wechat.controller.mobile;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.booking.wechat.constant.Constant;
import com.booking.wechat.controller.BaseController;

/**
 * 支付控制器
 * @ClassName MobilePayController
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月31日 下午2:49:53
 *
 */
@Controller
@RequestMapping("/mobile/pay")
public class MobilePayController extends BaseController {

	/**
	 * 支付
	 * @author shrChang.Liu
	 * @param orderId
	 * @param payType {@link Constant}
	 * @return
	 * @date 2018年10月31日 下午2:52:20
	 * @return Map<String,Object>
	 * @description
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> orderPay(@RequestParam(value="orderId")Long orderId,
			@RequestParam(value="payType")String payType){
		return null;
	}
}
