package com.osudpotro.posmaster.picture;

public class PictureNotFoundException extends RuntimeException{
    public PictureNotFoundException() {
        super("Picture not found");
    }

    public PictureNotFoundException(String message) {
        super(message);
    }
}
