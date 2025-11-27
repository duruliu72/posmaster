package com.osudpotro.posmaster.organization;

public class OrganizationNotFoundException extends RuntimeException {
    public OrganizationNotFoundException(){
        super("Organization not found");
    }
    public OrganizationNotFoundException(String message) {
        super(message);
    }
}