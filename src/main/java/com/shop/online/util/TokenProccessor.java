package com.shop.online.util;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/8 0008
 */
public class TokenProccessor {

    private TokenProccessor() {
    }

    private static final TokenProccessor tokenProccessor = new TokenProccessor();

    public static TokenProccessor getInstance() {
        return tokenProccessor;
    }

    public String makeToken() {
        String token = (System.currentTimeMillis() + new Random().nextInt(999999999)) + "";

        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte md5[] = md.digest(token.getBytes());
            //base64编码--任意二进制编码明文字符   adfsdfsdfsf
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(md5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
