package smarthome.model;

import smarthome.contract.Schedulable;
import smarthome.validation.Validator;

/**
 * SUBCLASS: SmartLamp (Lampu Pintar)
 * Mewarisi SmartDevice dan mengimplementasikan Schedulable.
 */
public class SmartLamp extends SmartDevice implements Schedulable {

    private int brightness; // 1 - 100%
    private String color;    // misal "Warm White", "Cool Daylight", "RGB Blue"
    private String scheduleStart;
    private String scheduleEnd;

    public SmartLamp(String id, String name, String room, double basePowerWatt, int initialBrightness, String initialColor) {
        super(id, name, room, basePowerWatt);
        setBrightness(initialBrightness);
        setColor(initialColor);
    }

    public void setBrightness(int brightness) {
        Validator.requireRange(brightness, 1, 100, "Tingkat Kecerahan Lampu");
        this.brightness = brightness;
    }

    public void setColor(String color) {
        Validator.requireNonEmpty(color, "Warna Lampu");
        this.color = color.trim();
    }

    public int getBrightness() {
        return brightness;
    }

    public String getColor() {
        return color;
    }

    // ==========================================
    // OVERRIDE DARI ABSTRACT CLASS (SmartDevice)
    // ==========================================

    @Override
    public void operate() {
        if (!isOn()) {
            System.out.printf("[%s] %s tidak dapat beroperasi karena perangkat sedang MATI.%n", getId(), getName());
            return;
        }
        System.out.printf("[%s] %s memancarkan cahaya %s dengan kecerahan %d%% ke seluruh %s.%n",
                getId(), getName(), color, brightness, getRoom());
    }

    @Override
    public double calculatePowerConsumption() {
        if (!isOn()) {
            return 0.5; // Konsumsi daya standby modul elektronik saat OFF (Watt)
        }
        // Konsumsi daya aktif proporsional dengan tingkat kecerahan
        return getBasePowerWatt() * (brightness / 100.0);
    }

    @Override
    public String getStatusSummary() {
        String base = String.format("Warna: %s, Kecerahan: %d%%", color, brightness);
        if (isScheduleActive()) {
            base += " | " + getScheduleInfo();
        }
        return base;
    }

    // ==========================================
    // IMPLEMENTASI DARI INTERFACE (Schedulable)
    // ==========================================

    @Override
    public void setSchedule(String startTime, String endTime) {
        Validator.requireNonEmpty(startTime, "Waktu Mulai Jadwal");
        Validator.requireNonEmpty(endTime, "Waktu Selesai Jadwal");
        this.scheduleStart = startTime;
        this.scheduleEnd = endTime;
        System.out.printf("[%s] Jadwal lampu berhasil disetel: %s s/d %s.%n", getId(), startTime, endTime);
    }

    @Override
    public void clearSchedule() {
        this.scheduleStart = null;
        this.scheduleEnd = null;
        System.out.printf("[%s] Jadwal lampu dibatalkan.%n", getId());
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
