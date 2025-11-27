package com.osudpotro.posmaster.category;

public class ParentCategoryNotFoundException extends RuntimeException {
    public ParentCategoryNotFoundException() {
        super("Category not found");
    }

    public ParentCategoryNotFoundException(String message) {
        super(message);
    }
}