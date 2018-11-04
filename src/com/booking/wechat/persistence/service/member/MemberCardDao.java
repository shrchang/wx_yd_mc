package com.booking.wechat.persistence.service.member;

import com.booking.wechat.persistence.bean.member.MemberCard;
import com.booking.wechat.persistence.service.base.DAO;

/**
 * 会员卡
 * @ClassName MemberCardDao
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月29日 上午10:13:47
 *
 */
public interface MemberCardDao extends DAO<MemberCard> {
	
	/**
	 * 根据微信的openId查询会员
	 * @author shrChang.Liu
	 * @param openId
	 * @return
	 * @date 2018年10月29日 上午10:18:22
	 * @return MemberCard
	 * @description
	 */
	public MemberCard findByOpenId(String openId);
	
	/**
	 * 根据手机号码查询会员信息 手机号码等于会员卡
	 * @author shrChang.Liu
	 * @param phoneNo
	 * @return
	 * @date 2018年10月29日 上午10:47:30
	 * @return MemberCard
	 * @description
	 */
	public MemberCard findByPhoneNo(String phoneNo);

}
