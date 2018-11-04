package com.booking.wechat.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {
	
	@Autowired
	private HttpServletRequest request;
	
	private static String SUCESS_CODE = "200";
	private static String FAILE_CODE = "500";
	

	protected ModelAndView ajaxDone(int statusCode, String message, String forwardUrl) {
		ModelAndView mav = new ModelAndView("/system/ajaxDone");
		mav.addObject("statusCode", statusCode);
		mav.addObject("message", message);
		mav.addObject("forwardUrl", forwardUrl);
		Map<String,String> param = new HashMap<String,String>();
		param.put("callbackType", request.getParameter("callbackType"));
		param.put("navTabId", request.getParameter("navTabId"));
		param.put("rel", request.getParameter("rel"));
		param.put("forwardUrl", request.getParameter("forwardUrl"));
		mav.addObject("param", param);
		return mav;
	}
	
	protected ModelAndView ajaxDoneSuccess(String message) {
		return ajaxDone(200, message, "");
	}
	
	protected ModelAndView ajaxDoneTimeout(String message) {
		return ajaxDone(301, message, "");
	}

	protected ModelAndView ajaxDoneError(String message) {
		return ajaxDone(300, message, "");
	}
	protected String getMessage(String code) {
		return this.getMessage(code, new Object[] {});
	}

	protected String getMessage(String code, Object arg0) {
		return getMessage(code, new Object[] { arg0 });
	}

	protected String getMessage(String code, Object arg0, Object arg1) {
		return getMessage(code, new Object[] { arg0, arg1 });
	}

	protected String getMessage(String code, Object arg0, Object arg1,
			Object arg2) {
		return getMessage(code, new Object[] { arg0, arg1, arg2 });
	}

	protected String getMessage(String code, Object arg0, Object arg1,
			Object arg2, Object arg3) {
		return getMessage(code, new Object[] { arg0, arg1, arg2, arg3 });
	}
	
	/**
	 * 获取一个执行完成的Map对象
	 * @author shrChang.Liu
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 * @date 2018年10月17日 下午2:45:59
	 * @return Map<String,String>
	 * @description
	 */
	protected Map<String,Object> getResultMap(String code,String msg,Object data){
		Map<String,Object> res = new HashMap<String, Object>();
		res.put("code", code);
		res.put("msg", msg);
		if(data != null){
			res.put("data", data);
		}
		return res;
	}
	
	/**
	 * 失败的返回
	 * @author shrChang.Liu
	 * @param msg
	 * @return
	 * @date 2018年10月18日 上午10:21:55
	 * @return Map<String,Object>
	 * @description
	 */
	protected Map<String,Object> getFailResultMap(String msg){
		return getResultMap(FAILE_CODE, msg, null);
	}
	
	/**
	 * 获取数据
	 * @author shrChang.Liu
	 * @param msg
	 * @param data
	 * @return
	 * @date 2018年10月18日 上午9:54:51
	 * @return Map<String,Object>
	 * @description
	 */
	protected Map<String,Object> getSuccessResultMap(String msg,Object data){
		return getResultMap(SUCESS_CODE,msg,data);
	}
	
	protected Map<String,Object> getSuccessResultMap(String msg){
		return getResultMap(SUCESS_CODE,msg,null);
	}

}
