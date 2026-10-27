package smarthome.contract;

/**
 * Interface untuk perangkat yang mendukung fitur timer/penjadwalan otomatis.
 * Menandakan kapabilitas "can-do" (dapat dijadwalkan waktu operasionalnya).
 */
public interface Schedulable {

    /**
     * Menyetel jadwal aktif dan nonaktif perangkat.
     *
     * @param startTime waktu mulai (format HH:mm)
     * @param endTime   waktu selesai (format HH:mm)
     */
    void setSchedule(String startTime, String endTime);

    /**
     * Menghapus jadwal operasional yang sudah disetel sebelumnya.
     */
    void clearSchedule();

    /**
     * Memeriksa apakah jadwal operasional saat ini sedang aktif disetel.
     *
     * @return true jika ada jadwal aktif, false jika tidak ada
     */
    boolean isScheduleActive();

    /**
     * Mendapatkan deskripsi informasi jadwal yang terpasang.
     *
     * @return String representasi jadwal
     */
    String getScheduleInfo();
}
