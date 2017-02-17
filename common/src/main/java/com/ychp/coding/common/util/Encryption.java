package com.ychp.coding.common.util;

import com.google.common.base.Objects;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.ychp.coding.common.exception.EncryptionException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/31
 */
public class Encryption {

    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish

    private static final int KEY_LENGTH = 24; //密钥长度

    /**
     * 3des解密
     * @param value 待加密字符串
     * @param key 原始密钥字符串
     */
    private static String Decrypt3DES(String value, String key) throws EncryptionException {
        byte[] b = decryptMode(GetKeyBytes(key), Base64.decode(value));
        return new String(b != null ? b : new byte[0]);
    }

    /**
     * 3des加密
     * @param value 待加密字符串
     * @param key 原始密钥字符串
     */
    public static String Encrypt3DES(String value, String key) throws EncryptionException {
        return byte2Base64(encryptMode(GetKeyBytes(key), md5Encode(value, key).getBytes()));
    }

    /**
     * 计算24位长的密码byte值,首先对原始密钥做MD5算hash值，再用前8位数据对应补全后8位
     */
    private static byte[] GetKeyBytes(String strKey) throws EncryptionException {
        if (null == strKey || strKey.length() < 1) {
            throw new EncryptionException("encryption.key.not.empty");
        }

        MessageDigest alg;
        try {
            alg = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("encryption.getMd5.fail");
        }
        alg.update(strKey.getBytes());
        byte[] bkey = alg.digest();

        int start = bkey.length;
        byte[] bkey24 = new byte[KEY_LENGTH];

        System.arraycopy(bkey, 0, bkey24, 0, start);

        System.arraycopy(bkey, 0, bkey24, start, KEY_LENGTH - start);

        return bkey24;
    }

    /**
     *
     * @param keybyte 加密密钥，长度为24字节
     * @param src 为被加密的数据缓冲区（源）
     */
    private static byte[] encryptMode(byte[] keybyte, byte[] src) {

        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm); //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (Exception e3) {
            e3.printStackTrace();
        }

        return null;
    }


    /**
     *
     * @param keybyte 加密密钥，长度为24字节
     * @param src 为被加密的数据缓冲区（源）
     */
    private static byte[] decryptMode(byte[] keybyte, byte[] src) {

        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (Exception e3) {
            e3.printStackTrace();
        }

        return null;
    }

    /**
     * 转换成base64编码
     */
    private static String byte2Base64(byte[] b) {
        return Base64.encode(b);
    }

    /***
     * MD5加密 生成32位md5码
     * @param inStr 待加密字符串
     * @return 返回32位md5码
     */
    private static String md5Encode(String inStr, String key) {
        MessageDigest md5;
        try {
            String password = inStr + key.substring(0,8);
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = password.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuilder hexValue = toHexString(md5Bytes);
            return hexValue.toString().substring(0,16);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 验证密码
     */
    public static boolean checkPassword(String value, String key, String equStr){
        String inStr = md5Encode(value, key);
        String originStr = Decrypt3DES(equStr, key);
        return Objects.equal(inStr, originStr);
    }

    public static String getSalt() {
        return CustomerStringUtils.getRandomString(KEY_LENGTH);
    }

    /***
     * MD5加密 生成32位md5码
     * @param inStr 待加密字符串
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = inStr.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuilder hexValue = toHexString(md5Bytes);
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static StringBuilder toHexString(byte[] md5Bytes){
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue;
    }

    public static String factoryAppCode(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static String factoryAppSecret(String appCode, String appName){
        return md5Encode(appName + System.currentTimeMillis() + appCode);
    }

    public static void main(String[] args) throws Exception {
        String salt = "grqx5iCM2Ma8KT9x1hja6acW";
        System.out.println("key:" + salt);
        String password = "123456";
        System.out.println("password:" + md5Encode(password, salt));
        password = Encrypt3DES(password, salt);
        System.out.println("password:" + password);
        password = Decrypt3DES(password, salt);
        System.out.println("origin_password:" + password);

    }

}
