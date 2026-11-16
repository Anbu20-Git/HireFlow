package com.hireflow;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hashedPassword = encoder.encode("Hr@123");

        System.out.println("BCrypt Password:");
        System.out.println(hashedPassword);
    }
}