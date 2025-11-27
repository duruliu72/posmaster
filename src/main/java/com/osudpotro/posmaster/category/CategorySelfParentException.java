package com.osudpotro.posmaster.category;

public class CategorySelfParentException extends RuntimeException {
    public CategorySelfParentException() {
        super("Parent Category Same As Child Category");
    }

    public CategorySelfParentException(String message) {
        super(message);
    }
}