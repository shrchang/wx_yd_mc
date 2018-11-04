package com.booking.wechat.token;


public class AccessToken {

	private String access_token = null;
	private Integer expires_in = null;
	private Long end_time = null;
	
	public AccessToken() {
		super();
	}

	public AccessToken(String access_token, Integer expires_in) {
		this.access_token = access_token;
		this.expires_in = expires_in;
	}
	
	public String toString() {
		return "AccessToken [access_token=" + access_token + ", expires_in="
				+ expires_in + "]";
	}

	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public Integer getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}

	public Long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Long endTime) {
		end_time = endTime;
	}
}
