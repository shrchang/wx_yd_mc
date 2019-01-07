package com.booking.wechat.persistence.service.share.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.share.ShareRequest;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.share.ShareRequestDao;

@Service
public class ShareRequestDaoImpl extends DaoSupport<ShareRequest> implements ShareRequestDao {

	@Override
	public ShareRequest findRequestByOpenIdAndResource(String openId, String type, Long resourceId) {
		String sql = "from "+getEntityName(ShareRequest.class) +" where openId=?1 and type=?2 and resourceId=?3";
		List<ShareRequest> requests = findByCustomJpql(sql,new Object[]{openId,type,resourceId});
		if(requests.size() > 0){
			return requests.get(0);
		}
		ShareRequest request = new ShareRequest();
		request.setOpenId(openId);
		request.setType(type);
		request.setResourceId(resourceId);
		return request;
	}

	@Override
	public void doSave(ShareRequest request) {
		request.setLastUpdateTime(new Date());
		if(request.getId() == null){
			request.setCreateTime(new Date());
			save(request);
		}else{
			update(request);
		}
	}

}
