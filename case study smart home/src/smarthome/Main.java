package smarthome;

import smarthome.contract.BatteryPowered;
import smarthome.contract.Schedulable;
import smarthome.contract.WiFiConnectable;
import smarthome.exception.ValidationException;
import smarthome.hub.SecureCloudHub;
import smarthome.hub.SmartHub;
import smarthome.hub.StandardHomeHub;
import smarthome.model.*;

import java.util.List;

/**
 * Program Utama (Demo Driver) Sistem Manajemen Smart Home.
 *
 * Nama : THORIQ ABDILLAH FALIAN KUSUMA
 * NIM  : F1D02410098
 * Kelas: 5A
 * Mata Kuliah: Pemrograman Berorientasi Objek
 */
public class Main {

    public static void main(String[] args) {
        printBanner();

        // =========================================================================
        // 1. SETUP PENGGUNA (USERS)
        // =========================================================================
        System.out.println(">>> 1. INISIALISASI PENGGUNA SISTEM");
        User thoriq = new User("USR-01", "Thoriq Abdillah", "ADMIN");
        User falian = new User("USR-02", "Falian", "MEMBER");
        User tamu = new User("USR-03", "Budi Santoso", "GUEST");

        System.out.println("  * " + thoriq);
        System.out.println("  * " + falian);
        System.out.println("  * " + tamu);
        System.out.println();

        // =========================================================================
        // 2. PEMBUATAN OBJEK PERANGKAT (INHERITANCE & MULTILEVEL INHERITANCE)
        // =========================================================================
        System.out.println(">>> 2. REGISTRASI & SETUP PERANGKAT SMART HOME");

        // Single Inheritance: SmartLamp extends SmartDevice
        SmartLamp livingRoomLamp = new SmartLamp(
                "LMP-01", "Lampu Ruang Tamu", "Ruang Tamu", 12.0, 80, "Warm White"
        );

        // Single Inheritance: SmartAC extends SmartDevice (implements 2 interfaces)
        SmartAC bedroomAC = new SmartAC(
                "AC-01", "AC Kamar Utama", "Kamar Tidur", 450.0, 22, 3
        );

        // Single Inheritance: SmartCamera extends SmartDevice
        SmartCamera backyardCamera = new SmartCamera(
                "CAM-01", "Kamera Halaman Belakang", "Halaman", 5.0, "2K QHD", 18
        );

        // Multilevel Inheritance: SmartDoorbellCamera extends SmartCamera extends SmartDevice
        SmartDoorbellCamera frontDoorbell = new SmartDoorbellCamera(
                "DBL-01", "Bel Pintu Utama", "Pintu Depan", 4.0, "1080p Full HD", 85, 8
        );

        System.out.println("  Semua perangkat berhasil diinstansiasi dengan benar.\n");

        // =========================================================================
        // 3. PENGATURAN KONEKSI, JADWAL, DAN DAYA BATERAI (INTERFACE CAPABILITIES)
        // =========================================================================
        System.out.println(">>> 3. MENGHUBUNGKAN PERANGKAT KE JARINGAN WI-FI & JADWAL OTOMATIS");
        bedroomAC.connectWiFi("Thoriq_Home_5G", "paspas123");
        backyardCamera.connectWiFi("Thoriq_Home_5G", "paspas123");
        frontDoorbell.connectWiFi("Thoriq_Home_5G", "paspas123");

        livingRoomLamp.setSchedule("18:00", "06:00");
        bedroomAC.setSchedule("21:30", "05:00");
        System.out.println();

        // =========================================================================
        // 4. MENGAKTIFKAN & MENGOPERASIKAN PERANGKAT
        // =========================================================================
        System.out.println(">>> 4. MENGAKTIFKAN & MENJALANKAN FUNGSI OPERASIONAL PERANGKAT");
        livingRoomLamp.turnOn();
        livingRoomLamp.operate();

        bedroomAC.turnOn();
        bedroomAC.operate();

        backyardCamera.turnOn();
        backyardCamera.setNightVision(true);
        backyardCamera.startRecording();
        backyardCamera.operate();

        frontDoorbell.turnOn();
        frontDoorbell.operate();

        // Simulasi interaksi tamu membunyikan bel pintu
        frontDoorbell.triggerMotionAlert("Seseorang kurir paket");
        frontDoorbell.pressDoorbell();
        System.out.println();

        // =========================================================================
        // 5. DEMONSTRASI HIERARKI KEDUA: SMARTHUB & KONTROL AKSES
        // =========================================================================
        System.out.println(">>> 5. DEMO HIERARKI DUA: SMARTHUB (ABSTRACT CLASS & ACCESS CONTROL)");
        SmartHub localHub = new StandardHomeHub("HUB-LOCAL-01", "Home Local Hub", thoriq);
        SmartHub cloudHub = new SecureCloudHub("HUB-CLOUD-01", "AWS Smart Cloud Hub", thoriq, "ap-southeast-3 (Jakarta)");

        // Mendaftarkan perangkat ke Hub
        localHub.addDevice(livingRoomLamp);
        localHub.addDevice(bedroomAC);
        localHub.addDevice(backyardCamera);
        localHub.addDevice(frontDoorbell);

        cloudHub.addDevice(bedroomAC);
        cloudHub.addDevice(frontDoorbell);

        // Dashboard Status Rumah Pintar (Menghitung Total Konsumsi Daya Secara Polimorfis)
        localHub.printDashboard();

        // Pengujian Akses Polimorfis pada Hub
        System.out.println("\n>>> UJI AKSES PENGGUNA TERHADAP KEDUA HUB (POLYMORPHISM):");
        List<SmartHub> hubs = List.of(localHub, cloudHub);
        List<User> users = List.of(thoriq, falian, tamu);

        for (SmartHub hub : hubs) {
            System.out.printf("Pemeriksaan izin pada [%s]:%n", hub.getHubName());
            for (User u : users) {
                boolean allowed = hub.canAccess(u);
                System.out.printf("   - User '%-16s' -> Akses: %s%n",
                        u.getName(), allowed ? "DIIZINKAN (Granted)" : "DITOLAK (Denied)");
            }
        }
        System.out.println();

        // =========================================================================
        // 6. DEMONSTRASI POLIMORFISME INTERFACE
        // =========================================================================
        System.out.println(">>> 6. DEMONSTRASI POLIMORFISME BERBASIS INTERFACE");

        // 6.A Polimorfisme Interface: BatteryPowered
        System.out.println("[A] Monitoring Perangkat Berbaterai (List<BatteryPowered>):");
        List<BatteryPowered> batteryDevices = List.of(backyardCamera, frontDoorbell);

        for (BatteryPowered dev : batteryDevices) {
            // Karena tipe variabelnya adalah interface BatteryPowered, kita memanggil method kontraknya
            SmartDevice sd = (SmartDevice) dev;
            System.out.printf("   * %-25s | Sisa Baterai: %3d%% | Status Kritis: %s%n",
                    sd.getName(), dev.getBatteryLevel(), dev.isLowBattery() ? "PERLU DICAS!" : "Aman");

            if (dev.isLowBattery()) {
                System.out.printf("     --> Mengisi daya otomatis untuk %s...%n", sd.getName());
                dev.recharge(50);
            }
        }
        System.out.println();

        // 6.B Polimorfisme Interface: Schedulable
        System.out.println("[B] Manajemen Jadwal Otomatis (List<Schedulable>):");
        List<Schedulable> scheduledDevices = List.of(livingRoomLamp, bedroomAC);
        for (Schedulable sched : scheduledDevices) {
            SmartDevice sd = (SmartDevice) sched;
            System.out.printf("   * %-20s -> %s%n", sd.getName(), sched.getScheduleInfo());
        }
        System.out.println();

        // =========================================================================
        // 7. PENGUJIAN ENKAPSULASI DAN VALIDASI INPUT (EXCEPTION HANDLING)
        // =========================================================================
        System.out.println(">>> 7. PENGUJIAN ATURAN VALIDASI DATA (ENCAPSULATION)");
        System.out.println("Mencoba melakukan input di luar batas normal untuk menguji proteksi data:");

        // Test 1: Set suhu AC melebihi batas (misal 35C, batas: 16-30C)
        try {
            System.out.print("  [Test 1] Menyetel suhu AC ke 35 C: ");
            bedroomAC.setTargetTemperature(35);
        } catch (ValidationException e) {
            System.out.println("GAGAL (Sesuai Harapan) -> " + e.getMessage());
        }

        // Test 2: Set kecerahan lampu 150% (batas: 1-100%)
        try {
            System.out.print("  [Test 2] Menyetel kecerahan lampu ke 150%: ");
            livingRoomLamp.setBrightness(150);
        } catch (ValidationException e) {
            System.out.println("GAGAL (Sesuai Harapan) -> " + e.getMessage());
        }

        // Test 3: SSID WiFi kosong
        try {
            System.out.print("  [Test 3] Menghubungkan WiFi dengan SSID kosong: ");
            backyardCamera.connectWiFi("   ", "password123");
        } catch (ValidationException e) {
            System.out.println("GAGAL (Sesuai Harapan) -> " + e.getMessage());
        }
        System.out.println();

        // =========================================================================
        // 8. EKSEKUSI RUTINITAS PEMADAMAN SENTRAL (ENERGY SAVER)
        // =========================================================================
        System.out.println(">>> 8. POLIMORFISME RUTINITAS: SHUTDOWN SELURUH PERANGKAT OLEH ADMIN");
        localHub.turnOffAllDevices(thoriq);

        // Cetak dashboard akhir setelah semua perangkat mati (memverifikasi konsumsi daya standby)
        localHub.printDashboard();

        System.out.println("\n[SUKSES] Seluruh pengujian konsep OOP Smart Home telah berhasil dijalankan!");
    }

    private static void printBanner() {
        System.out.println("===============================================================================================");
        System.out.println("                   STUDI KASUS SISTEM MANAJEMEN SMART HOME IOT                                ");
        System.out.println("             Penerapan Abstract Class, Interface, Inheritance, & Polymorphism                 ");
        System.out.println("===============================================================================================");
        System.out.println(" Nama   : THORIQ ABDILLAH FALIAN KUSUMA");
        System.out.println(" NIM    : F1D02410098");
        System.out.println(" Kelas  : 5A");
        System.out.println(" Matkul : Pemrograman Berorientasi Objek (PBO)");
        System.out.println("===============================================================================================\n");
    }
}
