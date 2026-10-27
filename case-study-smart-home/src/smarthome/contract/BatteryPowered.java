package smarthome.contract;

/**
 * Interface representing portable battery-powered operation capability.
 * Designates a "can-do" capability contract.
 */
public interface BatteryPowered {

    /**
     * Gets the current remaining battery charge percentage (0 - 100%).
     *
     * @return remaining battery level percentage
     */
    int getBatteryLevel();

    /**
     * Checks if the device battery has reached a critical low threshold (e.g., <= 20%).
     *
     * @return true if low battery warning triggered, false otherwise
     */
    boolean isLowBattery();

    /**
     * Recharges the internal battery.
     *
     * @param amount percentage to replenish
     */
    void recharge(int amount);
}
