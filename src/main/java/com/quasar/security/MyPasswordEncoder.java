package com.quasar.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyPasswordEncoder implements PasswordEncoder {
    private static int logRounds = 12;

    public MyPasswordEncoder() {
    }
    
    public static void main(String[] args) {
    	System.out.println(new MyPasswordEncoder().encode("test"));
    }

    public String encode(CharSequence passwordPlaintext) {
        String salt = BCrypt.gensalt(logRounds);
        String hashed_password = BCrypt.hashpw((String)passwordPlaintext, salt);
        return hashed_password;
    }

    public boolean matches(CharSequence storedHash, String passwordPlaintext) {
        boolean password_verified = false;
        if (null != storedHash && ((String)storedHash).startsWith("$2a$")) {
            password_verified = BCrypt.checkpw(passwordPlaintext, (String)storedHash);
            return password_verified;
        } else {
            throw new IllegalArgumentException("Invalid hash provided for comparison");
        }
    }
}
