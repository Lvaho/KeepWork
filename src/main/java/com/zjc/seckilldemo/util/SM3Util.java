package com.zjc.seckilldemo.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

public class SM3Util {
    private static final String salt = "1a2b3c4d";
    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        return SM3Encrypt(str);
    }
    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
        return SM3Encrypt(str);
    }
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static String SM3Encrypt(String a){
        byte[] md = new byte[32];
        byte[] msg1 = a.getBytes();
        SM3Digest sm3 = new SM3Digest();
        sm3.update(msg1, 0, msg1.length);
        sm3.doFinal(md, 0);
        String s = new String(Hex.encode(md));
        return s;
    }
    /**
     * 字节数组转换为十六进制字符串
     *
     * @param b byte[] 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public static String byteToHex(byte b[]) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toLowerCase();
        //return hs.toUpperCase();
    }
    public static void main(String[] args) {
        String x = inputPassToFormPass("123456");
        System.out.println(x);//f8754eae351227871e68eca11a10579adf8c816c380da79af3d8712761e39227
        System.out.println(formPassToDBPass("f8754eae351227871e68eca11a10579adf8c816c380da79af3d8712761e39227","1a2b3c4d"));//6764ff9fe99bdb23d46e3d7e08aaa983a8c293be8ca6e03aa3091d9fc6e0c867
        System.out.println(inputPassToDbPass("123456","1a2b3c4d"));//6764ff9fe99bdb23d46e3d7e08aaa983a8c293be8ca6e03aa3091d9fc6e0c867
        System.out.println(x.length());
    }
}
