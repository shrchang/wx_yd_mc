package com.booking.wechat.persistence.service.member.impl;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.member.MemberCard;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.member.MemberCardDao;

/**
 * 会员卡服务
 * @ClassName MemberCardDaoImpl
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月29日 上午10:15:49
 *
 */
@Service
public class MemberCardDaoImpl extends DaoSupport<MemberCard> implements MemberCardDao{

	public MemberCard findByOpenId(String openId) {
		MemberCard card = new MemberCard();
		card.setOpenId(openId);
		QueryResult<MemberCard> cards = findByExample(card);
		if(cards.getTotal()>0){
			return cards.getRows().get(0);
		}
		return null;
	}

	public MemberCard findByPhoneNo(String phoneNo) {
		MemberCard card = new MemberCard();
		card.setPhoneNo(phoneNo);
		QueryResult<MemberCard> cards = findByExample(card);
		if(cards.getTotal()>0){
			return cards.getRows().get(0);
		}
		return null;
	}
}
