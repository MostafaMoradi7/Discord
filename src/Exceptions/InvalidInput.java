package Exceptions;

public class InvalidInput extends Exception {
    public InvalidInput(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "InvalidInput: " + getMessage();
    }

}
