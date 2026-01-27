package com.osudpotro.posmaster.multimedia;

public class MultimediaNotFoundException extends RuntimeException{
    public MultimediaNotFoundException() {
        super("Picture not found");
    }

    public MultimediaNotFoundException(String message) {
        super(message);
    }
}
