package com.booking.wechat.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;


/**
 * 移动端的拦截器 可能这里获取一些usertoken信息
 * @ClassName MobilHandlerInterceptor
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月18日 上午9:41:24
 *
 */
public class MobilHandlerInterceptor implements HandlerInterceptor {
	
	private Logger logger = LoggerFactory.getLogger(MobilHandlerInterceptor.class);
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Map map = request.getParameterMap();
		logger.info("请求参数：" + JSONObject.fromObject(map).toString());
		logger.info("请求路径：" +  request.getRequestURI());
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
