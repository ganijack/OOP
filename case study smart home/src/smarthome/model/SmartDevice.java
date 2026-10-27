package smarthome.model;

import smarthome.validation.Validator;

/**
 * ABSTRACT CLASS: SmartDevice
 * Sebagai superclass (induk) dari seluruh perangkat pintar dalam sistem Smart Home.
 * Objek SmartDevice tidak dapat diinstansiasi secara langsung karena hanya berfungsi
 * sebagai cetak biru (blueprint) abstrak dari konsep umum "perangkat pintar".
 */
public abstract class SmartDevice {

    private final String id;
    private final String name;
    private final String room;
    private boolean isOn;
    private final double basePowerWatt;

    /**
     * Konstruktor SmartDevice untuk dipanggil oleh subclass via super(...).
     *
     * @param id            identifier unik perangkat
     * @param name          nama deskriptif perangkat
     * @param room          lokasi ruangan perangkat diletakkan
     * @param basePowerWatt daya listrik dasar dalam satuan Watt
     */
    public SmartDevice(String id, String name, String room, double basePowerWatt) {
        Validator.requireNonEmpty(id, "Device ID");
        Validator.requireNonEmpty(name, "Nama Perangkat");
        Validator.requireNonEmpty(room, "Lokasi Ruangan");
        Validator.requirePositive(basePowerWatt, "Base Power Watt");

        this.id = id.trim();
        this.name = name.trim();
        this.room = room.trim();
        this.basePowerWatt = basePowerWatt;
        this.isOn = false; // default awal perangkat mati
    }

    // ==========================================
    // ABSTRACT METHODS (Wajib di-override oleh Subclass)
    // ==========================================

    /**
     * Menjalankan fungsi operasional spesifik dari masing-masing jenis perangkat pintar.
     * Tiap turunan memiliki perilaku kerja yang unik.
     */
    public abstract void operate();

    /**
     * Menghitung konsumsi daya listrik riil saat ini (Watt/jam).
     * Subclass dapat memodifikasi rumus sesuai pengaturan (misal kecerahan lampu, suhu AC, dsb).
     *
     * @return daya listrik dalam satuan Watt
     */
    public abstract double calculatePowerConsumption();

    /**
     * Memberikan ringkasan status operasional spesifik dari perangkat.
     *
     * @return String ringkasan status
     */
    public abstract String getStatusSummary();

    // ==========================================
    // CONCRETE METHODS (Diwariskan langsung ke Subclass)
    // ==========================================

    public void turnOn() {
        if (!this.isOn) {
            this.isOn = true;
            System.out.printf("[%s] %s di %s berhasil DINYALAKAN.%n", id, name, room);
        } else {
            System.out.printf("[%s] %s sudah dalam keadaan AKTIF.%n", id, name);
        }
    }

    public void turnOff() {
        if (this.isOn) {
            this.isOn = false;
            System.out.printf("[%s] %s di %s berhasil DIMATIKAN.%n", id, name, room);
        } else {
            System.out.printf("[%s] %s memang sudah NONAKTIF.%n", id, name);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoom() {
        return room;
    }

    public boolean isOn() {
        return isOn;
    }

    public double getBasePowerWatt() {
        return basePowerWatt;
    }

    /**
     * Template method konkret untuk menampilkan baris informasi perangkat pada dashboard.
     */
    public final String describe() {
        String powerStr = String.format("%.1fW", calculatePowerConsumption());
        String statusStr = isOn ? "ON " : "OFF";
        return String.format("[%-7s] %-20s | Ruang: %-12s | Status: [%s] | Daya: %-8s | %s",
                id, name, room, statusStr, powerStr, getStatusSummary());
    }
}
