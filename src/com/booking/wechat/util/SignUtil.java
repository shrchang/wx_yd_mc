package com.booking.wechat.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.booking.wechat.controller.wechat.WechatRequestModel;
 
public class SignUtil {
     
    public static boolean checkSignature(String token,WechatRequestModel model){
        String[] arr = new String[]{token, model.getTimestamp(), model.getNonce()};
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for(int i = 0; i < arr.length; i++){
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;
         
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        content = null;
        return tmpStr != null ? tmpStr.equals(model.getSignature().toUpperCase()): false;
    }
     
    private static String byteToStr(byte[] digest) {
        String strDigest = "";
        for(int i = 0; i < digest.length; i++){
            strDigest += byteToHexStr(digest[i]);
        }
        return strDigest;
    }
     
    private static String byteToHexStr(byte b) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(b >>> 4) & 0X0F];
        tempArr[1] = Digit[b & 0X0F];
         
        String s = new String(tempArr);
        return s;
    }
}
