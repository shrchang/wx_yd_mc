package com.booking.wechat.constant;

/**
 * 常量类
 * @ClassName Constant
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月29日 上午11:26:14
 *
 */
public class Constant {
	
	//支付状态
	public static final String PAY_RESULT_SUCCESS = "success";//支付成功
	public static final String PAY_RESULT_DOING = "doing";//正在支付
	public static final String PAY_RESULT_FAILED = "failed";//支付失败
	
	//支付类型
	public static final String PAY_TYPE_WX="weixin";//微信支付
	public static final String PAY_TYPE_ALIPAY = "alipay";//阿里支付
	public static final String PAY_TYPE_BALANCE = "balance";//余额支付
	
	//变动类型
	public static final String CHANGE_TYPE_RECHARGE="recharge";//充值
	public static final String CHANGE_TYPE_PAYONLINE="payOnline";//在线支付
	public static final String CHANGE_TYPE_WX = "weixin";//微信支付
	public static final String CHANGE_TYPE_BALANCE="balance";//余额支付
	public static final String CHANGE_TYPE_PAYOTHER="payOther";//代付 这个地方应该要有一个代付的人

}
