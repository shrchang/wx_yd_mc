package com.booking.wechat.util;



/**
 * wincenter 加密工具类
 * @author qm
 *
 */
public class EncryptUntil {
	 
	 /**
	   * 系统DES加密key<br/>
	   * 加密工具默认加密key
	  */
	 public static final String DESKEY = "WEIXIN2015";

	 
	 
	 private static Endecrypt endecrypt;
	 
	/**
	 * 使用系统默认key加密
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public static String encrypt(String value) throws Exception{
		return getEndecrypt().get3DESEncrypt(value,DESKEY);
	}
	
	
	/**
	 * 提供key进行加密
	 * @param value
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public static String encrypt(String value,String key) throws Exception{
		return getEndecrypt().get3DESEncrypt(value,key);
	}
	
	/**
	 * 使用系统默认key解密
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public static String decrypt(String value) throws Exception{
		return getEndecrypt().get3DESDecrypt(value,DESKEY);
	}
	
	/**
	 * 使用自定义key解密
	 * @param value
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public static String decrypt(String value,String key) throws Exception{
		return  getEndecrypt().get3DESDecrypt(value, key);
	}
	
	
	/**
	 * 获取加密类
	 * @return
	 */
	private static Endecrypt getEndecrypt(){
		if(endecrypt == null){
			return new Endecrypt();
		}
		return endecrypt;
	}
	
}
