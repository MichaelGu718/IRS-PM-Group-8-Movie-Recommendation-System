package com.nusiss.movie.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.utils
 * <p>
 * Created by tangyi on 2022-10-26 23:51
 * @author tangyi
 */
public class EncryptUtil {
    public static String getMD5Str(String str) {
        str += "movie";
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest  = md5.digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        assert digest != null;
        return new BigInteger(1, digest).toString(16).toUpperCase();
    }

    public static String AESEncrypt(String message, String key) {
        // TO DO
        return message;
    }

    public static String AESDecrypt(String message, String key) {
        // TO DO
        return message;
    }
}
