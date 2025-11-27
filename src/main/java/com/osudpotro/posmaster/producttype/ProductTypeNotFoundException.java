package com.osudpotro.posmaster.producttype;

public class ProductTypeNotFoundException extends RuntimeException {
    public ProductTypeNotFoundException(){
        super("Product Type not Found");
    }
    public ProductTypeNotFoundException(String message){
        super(message);
    }
}