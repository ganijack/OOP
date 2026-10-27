package smarthome.hub;

import smarthome.model.User;

/**
 * SUBCLASS: StandardHomeHub (Hub Jaringan Lokal Rumah)
 * Mengontrol perangkat pintar melalui jaringan lokal (LAN / Offline).
 * Mengizinkan seluruh anggota keluarga (ADMIN, MEMBER, GUEST) untuk mengontrol perangkat.
 */
public class StandardHomeHub extends SmartHub {

    public StandardHomeHub(String hubId, String hubName, User owner) {
        super(hubId, hubName, owner);
    }

    @Override
    public boolean canAccess(User user) {
        // Pada hub lokal rumah, setiap penghuni yang terhubung diizinkan
        return user != null;
    }

    @Override
    public String getHubType() {
        return "Local Standard Hub (Akses Terbuka untuk Semua Penghuni)";
    }
}
