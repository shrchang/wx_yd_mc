package com.booking.wechat.persistence.service.member;

import com.booking.wechat.persistence.bean.member.BalanceRecord;
import com.booking.wechat.persistence.service.base.DAO;

/**
 * 余额变动
 * @ClassName BalanceRecordDao
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月29日 上午10:13:02
 *
 */
public interface BalanceRecordDao extends DAO<BalanceRecord> {
	
	/**
	 * 根据订单号查询资金变动流水 这个不对 因为一个订单可能会存在多笔流水 预付款/尾款
	 * @author shrChang.Liu
	 * @param orderId
	 * @return
	 * @date 2018年10月29日 上午11:33:39
	 * @return BalanceRecord
	 * @description
	 */
	public BalanceRecord selectByOrderId(String orderId);
	
	/**
	 * 
	 * @author shrChang.Liu
	 * @param orderId
	 * @return
	 * @date 2018年11月11日 下午10:09:18
	 * @return BalanceRecord
	 * @description
	 */
	public BalanceRecord selectByOutOrderId(String orderId);

}
