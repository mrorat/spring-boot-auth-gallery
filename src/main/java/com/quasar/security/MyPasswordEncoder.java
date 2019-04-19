package com.quasar.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyPasswordEncoder implements PasswordEncoder {

    public static void main(String[] args) {
    	System.out.println(new MyPasswordEncoder().encode("test"));
    }

    public String encode(CharSequence passwordPlaintext) {
        String salt = BCrypt.gensalt();
        String hashed_password = BCrypt.hashpw((String)passwordPlaintext, salt);
        return hashed_password;
    }

	@Override
	public boolean matches(CharSequence encodedPassword, String rawPassword) {
        boolean password_verified = false;
        if (null != encodedPassword && ((String)encodedPassword).startsWith("$2a$")) {
            password_verified = BCrypt.checkpw((String) rawPassword, (String)encodedPassword);
            return password_verified;
        } else {
            return false;
        }
	}
}
