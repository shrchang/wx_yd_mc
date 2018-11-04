package com.booking.wechat.persistence.service.faq;

import java.util.List;

import com.booking.wechat.persistence.bean.faq.FaqList;
import com.booking.wechat.persistence.service.base.DAO;

public interface FaqListDao extends DAO<FaqList>{

	public void updateBusName(Long busId,String busName);
	
	public List<FaqList> getFaqListParentByUser();
	
	/**
	 * 根据商户ID查询
	 * @param busId 商户ID
	 * @param parent 是否只查询父节点 true：只查询为parentId为0的值；false:查询所有
	 * @return
	 */
	public List<FaqList> findByBusiness(Long busId,boolean parent);
}
