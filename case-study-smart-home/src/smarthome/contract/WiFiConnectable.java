package smarthome.contract;

/**
 * Interface representing the capability to connect to a Wi-Fi wireless network.
 * Designates a "can-do" capability contract.
 */
public interface WiFiConnectable {

    /**
     * Connects the device to a specified wireless network Access Point.
     *
     * @param ssid     network SSID
     * @param password network security key
     */
    void connectWiFi(String ssid, String password);

    /**
     * Disconnects the device from the current Wi-Fi network.
     */
    void disconnectWiFi();

    /**
     * Checks whether the device currently has an active Wi-Fi connection.
     *
     * @return true if connected, false otherwise
     */
    boolean isWiFiConnected();

    /**
     * Retrieves the allocated IP address of the device.
     *
     * @return String IP address or null if offline
     */
    String getIpAddress();
}
