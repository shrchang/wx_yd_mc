package com.booking.wechat.persistence.service.share;

import com.booking.wechat.persistence.bean.share.ShareRequest;
import com.booking.wechat.persistence.service.base.DAO;

public interface ShareRequestDao extends DAO<ShareRequest> {

	/**
	 * 根据微信用户、分享类型与id查询分享记录 如果没有的话 默认返回一个NULL
	 * @author shrChang.Liu
	 * @param openId
	 * @param type
	 * @param resourceId
	 * @return
	 * @date 2018年11月13日 下午10:35:17
	 * @return ShareRequest
	 * @description
	 */
	public ShareRequest findRequestByOpenIdAndResource(String openId,String type,Long resourceId);
	
	/**
	 * 保存分享请求体 包含了新增和修改
	 * @author shrChang.Liu
	 * @param request
	 * @date 2018年11月13日 下午10:39:11
	 * @return void
	 * @description
	 */
	public void doSave(ShareRequest request);
}
