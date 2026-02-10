package com.osudpotro.posmaster.user.admin;

public class AdminUserNotFoundException extends RuntimeException{
    public AdminUserNotFoundException() {
        super("Admin User not found");
    }

    public AdminUserNotFoundException(String message) {
        super(message);
    }
}
