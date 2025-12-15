package com.osudpotro.posmaster.product;

public class ProductDetailNotFoundException extends RuntimeException{
    public ProductDetailNotFoundException(){
        super("Product Detail not Found");
    }
    public ProductDetailNotFoundException(String message){
        super(message);
    }
}
