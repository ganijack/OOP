package smarthome.contract;

/**
 * Interface representing automated timer scheduling capability.
 * Designates a "can-do" capability contract.
 */
public interface Schedulable {

    /**
     * Sets the operational active schedule window.
     *
     * @param startTime activation time (format HH:mm)
     * @param endTime   deactivation time (format HH:mm)
     */
    void setSchedule(String startTime, String endTime);

    /**
     * Cancels any active operational schedule.
     */
    void clearSchedule();

    /**
     * Checks whether an automated schedule is currently configured.
     *
     * @return true if schedule is active, false otherwise
     */
    boolean isScheduleActive();

    /**
     * Returns a formatted descriptor of the active schedule.
     *
     * @return String schedule descriptor
     */
    String getScheduleInfo();
}
