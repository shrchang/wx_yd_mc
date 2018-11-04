package com.booking.wechat.persistence.service.usercard.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.usercard.CardChangeRecords;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.usercard.CardChangeRecordsDao;

@Service
public class CardChangeRecordsDaoImpl extends DaoSupport<CardChangeRecords> implements CardChangeRecordsDao{

	public List<CardChangeRecords> selectByUserCardId(Long userCardId) {
		LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
	    orderby.put("changeDate", "desc");
	    orderby.put("changeType", "desc");
	    CardChangeRecords params = new CardChangeRecords();
	    params.setUserCardId(userCardId);
        List<CardChangeRecords> list =findByExample(params,true,-1,-1,orderby).getRows();
        return list;  
	}

	
	public List<CardChangeRecords> selectByUserCardId(Long userCardId, Date minDate, Date maxDate) {
		String jpql = "from "+getEntityName(CardChangeRecords.class)+" o where o.userCardId=?1 and o.changeDate>=?2 and o.changeDate<=?3 and o.changeType=?4";
		List<CardChangeRecords> list = findByCustomJpql(jpql,new Object[]{userCardId,minDate,maxDate,"消费"});
		return list;
	}

	
	public void deleteByUserCardId(Long userCardId) {
		batchUpdate("delete from "+this.getEntityName(CardChangeRecords.class) +" where userCardId="+userCardId);
	}

}
