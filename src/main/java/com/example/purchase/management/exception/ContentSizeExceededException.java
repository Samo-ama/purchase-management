package com.example.purchase.management.exception;

public class ContentSizeExceededException extends Exception{

    private static final String DEFAULT_MESSAGE =
            "Email content exceeds the maximum size limit";

    public ContentSizeExceededException() {
        super(DEFAULT_MESSAGE);
    }

    public ContentSizeExceededException(String message) {
        super(message);
    }
}
