package smarthome.model;

import smarthome.contract.Schedulable;
import smarthome.contract.WiFiConnectable;
import smarthome.validation.Validator;

/**
 * SUBCLASS: SmartAC (Smart Air Conditioner)
 * Extends SmartDevice and implements multiple interfaces: Schedulable and WiFiConnectable.
 */
public class SmartAC extends SmartDevice implements Schedulable, WiFiConnectable {

    private int targetTemperature; // 16 - 30 C
    private int fanSpeed;           // level 1 - 5

    // Fields for WiFiConnectable
    private boolean wiFiConnected;
    private String connectedSsid;
    private String ipAddress;

    // Fields for Schedulable
    private String scheduleStart;
    private String scheduleEnd;

    public SmartAC(String id, String name, String room, double basePowerWatt, int initialTemp, int initialFanSpeed) {
        super(id, name, room, basePowerWatt);
        setTargetTemperature(initialTemp);
        setFanSpeed(initialFanSpeed);
        this.wiFiConnected = false;
    }

    public void setTargetTemperature(int temperature) {
        Validator.requireRange(temperature, 16, 30, "AC Target Temperature (C)");
        this.targetTemperature = temperature;
    }

    public void setFanSpeed(int fanSpeed) {
        Validator.requireRange(fanSpeed, 1, 5, "AC Fan Speed (Level 1-5)");
        this.fanSpeed = fanSpeed;
    }

    public int getTargetTemperature() {
        return targetTemperature;
    }

    public int getFanSpeed() {
        return fanSpeed;
    }

    // ==========================================
    // OVERRIDES FROM ABSTRACT CLASS (SmartDevice)
    // ==========================================

    @Override
    public void operate() {
        if (!isOn()) {
            System.out.printf("[%s] AC %s is inactive. Turn ON first.%n", getId(), getName());
            return;
        }
        System.out.printf("[%s] %s actively cooling %s | Target: %d C | Fan: Level %d.%n",
                getId(), getName(), getRoom(), targetTemperature, fanSpeed);
    }

    @Override
    public double calculatePowerConsumption() {
        if (!isOn()) {
            return 2.5; // Compressor sensor standby power when OFF (Watts)
        }
        // Power increases with lower target temperature and higher fan speeds
        double coolingLoad = (30 - targetTemperature) * 35.0;
        double fanLoad = fanSpeed * 15.0;
        return getBasePowerWatt() + coolingLoad + fanLoad;
    }

    @Override
    public String getStatusSummary() {
        String base = String.format("Temp: %d C, Fan: Lv%d | WiFi: %s",
                targetTemperature, fanSpeed, wiFiConnected ? ipAddress : "Offline");
        if (isScheduleActive()) {
            base += " | " + getScheduleInfo();
        }
        return base;
    }

    // ==========================================
    // IMPLEMENTATION OF INTERFACE (WiFiConnectable)
    // ==========================================

    @Override
    public void connectWiFi(String ssid, String password) {
        Validator.requireNonEmpty(ssid, "Wi-Fi SSID");
        Validator.requireNonEmpty(password, "Wi-Fi Password");

        this.connectedSsid = ssid.trim();
        this.ipAddress = "192.168.1." + (100 + (Math.abs(getId().hashCode()) % 50));
        this.wiFiConnected = true;
        System.out.printf("[%s] AC connected to Wi-Fi '%s' [IP: %s].%n", getId(), connectedSsid, ipAddress);
    }

    @Override
    public void disconnectWiFi() {
        this.wiFiConnected = false;
        this.connectedSsid = null;
        this.ipAddress = null;
        System.out.printf("[%s] AC Wi-Fi disconnected.%n", getId());
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
    // IMPLEMENTATION OF INTERFACE (Schedulable)
    // ==========================================

    @Override
    public void setSchedule(String startTime, String endTime) {
        Validator.requireNonEmpty(startTime, "Schedule Start Time");
        Validator.requireNonEmpty(endTime, "Schedule End Time");
        this.scheduleStart = startTime;
        this.scheduleEnd = endTime;
        System.out.printf("[%s] AC schedule set: %s to %s.%n", getId(), startTime, endTime);
    }

    @Override
    public void clearSchedule() {
        this.scheduleStart = null;
        this.scheduleEnd = null;
        System.out.printf("[%s] AC schedule cleared.%n", getId());
    }

    @Override
    public boolean isScheduleActive() {
        return scheduleStart != null && scheduleEnd != null;
    }

    @Override
    public String getScheduleInfo() {
        if (isScheduleActive()) {
            return String.format("Schedule [%s - %s]", scheduleStart, scheduleEnd);
        }
        return "No schedule";
    }
}
