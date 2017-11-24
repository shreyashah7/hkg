package com.argusoft.hkg.web.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;

/**
 * Utility class to support token generation and validation of token
 *
 * @author rajkumar
 */
public class TokenUtils {

    public static final String MAGIC_KEY = "obfuscate";
    public static final String MAGIC_KEY_MASTER = "hkgmaster";
    public static final String MAGIC_KEY_CENTER = "hkgcenter";

    public static String createToken(UserDetails userDetails, String megicKey) {
        /*
         * Expires in eight hour
         */
        long expires = System.currentTimeMillis() + 1000L * 60 * 60 * 8;

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(userDetails.getUsername());
        tokenBuilder.append(":");
        tokenBuilder.append(expires);
        tokenBuilder.append(":");
        tokenBuilder.append(TokenUtils.computeSignature(userDetails, expires, megicKey));

        return tokenBuilder.toString();
    }

    public static String computeSignature(UserDetails userDetails, long expires, String megicKey) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername());
        signatureBuilder.append(":");
        signatureBuilder.append(expires);
        signatureBuilder.append(":");
        signatureBuilder.append(userDetails.getPassword());
        signatureBuilder.append(":");
        signatureBuilder.append(megicKey);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }

        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }

    public static String getUserNameFromToken(String authToken) {
        if (authToken != null) {
            String[] parts = authToken.split(":");
            return parts[0];
        } else {
            return null;
        }
    }

    public static boolean validateToken(String authToken, UserDetails userDetails, String megicKey) {
        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String signature = parts[2];
        //If token expired than it is invalid
        if (expires < System.currentTimeMillis()) {
            return false;
        }
        //Regenerate token and match with received token
        return signature.equals(TokenUtils.computeSignature(userDetails, expires, megicKey));
    }
}
