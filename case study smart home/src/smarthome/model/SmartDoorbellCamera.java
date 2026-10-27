package smarthome.model;

import smarthome.validation.Validator;

/**
 * SUBCLASS: SmartDoorbellCamera (Bel Pintu Kamera Pintar)
 * Mewarisi SmartCamera (yang mana SmartCamera mewarisi SmartDevice).
 * Menerapkan konsep INHERITANCE BERTINGKAT (MULTILEVEL INHERITANCE):
 * SmartDevice -> SmartCamera -> SmartDoorbellCamera.
 *
 * Menggunakan kembali method superclass (recharge, connectWiFi, dsb)
 * tanpa perlu mendefinisikan ulang kodenya, serta menambahkan fungsionalitas baru
 * seperti pendeteksi gerakan (PIR Sensor) dan tombol bel pintu.
 */
public class SmartDoorbellCamera extends SmartCamera {

    private int chimeVolume; // Volume bel pintu (level 1 - 10)
    private boolean motionDetectionEnabled;

    public SmartDoorbellCamera(String id, String name, String room, double basePowerWatt,
                               String resolution, int initialBattery, int chimeVolume) {
        super(id, name, room, basePowerWatt, resolution, initialBattery);
        setChimeVolume(chimeVolume);
        this.motionDetectionEnabled = true;
    }

    public void setChimeVolume(int volume) {
        Validator.requireRange(volume, 1, 10, "Volume Nada Dering Bel");
        this.chimeVolume = volume;
    }

    public int getChimeVolume() {
        return chimeVolume;
    }

    public void toggleMotionDetection(boolean enable) {
        this.motionDetectionEnabled = enable;
        System.out.printf("[%s] Sensor Deteksi Gerakan Tamu %s.%n",
                getId(), enable ? "DIAKTIFKAN" : "DINONAKTIFKAN");
    }

    /**
     * Fitur khusus bel pintu: Tamu menekan bel di depan pintu rumah.
     * Otomatis memicu perekaman video singkat dari method induk (startRecording).
     */
    public void pressDoorbell() {
        System.out.printf("%n[DING-DONG!] Tombol bel di %s (%s) DITEKAN oleh seseorang! (Vol: %d/10)%n",
                getRoom(), getName(), chimeVolume);
        System.out.println(">> Mengirim notifikasi push ke smartphone pemilik rumah...");
        startRecording(); // memanggil method warisan dari SmartCamera
    }

    /**
     * Fitur deteksi gerakan di depan pintu.
     */
    public void triggerMotionAlert(String description) {
        if (!motionDetectionEnabled) {
            return;
        }
        System.out.printf("[ALERT GERAKAN di %s]: %s terdeteksi di depan pintu!%n", getRoom(), description);
    }

    // ==========================================
    // OVERRIDE DARI SUPERCLASS DENGAN LOGIKA SUPER
    // ==========================================

    @Override
    public void operate() {
        super.operate(); // menjalankan tugas dasar kamera pemantau
        System.out.printf("   -> [Fitur Khusus Bel] PIR Sensor: %s | Volume Chime: %d/10.%n",
                motionDetectionEnabled ? "SIAGA" : "OFF", chimeVolume);
    }

    @Override
    public double calculatePowerConsumption() {
        // Konsumsi daya kamera biasa ditambah daya sensor gerak PIR yang selalu standby
        double baseCameraPower = super.calculatePowerConsumption();
        return baseCameraPower + (motionDetectionEnabled ? 1.2 : 0.2);
    }

    @Override
    public String getStatusSummary() {
        // Menggabungkan ringkasan kamera dari superclass dengan fitur bel
        return super.getStatusSummary() + String.format(" | Chime: Lv%d | PIR: %s",
                chimeVolume, motionDetectionEnabled ? "ON" : "OFF");
    }
}
