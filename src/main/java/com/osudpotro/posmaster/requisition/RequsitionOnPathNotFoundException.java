package com.osudpotro.posmaster.requisition;

public class RequsitionOnPathNotFoundException extends RuntimeException{
    public RequsitionOnPathNotFoundException(){
        super("Approver not Found");
    }
    public RequsitionOnPathNotFoundException(String message){
        super(message);
    }
}
