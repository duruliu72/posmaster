package com.osudpotro.posmaster.newsletter.post;

public class NewsLetterPageNotFoundException extends RuntimeException {
    public NewsLetterPageNotFoundException(Long id) {
        super("NewsLetter Page not found with ID: " + id);
    }
}
