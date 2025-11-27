package com.osudpotro.posmaster.supplier;

public class SupplierNotFoundException extends RuntimeException {
    public SupplierNotFoundException(){
        super("Supplier not Found");
    }
    public SupplierNotFoundException(String message){
        super(message);
    }
}