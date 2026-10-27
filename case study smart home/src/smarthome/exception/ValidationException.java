package smarthome.exception;

/**
 * Custom runtime exception to handle domain validation errors
 * within the Smart Home ecosystem.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
