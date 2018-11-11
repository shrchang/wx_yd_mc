package com.booking.wechat.persistence.service.member.impl;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.member.BalanceRecord;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.member.BalanceRecordDao;

/**
 * 余额变动
 * @ClassName BalanceRecordDaoImpl
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月29日 上午10:14:58
 *
 */
@Service
public class BalanceRecordDaoImpl extends DaoSupport<BalanceRecord> implements BalanceRecordDao{

	public BalanceRecord selectByOrderId(String orderId) {
		BalanceRecord record = new BalanceRecord();
		record.setOrderId(orderId);
		QueryResult<BalanceRecord> queryResult = findByExample(record);
		if(queryResult.getTotal()>0){
			return queryResult.getRows().get(0);
		}
		return null;
	}

	@Override
	public BalanceRecord selectByOutOrderId(String orderId) {
		BalanceRecord record = new BalanceRecord();
		record.setOutOrderId(orderId);
		QueryResult<BalanceRecord> queryResult = findByExample(record);
		if(queryResult.getTotal()>0){
			return queryResult.getRows().get(0);
		}
		return null;
	}

}
