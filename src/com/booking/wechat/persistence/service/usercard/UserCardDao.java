package com.booking.wechat.persistence.service.usercard;

import java.util.List;

import com.booking.wechat.persistence.bean.usercard.UserCard;
import com.booking.wechat.persistence.service.base.DAO;

public interface UserCardDao extends DAO<UserCard>{

	public List<UserCard> selectByUserId(String userId);
	
	public UserCard selectByCardNumber(String cardNumber);
	
	public void updateBusName(Long busId,String busName);
}
