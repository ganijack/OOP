package smarthome.model;

import smarthome.contract.Schedulable;
import smarthome.contract.WiFiConnectable;
import smarthome.validation.Validator;

/**
 * SUBCLASS: SmartAC (Pendingin Ruangan Pintar)
 * Mewarisi SmartDevice dan mengimplementasikan dua interface: Schedulable dan WiFiConnectable.
 * Membuktikan bahwa class di Java dapat mengimplementasikan banyak interface sekaligus.
 */
public class SmartAC extends SmartDevice implements Schedulable, WiFiConnectable {

    private int targetTemperature; // 16 - 30 °C
    private int fanSpeed;           // level 1 - 5

    // Field untuk WiFiConnectable
    private boolean wiFiConnected;
    private String connectedSsid;
    private String ipAddress;

    // Field untuk Schedulable
    private String scheduleStart;
    private String scheduleEnd;

    public SmartAC(String id, String name, String room, double basePowerWatt, int initialTemp, int initialFanSpeed) {
        super(id, name, room, basePowerWatt);
        setTargetTemperature(initialTemp);
        setFanSpeed(initialFanSpeed);
        this.wiFiConnected = false;
    }

    public void setTargetTemperature(int temperature) {
        Validator.requireRange(temperature, 16, 30, "Suhu Pendingin Ruangan (C)");
        this.targetTemperature = temperature;
    }

    public void setFanSpeed(int fanSpeed) {
        Validator.requireRange(fanSpeed, 1, 5, "Kecepatan Kipas AC (Level 1-5)");
        this.fanSpeed = fanSpeed;
    }

    public int getTargetTemperature() {
        return targetTemperature;
    }

    public int getFanSpeed() {
        return fanSpeed;
    }

    // ==========================================
    // OVERRIDE DARI ABSTRACT CLASS (SmartDevice)
    // ==========================================

    @Override
    public void operate() {
        if (!isOn()) {
            System.out.printf("[%s] AC %s tidak aktif. Tekan tombol ON terlebih dahulu.%n", getId(), getName());
            return;
        }
        System.out.printf("[%s] AC %s sedang menghembuskan udara sejuk di %s | Target: %d C | Fan: Level %d.%n",
                getId(), getName(), getRoom(), targetTemperature, fanSpeed);
    }

    @Override
    public double calculatePowerConsumption() {
        if (!isOn()) {
            return 2.5; // Daya standby kompresor dan sensor IoT saat OFF (Watt)
        }
        // Daya meningkat semakin dingin suhu yang disetel dan semakin kencang kipasnya
        double coolingLoad = (30 - targetTemperature) * 35.0;
        double fanLoad = fanSpeed * 15.0;
        return getBasePowerWatt() + coolingLoad + fanLoad;
    }

    @Override
    public String getStatusSummary() {
        String base = String.format("Suhu: %d C, Fan: Lv%d | WiFi: %s",
                targetTemperature, fanSpeed, wiFiConnected ? ipAddress : "Terputus");
        if (isScheduleActive()) {
            base += " | " + getScheduleInfo();
        }
        return base;
    }

    // ==========================================
    // IMPLEMENTASI DARI INTERFACE (WiFiConnectable)
    // ==========================================

    @Override
    public void connectWiFi(String ssid, String password) {
        Validator.requireNonEmpty(ssid, "SSID Wi-Fi");
        Validator.requireNonEmpty(password, "Password Wi-Fi");

        this.connectedSsid = ssid.trim();
        this.ipAddress = "192.168.1." + (100 + (Math.abs(getId().hashCode()) % 50));
        this.wiFiConnected = true;
        System.out.printf("[%s] AC terhubung ke Wi-Fi '%s' dengan IP: %s.%n", getId(), connectedSsid, ipAddress);
    }

    @Override
    public void disconnectWiFi() {
        this.wiFiConnected = false;
        this.connectedSsid = null;
        this.ipAddress = null;
        System.out.printf("[%s] Koneksi Wi-Fi AC telah diputus.%n", getId());
    }

    @Override
    public boolean isWiFiConnected() {
        return wiFiConnected;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    // ==========================================
    // IMPLEMENTASI DARI INTERFACE (Schedulable)
    // ==========================================

    @Override
    public void setSchedule(String startTime, String endTime) {
        Validator.requireNonEmpty(startTime, "Waktu Mulai Jadwal AC");
        Validator.requireNonEmpty(endTime, "Waktu Selesai Jadwal AC");
        this.scheduleStart = startTime;
        this.scheduleEnd = endTime;
        System.out.printf("[%s] Jadwal AC aktif disetel: %s s/d %s.%n", getId(), startTime, endTime);
    }

    @Override
    public void clearSchedule() {
        this.scheduleStart = null;
        this.scheduleEnd = null;
        System.out.printf("[%s] Jadwal AC dibatalkan.%n", getId());
    }

    @Override
    public boolean isScheduleActive() {
        return scheduleStart != null && scheduleEnd != null;
    }

    @Override
    public String getScheduleInfo() {
        if (isScheduleActive()) {
            return String.format("Jadwal [%s - %s]", scheduleStart, scheduleEnd);
        }
        return "Tidak ada jadwal";
    }
}
