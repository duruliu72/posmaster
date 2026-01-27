package com.osudpotro.posmaster.requisition;

public class RequsitionNotFoundException extends RuntimeException{
    public RequsitionNotFoundException(){
        super("Approver not Found");
    }
    public RequsitionNotFoundException(String message){
        super(message);
    }
}
