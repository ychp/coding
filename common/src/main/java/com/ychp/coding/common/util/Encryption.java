package com.ychp.coding.common.util;

import com.google.common.base.Objects;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.ychp.coding.common.exception.EncryptionException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

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
     * @return
     * @throws Exception
     */
    public static String Decrypt3DES(String value, String key) throws EncryptionException {
        byte[] b = decryptMode(GetKeyBytes(key), Base64.decode(value));
        return new String(b);
    }

    /**
     * 3des加密
     * @param value 待加密字符串
     * @param key 原始密钥字符串
     * @return
     * @throws Exception
     */
    public static String Encrypt3DES(String value, String key) throws EncryptionException {
        String str = byte2Base64(encryptMode(GetKeyBytes(key), md5Encode(value, key).getBytes()));
        return str;
    }

    /**
     * 计算24位长的密码byte值,首先对原始密钥做MD5算hash值，再用前8位数据对应补全后8位
     * @param strKey
     * @return
     * @throws Exception
     */
    public static byte[] GetKeyBytes(String strKey) throws EncryptionException {
        if (null == strKey || strKey.length() < 1) {
            throw new EncryptionException("encryption.key.not.empty");
        }

        MessageDigest alg = null;
        try {
            alg = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("encryption.getMd5.fail");
        }
        alg.update(strKey.getBytes());
        byte[] bkey = alg.digest();

        int start = bkey.length;
        byte[] bkey24 = new byte[KEY_LENGTH];

        for (int i = 0; i < start; i++) {
            bkey24[i] = bkey[i];
        }

        for (int i = start; i < KEY_LENGTH; i++) {//为了与.net16位key兼容
            bkey24[i] = bkey[i - start];
        }

        return bkey24;
    }

    /**
     *
     * @param keybyte 加密密钥，长度为24字节
     * @param src 为被加密的数据缓冲区（源）
     * @return
     */
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {

        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm); //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }

        return null;
    }


    /**
     *
     * @param keybyte 加密密钥，长度为24字节
     * @param src 为被加密的数据缓冲区（源）
     * @return
     */
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {

        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }

        return null;
    }

    /**
     * 转换成base64编码
     * @param b
     * @return
     */
    public static String byte2Base64(byte[] b) {
        return Base64.encode(b);
    }

    /***
     * MD5加密 生成32位md5码
     * @param inStr 待加密字符串
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr, String key) {
        MessageDigest md5;
        try {
            String password = inStr + key.substring(0,8);
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = password.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString().substring(0,16);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 验证密码
     * @param value
     * @param key
     * @param equStr
     * @return
     */
    public boolean checkPassword(String value, String key, String equStr){
        String inStr = md5Encode(value, key);
        String originStr =Decrypt3DES(equStr, key);
        return Objects.equal(inStr, originStr);
    }

    public static String getSalt() {
        int length = KEY_LENGTH;
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
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
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static void main(String[] args) throws Exception {
        String salt = getSalt();
        System.out.println("key:" + salt);
        String password = "123456";
        password = Encrypt3DES(password, salt);
        System.out.println("password:" + password);
        password = Decrypt3DES(password, salt);
        System.out.println("origin_password:" + password);

        String password1 = "123456";
        password1 = Encrypt3DES(password1, salt);
        System.out.println("password1:" + password1);
        password1 = Decrypt3DES(password1, salt);
        System.out.println("origin_password:" + password1);

    }

}
