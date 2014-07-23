/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */

package org.tfennelly.httpanalyse.common.security;

import org.tfennelly.httpanalyse.common.util.ByteUtils;
import org.tfennelly.httpanalyse.common.util.CharsetUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class AESUtils {

    // The methods in this class may require the installation of the unlimited
    // strength crypto files.  For Java6, these can be located at
    // http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html

    public static enum KEY_SIZE {

        KEY128BITS(128),
        KEY192BITS(192),
        KEY256BITS(256);

        private int keySize;

        KEY_SIZE(int keySize) {
            this.keySize = keySize;
        }

        public int toInt() {
            return keySize;
        }
    }

    private AESUtils() {
    }

    public static final String generateKeyString(KEY_SIZE keySize) {
        return ByteUtils.toHexString(generateKey(keySize));
    }

    public static final byte[] generateKey(KEY_SIZE keySize) {
        KeyGenerator keyGenerator;

        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unexpected Exception getting AES KeyGenerator.", e);
        }

        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unexpected Exception getting SecureRandom instance.", e);
        }
        UUID uuid = UUID.randomUUID();
        random.setSeed(uuid.toString().getBytes(CharsetUtils.UTF8));

        keyGenerator.init(keySize.toInt(), random);
        SecretKey skey = keyGenerator.generateKey();

        return skey.getEncoded();
    }

    public static Cipher toCipher(String key, int mode) throws InvalidKeyException {
        return toCipher(ByteUtils.fromHexString(key), mode);
    }

    public static Cipher toCipher(byte[] key, int mode) throws InvalidKeyException {
        Cipher cipher;

        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            throw new IllegalStateException("Unexpected Exception creating AES Cipher.", e);
        }

        cipher.init(mode, skeySpec);

        return cipher;
    }
}
