package com.example.clientfront;

public class InvalidInput extends Exception {
    public InvalidInput(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "com.example.clientfront.InvalidInput: " + getMessage();
    }

}
