package com.osudpotro.posmaster.newsletter.post;

public class PictureNotFoundException extends RuntimeException {
    public PictureNotFoundException(Long id) {
        super("Picture not found with ID: " + id);
    }
}
