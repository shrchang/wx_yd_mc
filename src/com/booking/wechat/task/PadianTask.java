package com.booking.wechat.task;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.booking.wechat.persistence.bean.constant.Constant;
import com.booking.wechat.persistence.bean.usercard.CardChangeRecords;
import com.booking.wechat.persistence.bean.usercard.UserCard;
import com.booking.wechat.persistence.service.constant.ConstantDao;
import com.booking.wechat.persistence.service.usercard.CardChangeRecordsDao;
import com.booking.wechat.persistence.service.usercard.UserCardDao;
import com.booking.wechat.util.ConstantKey;
import com.booking.wechat.util.ListUtils;
import com.booking.wechat.util.NumberUtil;

/**
 * 每个月的1号0点0分0秒赠送趴点
 * @author Luoxx
 *
 */
public class PadianTask {

	private static Logger logger = Logger.getLogger(PadianTask.class);
	@Autowired
	private UserCardDao cardDao;
	
	@Autowired
	private ConstantDao constantDao;
	
	@Autowired
	private CardChangeRecordsDao recordDao;
	
	@Scheduled(cron="0 0 0 1 * ?")
	public void padian(){
		List<UserCard> cardList = cardDao.getAll();
		for (UserCard userCard : cardList) {
			Constant mb = constantDao.findByConstantId(userCard.getBusId(),ConstantKey.MONTHLY_BALANCE);
			if(null == mb){
				return;
			}
			if(userCard.getRemainingSum().longValue()>0){
				//检查是否有消费记录
				boolean flag = checkUserCard(userCard.getId(),userCard.getBusId());
				
				if(flag){
					BigDecimal mbValue = new BigDecimal(mb.getConstantValue());
					//获取趴点
					BigDecimal padian = getPadian(userCard.getCreateTime(), userCard.getRemainingSum(), mbValue);
					userCard.setRemainingSum(NumberUtil.add(userCard.getRemainingSum(), padian, 0));
					userCard.setTotalAmount(NumberUtil.add(userCard.getTotalAmount(), padian, 0));
					cardDao.update(userCard);
					logger.info("user card ["+userCard.getCardNumber()+"] monthly balance padian:"+padian.intValue());
					//会员卡变更记录
					CardChangeRecords record = new CardChangeRecords();
					record.setChangeDate(new Date());
					record.setChangeMoney(padian);
					record.setChangeType(CardChangeRecords.TYPE_MONTH_GRANT);
					record.setRemainingSum(userCard.getRemainingSum());
					record.setTotalAmount(userCard.getTotalAmount());
					record.setUserCardId(userCard.getId());
					recordDao.save(record);
				}
				
			}
		}
	}
	
	/**
	 * 判断会员卡最近是否有消费
	 * @param userCardId
	 * @return
	 */
	private boolean checkUserCard(Long userCardId,Long busId){
		Calendar paramCal = Calendar.getInstance();
		Date maxDate = paramCal.getTime();
		Constant minMonth = constantDao.findByConstantId(busId,ConstantKey.MIN_MONTH_CONSUMPTION);
		paramCal.set(Calendar.MONTH, Integer.valueOf(minMonth.getConstantValue()));
		Date minDate = paramCal.getTime();
		List<CardChangeRecords> records = recordDao.selectByUserCardId(userCardId, minDate, maxDate);
		if(ListUtils.isEmpty(records)){
			return false;
		}
		return true;
	}
	
	/**
	 * 获取赠送的趴点数<br>
	 * 如果是当月开的卡：趴点数=(余额*赠送比率)*(开卡日期/开卡月最大天数)<br>
	 * 否则：趴点数=(余额*赠送比率)
	 * @param createDate
	 * @param remaining
	 * @param mbValue
	 * @return
	 */
	private BigDecimal getPadian(Date createDate,BigDecimal remaining,BigDecimal mbValue){
		Calendar cal = Calendar.getInstance();
		//因为是在每月1日0点0分0秒，所以当前月减去1
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1);
		int currentMonth = cal.get(Calendar.MONTH);
		int currentYear = cal.get(Calendar.YEAR);
		cal.setTime(createDate);
		int cardMonth = cal.get(Calendar.MONTH);
		int cardYear = cal.get(Calendar.YEAR);
		BigDecimal padian = NumberUtil.multiply(remaining, mbValue, 0);
		if(currentMonth == cardMonth && cardYear == currentYear){
			int maxDay = cal.getActualMaximum(Calendar.DATE);
			int day = cal.get(Calendar.DATE);
			BigDecimal bl = NumberUtil.divide(new BigDecimal(maxDay-day), new BigDecimal(maxDay),2);
			padian = NumberUtil.multiply(padian, bl, 0);
			return padian;
		}else{
			return padian;
		}
	}
	
}
