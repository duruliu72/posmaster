package com.osudpotro.posmaster.variantunit;
public class VariantUnitNotFoundException extends RuntimeException {
    public VariantUnitNotFoundException(){
        super("Product Unit not Found");
    }
    public VariantUnitNotFoundException(String message){
        super(message);
    }
}