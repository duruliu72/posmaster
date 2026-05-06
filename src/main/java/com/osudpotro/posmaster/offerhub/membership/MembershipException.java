package com.osudpotro.posmaster.offerhub.membership;


public class MembershipException extends RuntimeException {
    public MembershipException() {
        super("Entity Exception found");
    }

    public MembershipException(String message) {
        super(message);
    }
}