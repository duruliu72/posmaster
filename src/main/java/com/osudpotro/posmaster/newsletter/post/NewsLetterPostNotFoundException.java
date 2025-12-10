package com.osudpotro.posmaster.newsletter.post;

public class NewsLetterPostNotFoundException extends RuntimeException {
    public NewsLetterPostNotFoundException(Long id) {
        super("NewsLetter Post not found with ID: " + id);
    }
}
