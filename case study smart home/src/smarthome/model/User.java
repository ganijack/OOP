package smarthome.model;

import smarthome.validation.Validator;

/**
 * Representasi pengguna (penghuni rumah) dalam ekosistem Smart Home.
 */
public class User {

    private final String userId;
    private final String name;
    private final String role; // contoh: "ADMIN", "MEMBER", "GUEST"

    public User(String userId, String name, String role) {
        Validator.requireNonEmpty(userId, "User ID");
        Validator.requireNonEmpty(name, "Nama User");
        Validator.requireNonEmpty(role, "Role User");

        this.userId = userId.trim();
        this.name = name.trim();
        this.role = role.trim().toUpperCase();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    @Override
    public String toString() {
        return String.format("%s (%s [%s])", name, userId, role);
    }
}
