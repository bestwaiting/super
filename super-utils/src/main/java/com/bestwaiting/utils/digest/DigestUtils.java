package com.bestwaiting.utils.digest;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by bestwaiting on 17/8/14.
 */
@Slf4j
public class DigestUtils {

    /**
     * 字符编码
     */
    private static final String CHARSET = "UTF-8";
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 私有构造函数
     */
    private DigestUtils() {
    }

    /**
     * md5签名.
     *
     * @param s 字符串
     * @return md5
     */
    public static String md5(String s) {
        return md5(s, CHARSET);
    }

    /**
     * md5签名.
     *
     * @param s       字符串
     * @param charset 字符编码
     * @return md5
     */
    public static String md5(String s, String charset) {
        try {
            byte[] btInput;
            if (charset == null || charset.trim().equals("")) {
                btInput = s.getBytes();
            } else {
                btInput = s.getBytes(charset);
            }
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            return convertByte2Str16(md);

        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        }
        return null;
    }

    /**
     * sha1签名.
     *
     * @param s 字符串
     * @return sha1
     */
    public static String sha1(String s) {
        return sha1(s, CHARSET);
    }

    /**
     * sha1签名.
     *
     * @param s       字符串
     * @param charset 字符编码
     * @return sha1
     */
    public static String sha1(String s, String charset) {
        try {
            byte[] btInput;
            if (charset == null || charset.trim().equals("")) {
                btInput = s.getBytes();
            } else {
                btInput = s.getBytes(charset);
            }

            // 获得SHA1摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("SHA1");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            return convertByte2Str16(md);

        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        }
        return null;
    }

    /**
     * 把密文字节数组转换成十六进制的字符串形式
     *
     * @param md
     * @return
     */
    public static String convertByte2Str16(byte[] md) {
        int length = md.length;
        StringBuffer buffer = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            byte byte0 = md[i];
            buffer.append(hexDigits[byte0 >>> 4 & 0xf]);
            buffer.append(hexDigits[byte0 & 0xf]);
        }
        return buffer.toString();
    }
}
