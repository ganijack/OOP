package smarthome.hub;

import smarthome.model.User;
import smarthome.validation.Validator;

/**
 * SUBCLASS: SecureCloudHub (High-Security Cloud Hub)
 * Allows encrypted remote device management over the internet.
 * Enforces strict Role-Based Access Control (RBAC): Only ADMIN users are authorized.
 */
public class SecureCloudHub extends SmartHub {

    private final String serverRegion;

    public SecureCloudHub(String hubId, String hubName, User owner, String serverRegion) {
        super(hubId, hubName, owner);
        Validator.requireNonEmpty(serverRegion, "Server Region");
        this.serverRegion = serverRegion.trim();
    }

    @Override
    public boolean canAccess(User user) {
        if (user == null) {
            return false;
        }
        // Cloud control restricted to Admin users or the primary account owner
        return user.isAdmin() || user.getUserId().equals(getOwner().getUserId());
    }

    @Override
    public String getHubType() {
        return "Secure Cloud Hub [Region: " + serverRegion + "] (Admin/Owner Exclusive)";
    }

    public String getServerRegion() {
        return serverRegion;
    }
}
