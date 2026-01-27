package com.osudpotro.posmaster.tag;

public class TagNotFoundException extends RuntimeException{
    public TagNotFoundException(){
        super("Tag not found");
    }
    public TagNotFoundException(String message) {
        super(message);
    }
}
