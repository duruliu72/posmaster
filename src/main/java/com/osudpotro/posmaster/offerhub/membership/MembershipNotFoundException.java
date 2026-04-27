package com.osudpotro.posmaster.offerhub.membership;


public class MembershipNotFoundException extends RuntimeException {
    public MembershipNotFoundException() {
        super("Branch not found");
    }

    public MembershipNotFoundException(String message) {
        super(message);
    }
}