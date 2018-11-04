package com.booking.wechat.persistence.service.setmeal;

import java.util.List;

import com.booking.wechat.persistence.bean.setmeal.SetMeal;
import com.booking.wechat.persistence.service.base.DAO;

public interface SetMealDao extends DAO<SetMeal>{
	
	public void updateBusName(Long busId,String busName) ;
	
	public void updateShopName(Long shopId,String shopName) ;
	
	/**
	 * 根据商铺id查询增值套餐
	 * @author shrChang.Liu
	 * @param shopId
	 * @return
	 * @date 2018年10月30日 上午11:03:34
	 * @return List<SetMeal>
	 * @description
	 */
	public List<SetMeal> findByShopId(Long shopId);
}
