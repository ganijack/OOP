package smarthome.model;

import smarthome.contract.BatteryPowered;
import smarthome.contract.WiFiConnectable;
import smarthome.validation.Validator;

/**
 * SUBCLASS: SmartCamera (Kamera Pengawas Pintar)
 * Mewarisi SmartDevice dan mengimplementasikan WiFiConnectable serta BatteryPowered.
 * Merupakan superclass bagi SmartDoorbellCamera (membentuk Inheritance Bertingkat / Multilevel).
 */
public class SmartCamera extends SmartDevice implements WiFiConnectable, BatteryPowered {

    private final String resolution; // contoh: "1080p", "2K", "4K"
    private boolean nightVisionActive;
    private boolean isRecording;

    // Field BatteryPowered
    private int batteryLevel; // 0 - 100%

    // Field WiFiConnectable
    private boolean wiFiConnected;
    private String connectedSsid;
    private String ipAddress;

    public SmartCamera(String id, String name, String room, double basePowerWatt, String resolution, int initialBattery) {
        super(id, name, room, basePowerWatt);
        Validator.requireNonEmpty(resolution, "Resolusi Kamera");
        this.resolution = resolution.trim();
        this.batteryLevel = initialBattery;
        this.nightVisionActive = false;
        this.isRecording = false;
        this.wiFiConnected = false;
    }

    public void setNightVision(boolean active) {
        this.nightVisionActive = active;
        System.out.printf("[%s] Mode Night Vision %s.%n", getId(), active ? "DIAKTIFKAN" : "DINONAKTIFKAN");
    }

    public void startRecording() {
        if (!isOn()) {
            System.out.printf("[%s] Gagal merekam: Kamera masih dalam kondisi OFF.%n", getId());
            return;
        }
        this.isRecording = true;
        System.out.printf("[%s] Mulai merekam video beresolusi %s ke penyimpanan lokal/cloud.%n", getId(), resolution);
    }

    public void stopRecording() {
        this.isRecording = false;
        System.out.printf("[%s] Perekaman video dihentikan.%n", getId());
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
    // OVERRIDE DARI ABSTRACT CLASS (SmartDevice)
    // ==========================================

    @Override
    public void operate() {
        if (!isOn()) {
            System.out.printf("[%s] Kamera %s tidak aktif.%n", getId(), getName());
            return;
        }
        System.out.printf("[%s] Kamera %s memantau area %s [%s, NightVision: %s, Baterai: %d%%].%n",
                getId(), getName(), getRoom(), resolution, nightVisionActive ? "ON" : "OFF", batteryLevel);
    }

    @Override
    public double calculatePowerConsumption() {
        if (!isOn()) {
            return 0.8; // Konsumsi standby saat OFF
        }
        double power = getBasePowerWatt();
        if (isRecording) {
            power += 3.5; // Tambahan daya saat sensor rekaman bekerja
        }
        if (nightVisionActive) {
            power += 2.0; // Tambahan daya lampu inframerah IR
        }
        return power;
    }

    @Override
    public String getStatusSummary() {
        return String.format("Res: %s | Rekam: %s | Bat: %d%%%s | WiFi: %s",
                resolution,
                isRecording ? "REC" : "IDLE",
                batteryLevel,
                isLowBattery() ? " [LOW!]" : "",
                wiFiConnected ? ipAddress : "Offline");
    }

    // ==========================================
    // IMPLEMENTASI INTERFACE (BatteryPowered)
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
        Validator.requirePositive(amount, "Jumlah Persentase Isi Ulang");
        this.batteryLevel = Math.min(100, this.batteryLevel + amount);
        System.out.printf("[%s] Baterai kamera diisi ulang (+%d%%). Sisa baterai sekarang: %d%%.%n",
                getId(), amount, this.batteryLevel);
    }

    // ==========================================
    // IMPLEMENTASI INTERFACE (WiFiConnectable)
    // ==========================================

    @Override
    public void connectWiFi(String ssid, String password) {
        Validator.requireNonEmpty(ssid, "SSID Wi-Fi");
        Validator.requireNonEmpty(password, "Password Wi-Fi");

        this.connectedSsid = ssid.trim();
        this.ipAddress = "192.168.1." + (150 + (Math.abs(getId().hashCode()) % 50));
        this.wiFiConnected = true;
        System.out.printf("[%s] Kamera streaming online via Wi-Fi '%s' [IP: %s].%n", getId(), connectedSsid, ipAddress);
    }

    @Override
    public void disconnectWiFi() {
        this.wiFiConnected = false;
        this.connectedSsid = null;
        this.ipAddress = null;
        System.out.printf("[%s] Kamera diputus dari jaringan Wi-Fi.%n", getId());
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
