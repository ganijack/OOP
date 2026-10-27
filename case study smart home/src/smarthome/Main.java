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
 * Main Demo Driver for the Smart Home Management System.
 *
 * Student Name : THORIQ ABDILLAH FALIAN KUSUMA
 * Student ID   : F1D02410098
 * Class        : 5A (International Class)
 * Course       : Object-Oriented Programming (OOP)
 */
public class Main {

    public static void main(String[] args) {
        printBanner();

        // =========================================================================
        // 1. SYSTEM USERS INITIALIZATION
        // =========================================================================
        System.out.println(">>> 1. INITIALIZING SYSTEM USERS");
        User thoriq = new User("USR-01", "Thoriq Abdillah", "ADMIN");
        User falian = new User("USR-02", "Falian", "MEMBER");
        User tamu = new User("USR-03", "Budi Santoso", "GUEST");

        System.out.println("  * " + thoriq);
        System.out.println("  * " + falian);
        System.out.println("  * " + tamu);
        System.out.println();

        // =========================================================================
        // 2. DEVICE INSTANTIATION (INHERITANCE & MULTILEVEL INHERITANCE)
        // =========================================================================
        System.out.println(">>> 2. REGISTERING & INITIALIZING SMART DEVICES");

        // Single Inheritance: SmartLamp extends SmartDevice
        SmartLamp livingRoomLamp = new SmartLamp(
                "LMP-01", "Living Room Lamp", "Living Room", 12.0, 80, "Warm White"
        );

        // Single Inheritance: SmartAC extends SmartDevice (implements 2 interfaces)
        SmartAC bedroomAC = new SmartAC(
                "AC-01", "Master Bedroom AC", "Bedroom", 450.0, 22, 3
        );

        // Single Inheritance: SmartCamera extends SmartDevice
        SmartCamera backyardCamera = new SmartCamera(
                "CAM-01", "Backyard Security Camera", "Backyard", 5.0, "2K QHD", 18
        );

        // Multilevel Inheritance: SmartDoorbellCamera extends SmartCamera extends SmartDevice
        SmartDoorbellCamera frontDoorbell = new SmartDoorbellCamera(
                "DBL-01", "Front Doorbell Camera", "Front Door", 4.0, "1080p Full HD", 85, 8
        );

        System.out.println("  All devices successfully instantiated with valid parameters.\n");

        // =========================================================================
        // 3. WI-FI CONNECTIVITY, SCHEDULING, & BATTERY SETUP (INTERFACE CAPABILITIES)
        // =========================================================================
        System.out.println(">>> 3. CONNECTING TO WI-FI NETWORK & CONFIGURING AUTOMATION SCHEDULES");
        bedroomAC.connectWiFi("Thoriq_Home_5G", "paspas123");
        backyardCamera.connectWiFi("Thoriq_Home_5G", "paspas123");
        frontDoorbell.connectWiFi("Thoriq_Home_5G", "paspas123");

        livingRoomLamp.setSchedule("18:00", "06:00");
        bedroomAC.setSchedule("21:30", "05:00");
        System.out.println();

        // =========================================================================
        // 4. POWERING ON & EXECUTING DEVICE OPERATIONS
        // =========================================================================
        System.out.println(">>> 4. POWERING ON & EXECUTING DEVICE OPERATIONS");
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

        // Simulate visitor interaction pressing the doorbell chime
        frontDoorbell.triggerMotionAlert("Delivery courier detected near the porch");
        frontDoorbell.pressDoorbell();
        System.out.println();

        // =========================================================================
        // 5. SECOND HIERARCHY DEMONSTRATION: SMARTHUB & ACCESS CONTROL
        // =========================================================================
        System.out.println(">>> 5. SECOND HIERARCHY DEMONSTRATION: SMARTHUB & ACCESS CONTROL");
        SmartHub localHub = new StandardHomeHub("HUB-LOCAL-01", "Home Local Hub", thoriq);
        SmartHub cloudHub = new SecureCloudHub("HUB-CLOUD-01", "AWS Smart Cloud Hub", thoriq, "ap-southeast-3 (Jakarta)");

        // Register devices to hubs
        localHub.addDevice(livingRoomLamp);
        localHub.addDevice(bedroomAC);
        localHub.addDevice(backyardCamera);
        localHub.addDevice(frontDoorbell);

        cloudHub.addDevice(bedroomAC);
        cloudHub.addDevice(frontDoorbell);

        // Print initial dashboard with polymorphic power calculation
        localHub.printDashboard();

        // Polymorphic access authorization checks across both hubs
        System.out.println("\n>>> ACCESS CONTROL VERIFICATION (POLYMORPHISM):");
        List<SmartHub> hubs = List.of(localHub, cloudHub);
        List<User> users = List.of(thoriq, falian, tamu);

        for (SmartHub hub : hubs) {
            System.out.printf("Access checks on [%s]:%n", hub.getHubName());
            for (User u : users) {
                boolean allowed = hub.canAccess(u);
                System.out.printf("   - User '%-16s' -> Access: %s%n",
                        u.getName(), allowed ? "GRANTED" : "DENIED");
            }
        }
        System.out.println();

        // =========================================================================
        // 6. INTERFACE-BASED POLYMORPHISM DEMONSTRATION
        // =========================================================================
        System.out.println(">>> 6. INTERFACE-BASED POLYMORPHISM DEMONSTRATION");

        // 6.A BatteryPowered interface polymorphism
        System.out.println("[A] Battery-Powered Device Management (List<BatteryPowered>):");
        List<BatteryPowered> batteryDevices = List.of(backyardCamera, frontDoorbell);

        for (BatteryPowered dev : batteryDevices) {
            SmartDevice sd = (SmartDevice) dev;
            System.out.printf("   * %-25s | Battery: %3d%% | Low Warning: %s%n",
                    sd.getName(), dev.getBatteryLevel(), dev.isLowBattery() ? "CRITICAL (CHARGE NEEDED!)" : "Normal");

            if (dev.isLowBattery()) {
                System.out.printf("     --> Triggering automatic recharge for %s...%n", sd.getName());
                dev.recharge(50);
            }
        }
        System.out.println();

        // 6.B Schedulable interface polymorphism
        System.out.println("[B] Automated Schedule Management (List<Schedulable>):");
        List<Schedulable> scheduledDevices = List.of(livingRoomLamp, bedroomAC);
        for (Schedulable sched : scheduledDevices) {
            SmartDevice sd = (SmartDevice) sched;
            System.out.printf("   * %-20s -> %s%n", sd.getName(), sched.getScheduleInfo());
        }
        System.out.println();

        // =========================================================================
        // 7. ENCAPSULATION & DATA VALIDATION TESTING (EXCEPTION HANDLING)
        // =========================================================================
        System.out.println(">>> 7. ENCAPSULATION & DATA VALIDATION TESTING (EXCEPTION HANDLING)");
        System.out.println("Attempting out-of-bounds parameter inputs to confirm data integrity safeguards:");

        // Test 1: Temperature out of bounds (35 C, valid: 16-30 C)
        try {
            System.out.print("  [Test 1] Setting AC temperature to 35 C: ");
            bedroomAC.setTargetTemperature(35);
        } catch (ValidationException e) {
            System.out.println("REJECTED (Expected) -> " + e.getMessage());
        }

        // Test 2: Brightness out of bounds (150%, valid: 1-100%)
        try {
            System.out.print("  [Test 2] Setting Lamp brightness to 150%: ");
            livingRoomLamp.setBrightness(150);
        } catch (ValidationException e) {
            System.out.println("REJECTED (Expected) -> " + e.getMessage());
        }

        // Test 3: Blank Wi-Fi SSID
        try {
            System.out.print("  [Test 3] Connecting Wi-Fi with blank SSID: ");
            backyardCamera.connectWiFi("   ", "password123");
        } catch (ValidationException e) {
            System.out.println("REJECTED (Expected) -> " + e.getMessage());
        }
        System.out.println();

        // =========================================================================
        // 8. CENTRALIZED SHUTDOWN ROUTINE (POLYMORPHISM)
        // =========================================================================
        System.out.println(">>> 8. POLYMORPHIC ROUTINE: CENTRALIZED SHUTDOWN BY ADMIN");
        localHub.turnOffAllDevices(thoriq);

        // Print final dashboard after shutdown
        localHub.printDashboard();

        System.out.println("\n[SUCCESS] All OOP concepts for the Smart Home Case Study have been validated successfully!");
    }

    private static void printBanner() {
        System.out.println("===============================================================================================");
        System.out.println("                   SMART HOME IOT DEVICE MANAGEMENT SYSTEM                                     ");
        System.out.println("             Demonstrating Abstract Class, Interface, Inheritance, & Polymorphism              ");
        System.out.println("===============================================================================================");
        System.out.println(" Name   : THORIQ ABDILLAH FALIAN KUSUMA");
        System.out.println(" NIM    : F1D02410098");
        System.out.println(" Class  : 5A (International Class)");
        System.out.println(" Course : Object-Oriented Programming (OOP)");
        System.out.println("===============================================================================================\n");
    }
}
