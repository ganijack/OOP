package smarthome.hub;

import smarthome.model.SmartDevice;
import smarthome.model.User;
import smarthome.validation.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ABSTRACT CLASS: SmartHub
 * Central controller managing the smart home automation ecosystem.
 * Maintains a collection of devices (List<SmartDevice>) and provides
 * polymorphic routines for status dashboards and centralized power monitoring.
 */
public abstract class SmartHub {

    private final String hubId;
    private final String hubName;
    private final User owner;
    private final List<SmartDevice> devices;

    public SmartHub(String hubId, String hubName, User owner) {
        Validator.requireNonEmpty(hubId, "Hub ID");
        Validator.requireNonEmpty(hubName, "Hub Name");
        if (owner == null) {
            throw new IllegalArgumentException("Hub Owner must not be null.");
        }

        this.hubId = hubId.trim();
        this.hubName = hubName.trim();
        this.owner = owner;
        this.devices = new ArrayList<>();
    }

    // ==========================================
    // ABSTRACT METHODS (Subclasses MUST implement)
    // ==========================================

    /**
     * Verifies access permission for a given user attempting to manage the hub.
     *
     * @param user resident or guest attempting access
     * @return true if permitted, false otherwise
     */
    public abstract boolean canAccess(User user);

    /**
     * Returns the descriptive hub classification type.
     *
     * @return String hub type
     */
    public abstract String getHubType();

    // ==========================================
    // CONCRETE METHODS (Polymorphism Demonstration)
    // ==========================================

    public void addDevice(SmartDevice device) {
        if (device == null) {
            throw new IllegalArgumentException("Device must not be null.");
        }
        devices.add(device);
        System.out.printf("[%s] Device '%s' (%s) registered to hub.%n",
                hubName, device.getName(), device.getId());
    }

    public List<SmartDevice> getDevices() {
        return Collections.unmodifiableList(devices);
    }

    /**
     * RUNTIME POLYMORPHISM:
     * Computes the cumulative real-time power draw of all connected devices.
     * Invokes calculatePowerConsumption() on abstract SmartDevice references;
     * execution dynamically resolves to each concrete device's custom formula.
     */
    public double calculateTotalPowerConsumption() {
        double totalWatt = 0.0;
        for (SmartDevice device : devices) {
            totalWatt += device.calculatePowerConsumption();
        }
        return totalWatt;
    }

    /**
     * RUNTIME POLYMORPHISM:
     * Executes centralized power-off routine across all managed devices.
     */
    public void turnOffAllDevices(User user) {
        System.out.printf("%n>>> Executing 'Turn Off All Devices' routine by %s...%n", user.getName());
        if (!canAccess(user)) {
            System.out.printf("[DENIED] User %s does not have authority to control %s!%n",
                    user.getName(), hubName);
            return;
        }

        for (SmartDevice device : devices) {
            device.turnOff();
        }
        System.out.printf("[SUCCESS] All devices in %s successfully turned off.%n", hubName);
    }

    /**
     * Renders standard system dashboard for connected devices.
     */
    public void printDashboard() {
        System.out.println("\n" + "=".repeat(95));
        System.out.printf("SMART HOME DASHBOARD - [%s] (Type: %s)%n", hubName, getHubType());
        System.out.printf("Owner: %s | Total Devices: %d Units%n", owner, devices.size());
        System.out.println("-".repeat(95));

        if (devices.isEmpty()) {
            System.out.println("   (No devices currently installed)");
        } else {
            for (SmartDevice device : devices) {
                System.out.println(" * " + device.describe());
            }
        }

        System.out.println("-".repeat(95));
        System.out.printf("TOTAL ESTIMATED POWER CONSUMPTION: %.2f WATTS%n", calculateTotalPowerConsumption());
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
