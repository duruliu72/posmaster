package com.osudpotro.posmaster.user.customer;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException() {
        super("Customer not found");
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
