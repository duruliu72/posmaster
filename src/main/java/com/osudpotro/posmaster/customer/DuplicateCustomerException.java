package com.osudpotro.posmaster.customer;

public class DuplicateCustomerException extends RuntimeException {
    public DuplicateCustomerException() {
        super("Customer already exists");
    }

    public DuplicateCustomerException(String message) {
        super(message);
    }
}
