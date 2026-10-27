package smarthome.contract;

/**
 * Interface untuk perangkat yang memiliki kemampuan terhubung ke jaringan Wi-Fi.
 * Menandakan kapabilitas "can-do" (dapat terhubung ke jaringan).
 */
public interface WiFiConnectable {

    /**
     * Menghubungkan perangkat ke Access Point Wi-Fi.
     *
     * @param ssid     nama jaringan Wi-Fi
     * @param password kata sandi Wi-Fi
     */
    void connectWiFi(String ssid, String password);

    /**
     * Memutus koneksi Wi-Fi dari jaringan.
     */
    void disconnectWiFi();

    /**
     * Memeriksa status keterhubungan dengan jaringan Wi-Fi.
     *
     * @return true jika terhubung, false jika tidak
     */
    boolean isWiFiConnected();

    /**
     * Mendapatkan alamat IP yang dialokasikan ke perangkat.
     *
     * @return String alamat IP atau null jika belum terhubung
     */
    String getIpAddress();
}
