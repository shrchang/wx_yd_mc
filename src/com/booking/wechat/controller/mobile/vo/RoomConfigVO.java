package com.booking.wechat.controller.mobile.vo;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 场地预定时间范围 价格 是否可定
 * @ClassName RoomConfigVO
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月30日 上午10:43:59
 *
 */
public class RoomConfigVO {
	
	private String configId;//配置标识
	
	private String rangeId;//时间范围标识，以防后面改动时间，订单匹配不上
	
	private String timeRange;//时间范围配置
	
	private BigDecimal price;//价格
	
	private String status;//状态
	
	private Double bookingPriceRate;//预定的比率
	
	public Double getBookingPriceRate() {
		return bookingPriceRate;
	}

	public void setBookingPriceRate(Double bookingPriceRate) {
		this.bookingPriceRate = bookingPriceRate;
	}

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
		if(this.configId == null)
			this.configId = UUID.randomUUID().toString();
		return this.configId;
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
