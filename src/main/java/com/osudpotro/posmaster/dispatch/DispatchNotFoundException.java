package com.osudpotro.posmaster.dispatch;

public class DispatchNotFoundException extends RuntimeException{
    public DispatchNotFoundException(){
        super("Dispatch not Found");
    }
    public DispatchNotFoundException(String message){
        super(message);
    }
}
