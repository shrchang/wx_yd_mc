package com.booking.wechat.persistence.service.bus;

import java.util.List;

import com.booking.wechat.persistence.bean.bus.Business;
import com.booking.wechat.persistence.service.base.DAO;

public interface BusinessDao extends DAO<Business>{

	/**
	 * 统计商户场地数
	 * @return id,name,city,count
	 */
	public List<Object[]> getBusinessAndRoom();
	
	/**
	 * 根据用户角色查询商户信息
	 * @return
	 */
	public List<Business> getBusinessByUser();
}
