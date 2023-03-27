package com.fitnesspartner.utils.encryptor;

public interface Encryptor {
    String hashPassword(String raw);
    boolean isMatch(String raw, String hashed);
}
