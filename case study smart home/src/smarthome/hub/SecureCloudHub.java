package smarthome.hub;

import smarthome.model.User;
import smarthome.validation.Validator;

/**
 * SUBCLASS: SecureCloudHub (Hub Cloud Terenkripsi Berkeamanan Tinggi)
 * Memungkinkan kontrol perangkat jarak jauh melalui internet/cloud.
 * Menerapkan kontrol akses ketat: Hanya user dengan role "ADMIN" yang dapat mengontrol.
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
        // Hanya pemilik utama atau user dengan role ADMIN yang diizinkan mengakses kontrol cloud
        return user.isAdmin() || user.getUserId().equals(getOwner().getUserId());
    }

    @Override
    public String getHubType() {
        return "Secure Cloud Hub [Region: " + serverRegion + "] (Hanya Akses Admin/Owner)";
    }

    public String getServerRegion() {
        return serverRegion;
    }
}
