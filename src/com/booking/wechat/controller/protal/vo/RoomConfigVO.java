package com.booking.wechat.controller.protal.vo;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 场地的时间范围、价格，是否可订
 * @author xixi_
 *
 */
public class RoomConfigVO {
	
	private String configId;//配置标识
	
	private String rangeId;//时间范围标识，以防后面改动时间，订单匹配不上
	
	private String timeRange;
	
	private BigDecimal price;
	
	private String status;

	public String getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConfigId() {
		return UUID.randomUUID().toString();
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getRangeId() {
		return rangeId;
	}

	public void setRangeId(String rangeId) {
		this.rangeId = rangeId;
	}
	
}
