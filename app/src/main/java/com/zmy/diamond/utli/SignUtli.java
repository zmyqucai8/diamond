package com.zmy.diamond.utli;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * 签名工具类
 * Created by zhangmengyun on 2018/8/27.
 */

public class SignUtli {

    public static String API_KEY = "b4b6e77c-5974-4cc6-924a-7d279d64c921";


    /**
     * 获取签名
     *
     * @return
     */
    public static String getSignature(Map<String, String> paramMap) {
        String signStr = MyUtlis.getSignKey();
        for (String value : paramMap.values()) {
//            System.out.println(value);
            signStr = signStr + value;
        }
//        System.out.println(signStr);
//        System.out.println(signStr.length());
//        System.out.println(signStr.getBytes().length);
        try {
            return makeMd5Sum(signStr.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return makeMd5Sum(signStr.getBytes());
    }


    /**
     * 判断字符串是否为空
     *
     * @param charSequence
     * @return
     */
    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    /**
     * 生成md5校验码
     *
     * @param srcContent 需要加密的数据
     * @return 加密后的md5校验码。出错则返回null。
     */
    public static String makeMd5Sum(byte[] srcContent) {
        if (srcContent == null) {
            return null;
        }
        String strDes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(srcContent);
            strDes = bytes2Hex(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * 将byte转换为字符串
     *
     * @param byteArray
     * @return
     */
    private static String bytes2Hex(byte[] byteArray) {
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (byteArray[i] >= 0 && byteArray[i] < 16) {
                strBuf.append("0");
            }
            strBuf.append(Integer.toHexString(byteArray[i] & 0xFF));
        }
        return strBuf.toString();
    }

}
