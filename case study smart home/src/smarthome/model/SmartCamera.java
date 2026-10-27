package smarthome.model;

import smarthome.contract.BatteryPowered;
import smarthome.contract.WiFiConnectable;
import smarthome.validation.Validator;

/**
 * SUBCLASS: SmartCamera (Surveillance Camera)
 * Extends SmartDevice and implements WiFiConnectable and BatteryPowered.
 * Acts as the base class for SmartDoorbellCamera (forming Multilevel Inheritance).
 */
public class SmartCamera extends SmartDevice implements WiFiConnectable, BatteryPowered {

    private final String resolution; // e.g., "1080p", "2K", "4K"
    private boolean nightVisionActive;
    private boolean isRecording;

    // BatteryPowered state
    private int batteryLevel; // 0 - 100%

    // WiFiConnectable state
    private boolean wiFiConnected;
    private String connectedSsid;
    private String ipAddress;

    public SmartCamera(String id, String name, String room, double basePowerWatt, String resolution, int initialBattery) {
        super(id, name, room, basePowerWatt);
        Validator.requireNonEmpty(resolution, "Camera Resolution");
        this.resolution = resolution.trim();
        this.batteryLevel = initialBattery;
        this.nightVisionActive = false;
        this.isRecording = false;
        this.wiFiConnected = false;
    }

    public void setNightVision(boolean active) {
        this.nightVisionActive = active;
        System.out.printf("[%s] Night Vision mode %s.%n", getId(), active ? "ENABLED" : "DISABLED");
    }

    public void startRecording() {
        if (!isOn()) {
            System.out.printf("[%s] Cannot record: Camera is currently OFF.%n", getId());
            return;
        }
        this.isRecording = true;
        System.out.printf("[%s] Started recording video feed in %s to cloud/local storage.%n", getId(), resolution);
    }

    public void stopRecording() {
        this.isRecording = false;
        System.out.printf("[%s] Video recording stopped.%n", getId());
    }

    public boolean isRecording() {
        return isRecording;
    }

    public boolean isNightVisionActive() {
        return nightVisionActive;
    }

    public String getResolution() {
        return resolution;
    }

    // ==========================================
    // OVERRIDES FROM ABSTRACT CLASS (SmartDevice)
    // ==========================================

    @Override
    public void operate() {
        if (!isOn()) {
            System.out.printf("[%s] Camera %s is offline.%n", getId(), getName());
            return;
        }
        System.out.printf("[%s] %s monitoring %s [%s, NightVision: %s, Battery: %d%%].%n",
                getId(), getName(), getRoom(), resolution, nightVisionActive ? "ON" : "OFF", batteryLevel);
    }

    @Override
    public double calculatePowerConsumption() {
        if (!isOn()) {
            return 0.8; // Standby power when OFF (Watts)
        }
        double power = getBasePowerWatt();
        if (isRecording) {
            power += 3.5; // Additional sensor & encoder power
        }
        if (nightVisionActive) {
            power += 2.0; // Infrared IR illuminator load
        }
        return power;
    }

    @Override
    public String getStatusSummary() {
        return String.format("Res: %s | Rec: %s | Bat: %d%%%s | WiFi: %s",
                resolution,
                isRecording ? "REC" : "IDLE",
                batteryLevel,
                isLowBattery() ? " [LOW!]" : "",
                wiFiConnected ? ipAddress : "Offline");
    }

    // ==========================================
    // IMPLEMENTATION OF INTERFACE (BatteryPowered)
    // ==========================================

    @Override
    public int getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    public boolean isLowBattery() {
        return batteryLevel <= 20;
    }

    @Override
    public void recharge(int amount) {
        Validator.requirePositive(amount, "Recharge Amount");
        this.batteryLevel = Math.min(100, this.batteryLevel + amount);
        System.out.printf("[%s] Camera battery recharged (+%d%%). Current battery: %d%%.%n",
                getId(), amount, this.batteryLevel);
    }

    // ==========================================
    // IMPLEMENTATION OF INTERFACE (WiFiConnectable)
    // ==========================================

    @Override
    public void connectWiFi(String ssid, String password) {
        Validator.requireNonEmpty(ssid, "Wi-Fi SSID");
        Validator.requireNonEmpty(password, "Wi-Fi Password");

        this.connectedSsid = ssid.trim();
        this.ipAddress = "192.168.1." + (150 + (Math.abs(getId().hashCode()) % 50));
        this.wiFiConnected = true;
        System.out.printf("[%s] Camera streaming online via Wi-Fi '%s' [IP: %s].%n", getId(), connectedSsid, ipAddress);
    }

    @Override
    public void disconnectWiFi() {
        this.wiFiConnected = false;
        this.connectedSsid = null;
        this.ipAddress = null;
        System.out.printf("[%s] Camera Wi-Fi disconnected.%n", getId());
    }

    @Override
    public boolean isWiFiConnected() {
        return wiFiConnected;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }
}
