package com.vetcontrol.vetcontrolbackend.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordHasher {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 100_000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_BYTES = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static boolean verify(String password, String storedHash) {
        if (storedHash == null || !storedHash.contains(":")) {
            return false;
        }
        String[] parts = storedHash.split(":");
        if (parts.length != 3) return false;
        try {
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] hash = Base64.getDecoder().decode(parts[2]);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
            try {
                SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
                byte[] testHash = skf.generateSecret(spec).getEncoded();
                return MessageDigest.isEqual(hash, testHash);
            } finally {
                spec.clearPassword();
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static String hash(String password) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hashBytes = skf.generateSecret(spec).getEncoded();
            spec.clearPassword();
            return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static boolean verifyLegacy(String password, String storedHash) {
        if (storedHash == null || storedHash.contains(":")) return false;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString().equalsIgnoreCase(storedHash);
        } catch (Exception e) {
            return false;
        }
    }
}
