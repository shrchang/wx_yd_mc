package com.booking.wechat.ticket;

import java.io.Serializable;

/**
 * js api的票据
 * @ClassName JsapiTickect
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年11月11日 上午10:59:55
 *
 */
public class JsapiTickect implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6837506998126762176L;

	private String errcode;
	
	private String errmsg;
	
	private String ticket;
	
	private Integer expires_in;
	
	private Long end_time;

	public String getErrcode() {
		return errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public String getTicket() {
		return ticket;
	}

	public Integer getExpires_in() {
		return expires_in;
	}

	public Long getEnd_time() {
		return end_time;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}

	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}
	
	
}
