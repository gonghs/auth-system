package com.maple.starter.shiro.utils;

import lombok.experimental.UtilityClass;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * shiro工具
 *
 * @author maple
 * @version 1.0
 * @since 2020-07-01 10:44
 */
@UtilityClass
public class ShiroUtils {
    public String genMd5Password(String password, String saltStr, int times) {
        return genPassword("MD5", password, saltStr, times);
    }

    public String genPassword(String algorithmName, String password, String saltStr, int times) {
        SimpleHash simpleHash = new SimpleHash(algorithmName, password, ByteSource.Util.bytes(saltStr), times);
        return simpleHash.toString();
    }
}
