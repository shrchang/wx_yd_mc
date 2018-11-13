package com.booking.wechat.persistence.service.share;

import com.booking.wechat.persistence.bean.share.ShareResponse;
import com.booking.wechat.persistence.service.base.DAO;

public interface ShareResponseDao extends DAO<ShareResponse> {

	/**
	 * 根据分享者的id与用户查询已经浏览记录
	 * @author shrChang.Liu
	 * @param shareId
	 * @param openId
	 * @return
	 * @date 2018年11月13日 下午10:59:33
	 * @return ShareResponse
	 * @description
	 */
	public ShareResponse findResponseByShareIdAndOpenId(Long shareId,String openId);
	
	/**
	 * 保存分享浏览记录 新增和修改
	 * @author shrChang.Liu
	 * @param shareResponse
	 * @date 2018年11月13日 下午11:03:29
	 * @return void
	 * @description
	 */
	public void doSave(ShareResponse shareResponse);
}
