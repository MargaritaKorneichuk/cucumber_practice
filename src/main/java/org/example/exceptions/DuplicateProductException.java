package org.example.exceptions;

public class DuplicateProductException extends Exception {
    public DuplicateProductException(String message) {
        super(message);
    }
}
