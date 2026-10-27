package smarthome.model;

import smarthome.contract.Schedulable;
import smarthome.validation.Validator;

/**
 * SUBCLASS: SmartLamp (Intelligent Lighting)
 * Extends SmartDevice and implements Schedulable.
 */
public class SmartLamp extends SmartDevice implements Schedulable {

    private int brightness; // 1 - 100%
    private String color;    // e.g., "Warm White", "Cool Daylight", "RGB Blue"
    private String scheduleStart;
    private String scheduleEnd;

    public SmartLamp(String id, String name, String room, double basePowerWatt, int initialBrightness, String initialColor) {
        super(id, name, room, basePowerWatt);
        setBrightness(initialBrightness);
        setColor(initialColor);
    }

    public void setBrightness(int brightness) {
        Validator.requireRange(brightness, 1, 100, "Lamp Brightness");
        this.brightness = brightness;
    }

    public void setColor(String color) {
        Validator.requireNonEmpty(color, "Lamp Color");
        this.color = color.trim();
    }

    public int getBrightness() {
        return brightness;
    }

    public String getColor() {
        return color;
    }

    // ==========================================
    // OVERRIDES FROM ABSTRACT CLASS (SmartDevice)
    // ==========================================

    @Override
    public void operate() {
        if (!isOn()) {
            System.out.printf("[%s] %s cannot operate because device is OFF.%n", getId(), getName());
            return;
        }
        System.out.printf("[%s] %s illuminating %s with %s at %d%% brightness.%n",
                getId(), getName(), getRoom(), color, brightness);
    }

    @Override
    public double calculatePowerConsumption() {
        if (!isOn()) {
            return 0.5; // Microcontroller standby power when OFF (Watts)
        }
        // Active power proportional to brightness setting
        return getBasePowerWatt() * (brightness / 100.0);
    }

    @Override
    public String getStatusSummary() {
        String base = String.format("Color: %s, Brightness: %d%%", color, brightness);
        if (isScheduleActive()) {
            base += " | " + getScheduleInfo();
        }
        return base;
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
        System.out.printf("[%s] Lamp schedule set: %s to %s.%n", getId(), startTime, endTime);
    }

    @Override
    public void clearSchedule() {
        this.scheduleStart = null;
        this.scheduleEnd = null;
        System.out.printf("[%s] Lamp schedule cleared.%n", getId());
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
