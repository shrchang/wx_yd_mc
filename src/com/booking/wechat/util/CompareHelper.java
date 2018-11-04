package com.booking.wechat.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

/**
 * 通用对象集合的排序工具类
 * @author Luoxx
 * 
 * */
@SuppressWarnings("unchecked")
public class CompareHelper {
	
	/**
	 * 通用对象集合的单属性排序
	 * @param entityList 对象集合
	 * @param fieldName 对象排序属性
	 * @param order 排序顺序：order=0升序，order=1降序
	 * */
	public static <T> List<T> singleSort(List<T> entityList, String fieldName, int order) {
		Comparator comparator = new BeanComparator(fieldName);
		if (order == 1) {
			comparator = new ReverseComparator(comparator);
		}
		Collections.sort(entityList, comparator);
		return entityList;
	}
	
	
}