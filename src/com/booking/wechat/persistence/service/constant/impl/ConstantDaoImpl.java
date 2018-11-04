package com.booking.wechat.persistence.service.constant.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.util.ListUtils;

@Service
public class ConstantDaoImpl extends DaoSupport<Constant> implements ConstantDao{

	
	public Constant findByConstantId(Long busId,String constantId) {
		Constant params = new Constant();
		params.setConstantId(constantId);
		params.setBusId(busId);
		List<Constant> list = findByExample(params,true).getRows();
		if(ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	
	public void updateBusName(Long busId, String busName) {
		batchUpdate("update "+getEntityName(Constant.class)+" set busName='"+busName+"' where busId="+busId);
	}

}
