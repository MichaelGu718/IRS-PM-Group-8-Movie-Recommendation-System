package com.nusiss.movie.utils;

import org.springframework.util.StringUtils;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.utils
 * <p>
 * Created by tangyi on 2022-10-26 23:53
 * @author tangyi
 */
public class TokenUtil {
    private static final String SEPARATOR = "-";
    private static final Integer PARAM_LENGTH = 2;
    private static final String KEY = "nusissmovierecmd";

    /**
     * Token Format：timestamp-userId
     */
    public static String generateToken(long userId) {
        String token = System.currentTimeMillis() + SEPARATOR + userId;
        return EncryptUtil.AESEncrypt(token, KEY);
    }

    /**
     * Parse Token, get userId from it
     */
    public static Integer getUserIdFromToken(String token) {
        token = EncryptUtil.AESDecrypt(token, KEY);
        if (!StringUtils.hasLength(token)) {
            return null;
        }
        String[] param = token.split(SEPARATOR);
        if (param.length != PARAM_LENGTH) {
            return null;
        }
        try {
            return Integer.parseInt(param[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
