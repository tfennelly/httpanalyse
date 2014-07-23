/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */

package org.tfennelly.httpanalyse.common.util;

import java.util.Formatter;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class ByteUtils {

    private ByteUtils() {
    }

    public static byte[] fromHexString(String hex) {
        final byte[] buf = new byte[hex.length() / 2];
        for (int i = 0, j = 0; i < hex.length(); i += 2) {
            buf[j++] = (byte) (fromDigit(hex.charAt(i)) << 4 | fromDigit(hex.charAt(i + 1)));
        }
        return buf;
    }

    public static String toHexString(byte buf[]) {
        final Formatter formatter = new Formatter(new StringBuffer());
        for (int i = 0; i < buf.length; i++) {
            formatter.format("%02x", 0xff & buf[i]);
        }
        return formatter.toString();
    }

    private static int fromDigit(char ch) {
        if ((ch >= '0') && (ch <= '9')) {
            return ch - '0';
        } else if ((ch >= 'A') && (ch <= 'F')) {
            return ch + 10 - 'A';
        } else if ((ch >= 'a') && (ch <= 'f')) {
            return ch + 10 - 'a';
        } else {
            throw new IllegalArgumentException(String.format("Invalid hex character 0x%04x", 0xff & ch));
        }
    }
}
