package com.booking.wechat.util;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 数字工具类
 * 
 * @author Luoxx
 */
public final class NumberUtil {

    /**
     * 判断一个数字对象是否空或0
     * 
     * @param number
     * @return 空或0时返回true,否则返回false
     * @author Luozhh,Time 2013/02/26
     */
    public static boolean isNull(Number number) {
        return (null == number || number.longValue() == 0);
    }

    /*
     * @描述 根据给定的最小数字和最大数字，以及随机数的个数，产生指定的不重复的数组
     * @param begin 最小数字（包含该数）
     * @param end 最大数字（不包含该数）
     * @param size 指定产生随机数的个数
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:07:09
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:07:09
     */
    public static int[] generateRandomNumber(int begin, int end, int size) {
        // 加入逻辑判断，确保begin<end并且size不能大于该表示范围
        if (begin >= end || (end - begin) < size) {
            return null;
        }
        // 种子你可以随意生成，但不能重复
        int[] seed = new int[end - begin];

        for (int i = begin; i < end; i++) {
            seed[i - begin] = i;
        }
        int[] ranArr = new int[size];
        Random ran = new Random();
        // 数量你可以自己定义。
        for (int i = 0; i < size; i++) {
            // 得到一个位置
            int j = ran.nextInt(seed.length - i);
            // 得到那个位置的数值
            ranArr[i] = seed[j];
            // 将最后一个未用的数字放到这里
            seed[j] = seed[seed.length - 1 - i];
        }
        return ranArr;
    }

    /*
     * @描述 根据给定的最小数字和最大数字，以及随机数的个数，产生指定的不重复的数组
     * @param begin 最小数字（包含该数）
     * @param end 最大数字（不包含该数）
     * @param size 指定产生随机数的个数
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:07:28
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:07:28
     */
    public static Integer[] generateBySet(int begin, int end, int size) {
        // 加入逻辑判断，确保begin<end并且size不能大于该表示范围
        if (begin >= end || (end - begin) < size) {
            return null;
        }

        Random ran = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < size) {
            set.add(begin + ran.nextInt(end - begin));
        }

        Integer[] ranArr = new Integer[size];
        ranArr = set.toArray(new Integer[size]);
        // ranArr = (Integer[]) set.toArray();

        return ranArr;
    }

    /*
     * @描述 判断String是否是整数
     * @param s
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:07:46
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:07:46
     */
    public static boolean isInteger(String str) {
        int begin = 0;
        if (str == null || str.trim().equals("")) {
            return false;
        }
        str = str.trim();
        if (str.startsWith("+") || str.startsWith("-")) {
            if (str.length() == 1) {
                // "+" "-"
                return false;
            }
            begin = 1;
        }
        for (int i = begin; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /*
     * @描述 判断String是否是Long型
     * @param s
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:07:46
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:07:46
     */
    public static boolean isLong(String s) {
        return isInteger(s);
    }

    /*
     * @描述 判断字符串是否是浮点数
     * @param value
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:07:54
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:07:54
     */
    public static boolean isDouble(String value) {
        try {
            return isNumber(value);
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @描述 判断Object对象是否是字符串
     * @param obj
     * @return 
     * @author 冼土彪
     */
    public static boolean isNumber(Object obj){
    	String str = String.valueOf(obj);
    	return isNumber(str);
    }
    
    /*
     * @描述 判断字符串是否是数字
     * @param value
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:08:01
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:08:01
     */
    public static boolean isNumber(String str) {
        int begin = 0;
        boolean once = true;
        if (str == null || str.trim().equals("")) {
            return false;
        }
        str = str.trim();
        if (str.startsWith("+") || str.startsWith("-")) {
            if (str.length() == 1) {
                // "+" "-"
                return false;
            }
            begin = 1;
        }
        for (int i = begin; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                if (str.charAt(i) == '.' && once) {
                    // '.' can only once
                    once = false;
                }
                else {
                    return false;
                }
            }
        }
        if (str.length() == (begin + 1) && !once) {
            // "." "+." "-."
            return false;
        }
        return true;
    }

    /*
     * @描述 排序
     * @param array
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:08:13
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:08:13
     */
    public static void sort(int[] array) {// 小到大的排序
        int temp = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = i; j < array.length; j++) {
                if (array[i] > array[j]) {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    /*
     * @描述 是否是质数
     * @param n
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:08:24
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:08:24
     */
    public static boolean isPrimes(int n) {
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /*
     * @描述 阶乘
     * @param n
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:08:30
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:08:30
     */
    public static int factorial(int n) {
        if (n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    /*
     * @描述 平方根算法
     * @param x
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:08:37
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:08:37
     */
    public static long sqrt(long x) {
        long y = 0;
        long b = (~Long.MAX_VALUE) >>> 1;
        while (b > 0) {
            if (x >= y + b) {
                x -= y + b;
                y >>= 1;
                y += b;
            }
            else {
                y >>= 1;
            }
            b >>= 2;
        }
        return y;
    }

    private static int math_subnode(int selectNum, int minNum) {
        if (selectNum == minNum) {
            return 1;
        }
        else {
            return selectNum * math_subnode(selectNum - 1, minNum);
        }
    }

    private static int math_node(int selectNum) {
        if (selectNum == 0) {
            return 1;
        }
        else {
            return selectNum * math_node(selectNum - 1);
        }
    }

    /*
     * @描述 可以用于计算双色球、大乐透注数的方法 selectNum：选中了的小球个数 minNum：至少要选中多少个小球
     * 比如大乐透35选5可以这样调用processMultiple(7,5); 就是数学中的：C75=7*6/2*1
     * @param selectNum
     * @param minNum
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:08:50
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:08:50
     */
    public static int processMultiple(int selectNum, int minNum) {
        int result;
        result = math_subnode(selectNum, minNum) / math_node(selectNum - minNum);
        return result;
    }

    /*
     * @描述 求m和n的最大公约数
     * @param m
     * @param n
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:09:05
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:09:05
     */
    public static int gongyue(int m, int n) {
        while (m % n != 0) {
            int temp = m % n;
            m = n;
            n = temp;
        }
        return n;
    }

    /*
     * @描述 求两数的最小公倍数
     * @param m
     * @param n
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:09:11
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:09:11
     */
    public static int gongbei(int m, int n) {
        return m * n / gongyue(m, n);
    }

    /*
     * @描述 递归求两数的最大公约数
     * @param m
     * @param n
     * @return
     * @创建人 侯伟
     * @创建时间 2011-7-19 下午04:09:16
     * @修改人 侯伟
     * @修改时间 2011-7-19 下午04:09:16
     */
    public static int divisor(int m, int n) {
        if (m % n == 0) {
            return n;
        }
        else {
            return divisor(n, m % n);
        }
    }

    public static Double toDouble(String value) {
        BigDecimal bg = new BigDecimal(value);
        return bg.doubleValue();
    }

    public static Double toDouble(String value, Double defVal) {
        if (NumberUtil.isNumber(value)) {
            return toDouble(value);
        }
        return defVal;
    }

    public static Integer toInteger(String value, Integer defVal) {
        if (NumberUtil.isNumber(value)) {
            BigDecimal bg = new BigDecimal(value);
            return bg.intValue();
        }
        return defVal;
    }

    public static Long toLong(String value, Long defVal) {
        if (NumberUtil.isNumber(value)) {
            BigDecimal bg = new BigDecimal(value);
            return bg.longValue();
        }
        return defVal;
    }

    public static boolean isNull(Object[] objs) {
        return objs == null || objs.length <= 0;
    }

    public static String toString(Object[] objs) {
        return join(objs, ",");
    }

    public static String join(Object[] objs, String seperator) {
        if (isNull(objs)) {
            return null;
        }

        StringBuffer result = new StringBuffer();

        for (int i = 0; i < objs.length; i++) {
            result.append(objs[i].toString());
            if (i < objs.length - 1) {
                result.append(seperator);
            }
        }
        return result.toString();
    }

    /**
     * 提供精确的加法运算
     * 
     * @param v1
     * @param v2
     * @param scale
     *            :保留精度
     * @return 两个参数的和
     */
    public static double doubleAdd(double v1, double v2, int scale) {

        return (BigDecimal.valueOf(v1).add(BigDecimal.valueOf(v2))).setScale(scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }
    
    /**
     * 提供精确的加法运算
     * 
     * @param v1
     * @param v2
     * @param scale
     *            :保留精度
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2, int scale) {

        return v1.add(v2).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 提供精确的减法运算
     * 
     * @param v1
     * @param v2
     * @param scale
     *            :保留精度
     * @return 两个参数的差
     */
    public static double doubleSubtract(double v1, double v2, int scale) {
        return (BigDecimal.valueOf(v1).subtract(BigDecimal.valueOf(v2))).setScale(scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }
    

    /**
     * 提供精确的减法运算
     * 
     * @param v1
     * @param v2
     * @param scale
     *            :保留精度
     * @return 两个参数的差
     */
    public static BigDecimal numberSubtract(BigDecimal v1, BigDecimal v2, int scale) {
        return v1.subtract(v2).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 提供精确的除法运算
     * 
     * @param v1
     * @param v2
     * @param scale
     *            :保留精度
     * @return 两个参数的和
     */
    public static BigDecimal divide(BigDecimal v1, BigDecimal v2, int scale) {
        if (v1 == null || v1.equals(BigDecimal.ZERO) || v2 == null || v2.equals(BigDecimal.ZERO)) {
            return new BigDecimal(0);
        }
        return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的乘法运算
     * 
     * @param v1
     * @param v2
     * @param scale
     *            :保留精度
     * @return 两个参数的和
     */
    public static BigDecimal multiply(BigDecimal v1, BigDecimal v2, int scale) {
        if(v1 == null || v1.equals(BigDecimal.ZERO) || v2 == null || v2.equals(BigDecimal.ZERO)){
            return new BigDecimal(0);
        }
        return v1.multiply(v2).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * val为处理double 数字，precsion为保留小数位数。
     * 
     * @param val
     * @param precision
     * @return
     */
    public static Double roundDouble(double val, int precision) {
        Double ret = null;
        try {
            double factor = Math.pow(10, precision);
            ret = Math.floor(val * factor + 0.5) / factor;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String getNetmask(int prefixLength) {
        if (prefixLength < 0 || prefixLength > 31) {
            return null;
        }
        StringBuilder binaryNetmask = new StringBuilder();
        StringBuilder decimalNetmask = new StringBuilder();
        for (int i = 1; i <= prefixLength; i++) {
            binaryNetmask.append("1");
        }
        for (int i = 1; i <= (32 - prefixLength); i++) {
            binaryNetmask.append("0");
        }
        String a = binaryNetmask.toString().substring(0, 8);
        decimalNetmask.append(Integer.valueOf(a, 2));
        decimalNetmask.append(".");
        String b = binaryNetmask.toString().substring(8, 16);
        decimalNetmask.append(Integer.valueOf(b, 2));
        decimalNetmask.append(".");
        String c = binaryNetmask.toString().substring(16, 24);
        decimalNetmask.append(Integer.valueOf(c, 2));
        decimalNetmask.append(".");
        String d = binaryNetmask.toString().substring(24, 32);
        decimalNetmask.append(Integer.valueOf(d, 2));

        return decimalNetmask.toString();
    }

    /**
     * 判断传入的是否是正数
     * 
     * @param number
     * @return
     */
    public static boolean isPositiveNum(Number number) {
        if (number == null) {
            return false;
        }
        if (number.doubleValue() <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isEqual(Number a, Number b) {
        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {

            return false;
        }
        if (a instanceof Integer || b instanceof Integer) {
            if (a.intValue() != b.intValue()) {
                return false;
            }
            else {
                return true;
            }
        }
        if (a instanceof Double || b instanceof Double) {
            if (a.doubleValue() != b.doubleValue()) {
                return false;
            }
            else {
                return true;
            }
        }
        if (a instanceof Long || b instanceof Long) {
            if (a.longValue() != b.longValue()) {
                return false;
            }
            else {
                return true;
            }
        }
        return true;
    }

    public static boolean isNotEqual(Number a, Number b) {
        return !isEqual(a, b);
    }

}
