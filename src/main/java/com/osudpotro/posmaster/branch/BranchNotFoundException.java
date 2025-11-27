package com.osudpotro.posmaster.branch;


public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException() {
        super("Branch not found");
    }

    public BranchNotFoundException(String message) {
        super(message);
    }
}