/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author gautam
 * Simple AES Encryption uses initialization vector and a security key of 128 bit.
 * Then uses base64 encoding/decoding
 */
public class AesEncrypter {
    
    public static String initializationVector = "B4r1PZ45UUr82Lp5"; // 128 bit key
    public static String secretKey = "Th7U094SYEPe8mez"; // Secret key

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initializationVector.getBytes("UTF-8"));

            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"),
                    "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string:"
                    + Base64.encodeBase64String(encrypted));
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initializationVector.getBytes("UTF-8"));

            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"),
                    "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
