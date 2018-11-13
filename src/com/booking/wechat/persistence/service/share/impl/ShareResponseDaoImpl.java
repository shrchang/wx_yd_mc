package com.booking.wechat.persistence.service.share.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.share.ShareResponse;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.share.ShareResponseDao;

@Service
public class ShareResponseDaoImpl extends DaoSupport<ShareResponse> implements ShareResponseDao {

	@Override
	public ShareResponse findResponseByShareIdAndOpenId(Long requestId, String openId) {
		ShareResponse response = new ShareResponse();
		response.setRequestId(requestId);
		response.setOpenId(openId);
		List<ShareResponse> shareResponses = findByExample(response).getRows();
		if(!shareResponses.isEmpty()){
			return shareResponses.get(0);
		}
		return response;
	}

	@Override
	public void doSave(ShareResponse shareResponse) {
		shareResponse.setLastUpdateTime(new Date());
		if(shareResponse.getId() == null){
			shareResponse.setCreateTime(new Date());
			save(shareResponse);
		}else{
			shareResponse.setCount(shareResponse.getCount() + 1);
			update(shareResponse);
		}
		
	}

}