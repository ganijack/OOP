package smarthome.validation;

import smarthome.exception.ValidationException;

/**
 * Utility class to validate domain invariants (data integrity & encapsulation).
 */
public final class Validator {

    private Validator() {
        // Prevent instantiation of utility class
    }

    public static void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " must not be empty or null.");
        }
    }

    public static void requireRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new ValidationException(String.format("%s must be between %d and %d (given: %d).",
                    fieldName, min, max, value));
        }
    }

    public static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " must be strictly positive (> 0).");
        }
    }
}
