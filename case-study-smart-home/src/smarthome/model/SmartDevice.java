package smarthome.model;

import smarthome.validation.Validator;

/**
 * ABSTRACT CLASS: SmartDevice
 * Serves as the abstract superclass for all smart devices in the system.
 * Cannot be directly instantiated and provides common state and invariant behaviors.
 */
public abstract class SmartDevice {

    private final String id;
    private final String name;
    private final String room;
    private boolean isOn;
    private final double basePowerWatt;

    /**
     * Protected constructor invoked by concrete subclasses via super(...).
     *
     * @param id            unique device identifier
     * @param name          descriptive appliance name
     * @param room          room location
     * @param basePowerWatt base electrical power rating in Watts
     */
    public SmartDevice(String id, String name, String room, double basePowerWatt) {
        Validator.requireNonEmpty(id, "Device ID");
        Validator.requireNonEmpty(name, "Device Name");
        Validator.requireNonEmpty(room, "Room Location");
        Validator.requirePositive(basePowerWatt, "Base Power Watt");

        this.id = id.trim();
        this.name = name.trim();
        this.room = room.trim();
        this.basePowerWatt = basePowerWatt;
        this.isOn = false;
    }

    // ==========================================
    // ABSTRACT METHODS (Subclasses MUST override)
    // ==========================================

    /**
     * Executes the primary operational task specific to the device type.
     */
    public abstract void operate();

    /**
     * Calculates the current real-time electrical power consumption in Watts.
     *
     * @return electrical power in Watts
     */
    public abstract double calculatePowerConsumption();

    /**
     * Produces a concise summary string representing current device status.
     *
     * @return String status descriptor
     */
    public abstract String getStatusSummary();

    // ==========================================
    // CONCRETE METHODS (Inherited by subclasses)
    // ==========================================

    public void turnOn() {
        if (!this.isOn) {
            this.isOn = true;
            System.out.printf("[%s] %s in %s powered ON.%n", id, name, room);
        } else {
            System.out.printf("[%s] %s is already ACTIVE.%n", id, name);
        }
    }

    public void turnOff() {
        if (this.isOn) {
            this.isOn = false;
            System.out.printf("[%s] %s in %s powered OFF.%n", id, name, room);
        } else {
            System.out.printf("[%s] %s is already INACTIVE.%n", id, name);
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
     * Concrete template method formatting standard device status for dashboards.
     */
    public final String describe() {
        String powerStr = String.format("%.1fW", calculatePowerConsumption());
        String statusStr = isOn ? "ON " : "OFF";
        return String.format("[%-7s] %-24s | Room: %-12s | Status: [%s] | Power: %-8s | %s",
                id, name, room, statusStr, powerStr, getStatusSummary());
    }
}
