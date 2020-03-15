package com.maple.utils;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;

/**
 * DigestUtils
 *
 * @author maple
 * @date 2019-9-10
 */
public class DigestUtils {

    /**
     * 功能描述: MD5加密账号密码
     *
     * @param userName 用户名
     * @param password 密码
     */
    public static String md5(String userName, String password) {
        Md5Hash hash = new Md5Hash(password, ByteSource.Util.bytes(userName), 2);
        return hash.toString();
    }
}
