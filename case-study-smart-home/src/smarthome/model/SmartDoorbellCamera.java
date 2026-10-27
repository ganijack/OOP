package smarthome.model;

import smarthome.validation.Validator;

/**
 * SUBCLASS: SmartDoorbellCamera (Smart Video Doorbell)
 * Extends SmartCamera (which in turn extends SmartDevice).
 * Implements MULTILEVEL INHERITANCE:
 * SmartDevice -> SmartCamera -> SmartDoorbellCamera.
 *
 * Inherits and reuses all camera capabilities while adding specialized
 * porch security features such as PIR motion alerts and chime triggers.
 */
public class SmartDoorbellCamera extends SmartCamera {

    private int chimeVolume; // Chime loudness (level 1 - 10)
    private boolean motionDetectionEnabled;

    public SmartDoorbellCamera(String id, String name, String room, double basePowerWatt,
                               String resolution, int initialBattery, int chimeVolume) {
        super(id, name, room, basePowerWatt, resolution, initialBattery);
        setChimeVolume(chimeVolume);
        this.motionDetectionEnabled = true;
    }

    public void setChimeVolume(int volume) {
        Validator.requireRange(volume, 1, 10, "Doorbell Chime Volume");
        this.chimeVolume = volume;
    }

    public int getChimeVolume() {
        return chimeVolume;
    }

    public void toggleMotionDetection(boolean enable) {
        this.motionDetectionEnabled = enable;
        System.out.printf("[%s] PIR Motion Detection sensor %s.%n",
                getId(), enable ? "ENABLED" : "DISABLED");
    }

    /**
     * Specialized doorbell action: visitor presses the physical chime button.
     * Automatically triggers short video recording via inherited startRecording() method.
     */
    public void pressDoorbell() {
        System.out.printf("%n[DING-DONG!] %s chime PRESSED by a visitor! (Vol: %d/10)%n",
                getName(), chimeVolume);
        System.out.println(">> Sending instant push notification to homeowner's smartphone...");
        startRecording(); // Reuses inherited method from SmartCamera
    }

    /**
     * Motion sensor trigger.
     */
    public void triggerMotionAlert(String description) {
        if (!motionDetectionEnabled) {
            return;
        }
        System.out.printf("[ALERT MOTION at %s]: %s!%n", getRoom(), description);
    }

    // ==========================================
    // OVERRIDES WITH SUPERCLASS METHOD REUSE
    // ==========================================

    @Override
    public void operate() {
        super.operate(); // Runs base camera surveillance operations
        System.out.printf("   -> [Doorbell Feature] PIR Motion Sensor: %s | Chime Volume: %d/10.%n",
                motionDetectionEnabled ? "ACTIVE" : "OFF", chimeVolume);
    }

    @Override
    public double calculatePowerConsumption() {
        // Base camera power + continuous PIR motion sensor standby current
        double baseCameraPower = super.calculatePowerConsumption();
        return baseCameraPower + (motionDetectionEnabled ? 1.2 : 0.2);
    }

    @Override
    public String getStatusSummary() {
        return super.getStatusSummary() + String.format(" | Chime: Lv%d | PIR: %s",
                chimeVolume, motionDetectionEnabled ? "ON" : "OFF");
    }
}
