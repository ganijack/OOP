package smarthome.validation;

import smarthome.exception.ValidationException;

/**
 * Utility class untuk memvalidasi batasan nilai (data integrity & encapsulation).
 */
public final class Validator {

    private Validator() {
        // Mencegah instansiasi utility class
    }

    public static void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " tidak boleh kosong atau null.");
        }
    }

    public static void requireRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new ValidationException(String.format("%s harus bernilai antara %d dan %d (diberikan: %d).",
                    fieldName, min, max, value));
        }
    }

    public static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " harus bernilai lebih dari 0.");
        }
    }
}
