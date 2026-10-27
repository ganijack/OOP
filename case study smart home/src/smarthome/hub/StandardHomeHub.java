package smarthome.hub;

import smarthome.model.User;

/**
 * SUBCLASS: StandardHomeHub (Local Area Household Hub)
 * Coordinates smart devices over the local residential network.
 * Grants open control access to any registered household member (ADMIN, MEMBER, GUEST).
 */
public class StandardHomeHub extends SmartHub {

    public StandardHomeHub(String hubId, String hubName, User owner) {
        super(hubId, hubName, owner);
    }

    @Override
    public boolean canAccess(User user) {
        // Local residential hub allows access to any recognized user
        return user != null;
    }

    @Override
    public String getHubType() {
        return "Local Standard Hub (Open Access for Household)";
    }
}
