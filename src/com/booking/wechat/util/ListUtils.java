package com.booking.wechat.util;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @功能: 对List获取Array数组的操作
 * @author: 王宇阳
 * @version：2012-5-7 下午05:20:57
 */
public class ListUtils {

    /**
     * 判断list集合是否为null
     * 
     * @param list
     * @return
     */
    public static final boolean isEmpty(List<?> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断list集合是否不为null
     * 
     * @param list
     * @return
     */
    public static final boolean isNotEmpty(List<?> list) {
        if (list == null || list.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断数据是否为null
     * 
     * @param list
     * @return
     */
    public static final boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断数据是否不为null
     * 
     * @param array
     * @return
     */
    public static final boolean isNotEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return false;
        }
        return true;
    }

    /**
     * 把String的集合已某种字符隔开连接并返回
     * 
     * @param list
     * @param jointChar
     * @return
     */
    public static final String convertToQuerySql(List<String> list, String jointChar) {
        StringBuffer listStr = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            listStr.append("'");
            listStr.append(list.get(i));
            listStr.append("'");
            if (i != list.size() - 1) {
                listStr.append(jointChar);
            }
        }
        return listStr.toString();
    }

    /**
     * 装换成String
     * 
     * @param list
     * @param jointChar
     * @return
     */
    public static final String convertToString(List<? extends Object> list, String jointChar) {
        if (isEmpty(list)) {
            return "";
        }
        StringBuffer listStr = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            listStr.append(list.get(i));
            if (i != list.size() - 1) {
                listStr.append(jointChar);
            }
        }
        return listStr.toString();
    }
    
    /**
     * 装换成String
     * 
     * @param list
     * @param jointChar
     * @return
     */
    public static final String convertToString(String[] array, String jointChar) {
        if (isEmpty(array)) {
            return "";
        }
        StringBuffer listStr = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            listStr.append(array[i]);
            if (i != array.length - 1) {
                listStr.append(jointChar);
            }
        }
        return listStr.toString();
    }


    /**
     * 装换成String
     * 
     * @param list
     * @param jointChar
     * @return
     */
    public static final String convertToString(Set<? extends Object> list, String jointChar) {
        if (list == null || list.size() == 0) {
            return "";
        }
        int index = 0;
        StringBuffer listStr = new StringBuffer();
        Iterator<? extends Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            listStr.append((String)iterator.next());
            if (index != list.size() - 1) {
                listStr.append(jointChar);
            }
            index++;
        }
        return listStr.toString();
    }

    /**
     * 查询set里面对应的对象信息
     * 
     * @param obj
     * @param objs
     * @return
     */
    public static final Object getForSet(Object obj, Set<? extends Object> objs) {
        Object returnObj = null;
        Iterator<? extends Object> iter = objs.iterator();
        while (iter.hasNext()) {
            Object iterObj = iter.next();
            if (obj.equals(iterObj)) {
                returnObj = iterObj;
                break;
            }
        }
        return returnObj;
    }

}
