package com.booking.wechat.client.pay;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.booking.wechat.util.MD5Util;

/**
 * 生成支付签名
 * 
 * @author Luoxx
 * 
 */
public class TenPaySign {

	private static Logger logger = Logger.getLogger(TenPaySign.class);

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * @param bean 要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 * @throws Exception
	 */
	public static Map<String, String> convertBean(Object bean) throws Exception {
		Class<? extends Object> type = bean.getClass();
		SortedMap<String, String> returnMap = new TreeMap<String, String>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result.toString());
				}
			}
		}
		return returnMap;
	}

	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public static String createSign(String payKey,Map<String, String> packageParams) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = packageParams.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + payKey);
		logger.info("md5加密数据:" + sb);
		String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
		logger.info("支付签名:" + sign);
		return sign;

	}

}
