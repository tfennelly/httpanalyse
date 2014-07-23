/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */

package org.tfennelly.httpanalyse.common.security;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash Utilities.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class HashUtil {

    public static final Charset UTF8 = Charset.forName("UTF-8");

    private HashUtil() {
    }

    public static byte[] toMD5Bytes(String string) {
        return toMD5Bytes(string.getBytes(UTF8));
    }

    public static byte[] toMD5Bytes(byte[] data) {
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unexpected error creating MD5 MessageDigest instance.", e);
        }

        return messageDigest.digest(data);
    }

    public static String toMD5String(String string) {
        byte[] digestedPWBytes = toMD5Bytes(string);
        return toHexString(digestedPWBytes);
    }

    public static String toMD5String(byte[] data) {
        byte[] digestedPWBytes = toMD5Bytes(data);
        return toHexString(digestedPWBytes);
    }

    private static String toHexString(byte[] bytes) {
        StringBuffer toStringBuffer = new StringBuffer();

        for (int i = 0; i < bytes.length; ++i) {
            toStringBuffer.append(Integer.toHexString((bytes[i] & 0xFF) | 0x100).substring(1, 3));
        }

        return toStringBuffer.toString();
    }
}
