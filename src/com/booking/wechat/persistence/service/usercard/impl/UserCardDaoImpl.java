package com.booking.wechat.persistence.service.usercard.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.usercard.UserCard;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.usercard.UserCardDao;
import com.booking.wechat.util.ListUtils;

@Service
public class UserCardDaoImpl extends DaoSupport<UserCard> implements UserCardDao {

	public List<UserCard> selectByUserId(String userId) {
        String sql = "from "+getEntityName(UserCard.class)+" where userId=?1";
        List<UserCard> cards = findByCustomJpql(sql,new Object[]{userId});
		return cards;
	}

	public UserCard selectByCardNumber(String cardNumber) {
		String sql = "from "+getEntityName(UserCard.class)+" where cardNumber=?1";
        List<UserCard> cards = findByCustomJpql(sql,new Object[]{cardNumber});
        if(ListUtils.isNotEmpty(cards)){
        	return cards.get(0);
        }
		return null;
	}

	
	public void updateBusName(Long busId,String busName) {
		batchUpdate("update "+getEntityName(UserCard.class)+" set busName='"+busName+"' where busId="+busId);
	}

}
