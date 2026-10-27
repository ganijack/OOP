package smarthome.exception;

/**
 * Custom Exception untuk menangani kesalahan validasi input data
 * pada sistem Smart Home.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
