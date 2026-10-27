package smarthome.contract;

/**
 * Interface untuk perangkat pintar yang menggunakan daya baterai portabel.
 * Menandakan kapabilitas "can-do" (dapat beroperasi dengan baterai dan diisi ulang).
 */
public interface BatteryPowered {

    /**
     * Mendapatkan persentase sisa daya baterai (0 - 100%).
     *
     * @return persentase level baterai
     */
    int getBatteryLevel();

    /**
     * Memeriksa apakah status baterai sudah berada pada batas kritis/rendah (misal < 20%).
     *
     * @return true jika baterai rendah, false jika masih cukup
     */
    boolean isLowBattery();

    /**
     * Melakukan pengisian ulang daya baterai perangkat.
     *
     * @param amount persentase daya yang ditambahkan
     */
    void recharge(int amount);
}
