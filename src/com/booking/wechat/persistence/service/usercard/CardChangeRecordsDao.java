package com.booking.wechat.persistence.service.usercard;

import java.util.Date;
import java.util.List;

import com.booking.wechat.persistence.bean.usercard.CardChangeRecords;
import com.booking.wechat.persistence.service.base.DAO;

public interface CardChangeRecordsDao extends DAO<CardChangeRecords> {
	
	public List<CardChangeRecords> selectByUserCardId(Long userCardId);

	public List<CardChangeRecords> selectByUserCardId(Long userCardId,Date minDate,Date maxDate);
	
	public void deleteByUserCardId(Long userCardId);
}
