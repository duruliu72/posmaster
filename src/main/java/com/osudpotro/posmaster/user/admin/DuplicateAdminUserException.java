package com.osudpotro.posmaster.user.admin;

public class DuplicateAdminUserException extends RuntimeException{
    public DuplicateAdminUserException() {
        super("Admin already exists");
    }

    public DuplicateAdminUserException(String message) {
        super(message);
    }
}
