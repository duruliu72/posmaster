package com.osudpotro.posmaster.varianttype;

public class VariantTypeNotFoundException extends RuntimeException{
    public VariantTypeNotFoundException(){
        super("Supplier not Found");
    }
    public VariantTypeNotFoundException(String message){
        super(message);
    }
}
