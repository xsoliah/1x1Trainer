package com.example.manuel.a1x1trainer.Security;

import org.apache.commons.codec.binary.Hex;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Security Algorithm
 *
 * Provides the necessary security routines
 */
public class SecurityAlgorithm {
    private final static String SHA256_INSTANCE = "SHA-256";
    private final static String UTF8 = "UTF-8";
    private final static String ZERO = "0";

    /**
     * performs a sha256 hashing
     * @param base string to hash
     * @return 256bit hash-value as string
     */
    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance(SHA256_INSTANCE);
            byte[] hash = digest.digest(base.getBytes(UTF8));
            StringBuffer hexString = new StringBuffer();

            for (byte aHash : hash) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) hexString.append(ZERO);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private final static String HMAC_SHA256 = "HmacSHA256";

    /**
     * performs a hmac sha256 encryption
     * @param key hmac key
     * @param data string to encrypt
     * @return encrypted string
     */
    public static String hmac_sha256(String key, String data) {
        try {

            Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(UTF8), HMAC_SHA256);
            sha256_HMAC.init(secret_key);

            return new String(Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes(UTF8))));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
