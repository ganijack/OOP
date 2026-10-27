package smarthome.hub;

import smarthome.model.SmartDevice;
import smarthome.model.User;
import smarthome.validation.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ABSTRACT CLASS: SmartHub
 * Pusat kendali otomasi dan ekosistem rumah pintar.
 * Mengelola koleksi perangkat pintar (List<SmartDevice>) dan menyajikan
 * dashboard pemantauan daya serta kontrol terpusat.
 */
public abstract class SmartHub {

    private final String hubId;
    private final String hubName;
    private final User owner;
    private final List<SmartDevice> devices;

    public SmartHub(String hubId, String hubName, User owner) {
        Validator.requireNonEmpty(hubId, "Hub ID");
        Validator.requireNonEmpty(hubName, "Nama Hub");
        if (owner == null) {
            throw new IllegalArgumentException("Pemilik Hub (Owner) tidak boleh null.");
        }

        this.hubId = hubId.trim();
        this.hubName = hubName.trim();
        this.owner = owner;
        this.devices = new ArrayList<>();
    }

    // ==========================================
    // ABSTRACT METHODS (Diwariskan ke Subclass)
    // ==========================================

    /**
     * Memeriksa hak otorisasi pengguna untuk mengakses dan mengontrol perangkat pada Hub.
     * Aturan akses berbeda tergantung apakah Hub bersifat Lokal Terbuka atau Cloud Terenkripsi.
     *
     * @param user pengguna yang mencoba mengakses
     * @return true jika diizinkan, false jika ditolak
     */
    public abstract boolean canAccess(User user);

    /**
     * Mengembalikan identitas tipe dari Hub.
     *
     * @return String tipe hub
     */
    public abstract String getHubType();

    // ==========================================
    // CONCRETE METHODS (Polymorphism Demonstration)
    // ==========================================

    public void addDevice(SmartDevice device) {
        if (device == null) {
            throw new IllegalArgumentException("Perangkat tidak boleh null.");
        }
        devices.add(device);
        System.out.printf("[%s] Perangkat '%s' (%s) berhasil didaftarkan ke hub.%n",
                hubName, device.getName(), device.getId());
    }

    public List<SmartDevice> getDevices() {
        return Collections.unmodifiableList(devices);
    }

    /**
     * POLIMORFISME RUNTIME:
     * Menghitung total pemakaian daya seluruh perangkat yang terhubung.
     * Memanggil method calculatePowerConsumption() pada tipe abstrak SmartDevice,
     * namun hasil kalkulasi secara otomatis mengikuti perilaku masing-masing objek konkret
     * (SmartLamp, SmartAC, SmartCamera, SmartDoorbellCamera).
     */
    public double calculateTotalPowerConsumption() {
        double totalWatt = 0.0;
        for (SmartDevice device : devices) {
            totalWatt += device.calculatePowerConsumption();
        }
        return totalWatt;
    }

    /**
     * POLIMORFISME RUNTIME:
     * Mematikan seluruh perangkat dalam satu kali perintah terpusat (Energy Saving Routine).
     */
    public void turnOffAllDevices(User user) {
        System.out.printf("%n>>> Eksekusi Rutinitas 'Matikan Semua Perangkat' oleh %s...%n", user.getName());
        if (!canAccess(user)) {
            System.out.printf("[DITOLAK] Pengguna %s tidak memiliki wewenang mengontrol %s!%n",
                    user.getName(), hubName);
            return;
        }

        for (SmartDevice device : devices) {
            device.turnOff();
        }
        System.out.printf("[SUKSES] Seluruh perangkat di %s berhasil dipadamkan.%n", hubName);
    }

    /**
     * Menampilkan dashboard status seluruh perangkat di rumah pintar.
     */
    public void printDashboard() {
        System.out.println("\n" + "=".repeat(95));
        System.out.printf("SMART HOME DASHBOARD - [%s] (Tipe: %s)%n", hubName, getHubType());
        System.out.printf("Pemilik: %s | Total Perangkat: %d Unit%n", owner, devices.size());
        System.out.println("-".repeat(95));

        if (devices.isEmpty()) {
            System.out.println("   (Belum ada perangkat yang terpasang)");
        } else {
            for (SmartDevice device : devices) {
                // Memanggil method describe() yang secara polimorfis memanggil calculatePowerConsumption()
                System.out.println(" * " + device.describe());
            }
        }

        System.out.println("-".repeat(95));
        System.out.printf("TOTAL ESTIMASI DAYA KONSUMSI SAAT INI: %.2f WATT%n", calculateTotalPowerConsumption());
        System.out.println("=".repeat(95));
    }

    public String getHubId() {
        return hubId;
    }

    public String getHubName() {
        return hubName;
    }

    public User getOwner() {
        return owner;
    }
}
