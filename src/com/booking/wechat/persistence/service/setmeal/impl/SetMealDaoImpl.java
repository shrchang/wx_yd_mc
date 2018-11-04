package com.booking.wechat.persistence.service.setmeal.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.wechat.persistence.bean.setmeal.SetMeal;
import com.booking.wechat.persistence.service.base.DaoSupport;
import com.booking.wechat.persistence.service.setmeal.SetMealDao;

/**
 * 增值套餐
 * @ClassName SetMealDaoImpl
 * @author shrChang.Liu
 * @Description TODO
 * @date 2018年10月17日 下午3:31:01
 *
 */
@Service
public class SetMealDaoImpl extends DaoSupport<SetMeal> implements SetMealDao {

	public void updateBusName(Long busId, String busName) {
		batchUpdate("update "+getEntityName(SetMeal.class)+" set busName='"+busName+"' where busId="+busId);
	}

	public void updateShopName(Long shopId, String shopName) {
		batchUpdate("update "+getEntityName(SetMeal.class)+" set shopName='"+shopName+"' where shopId="+shopId);
	}

	public List<SetMeal> findByShopId(Long shopId) {
		SetMeal setMeal = new SetMeal();
		setMeal.setShopId(shopId);
		return findByExample(setMeal).getRows();
	}

}
