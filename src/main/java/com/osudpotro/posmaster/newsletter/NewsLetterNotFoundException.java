package com.osudpotro.posmaster.newsletter;

public class NewsLetterNotFoundException extends RuntimeException {
    public NewsLetterNotFoundException() {
        super("NewsLetter not found");
    }

    public NewsLetterNotFoundException(String message) {
        super(message);
    }
}
