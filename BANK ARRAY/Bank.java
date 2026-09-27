public class Bank {
    private String nama;
    private Customer[] daftarNasabah;
    private int numberOfCustomers;

    public Bank(String nama) {
        this.nama = nama;
        this.daftarNasabah = new Customer[10];
        this.numberOfCustomers = 0;
    }

    public String getNama() {
        return nama;
    }

    public void addCustomer(String namaDepan, String namaBelakang) {
        if (numberOfCustomers >= daftarNasabah.length) {
            System.out.println("  >> Kapasitas bank penuh, tidak bisa menambah nasabah lagi.");
            return;
        }
        daftarNasabah[numberOfCustomers] = new Customer(namaDepan, namaBelakang);
        numberOfCustomers++;
    }

    public Customer getCustomer(int idx) {
        if (idx >= 0 && idx < numberOfCustomers) {
            return daftarNasabah[idx];
        }
        return null;
    }

    public int getNumOfCustomers() {
        return numberOfCustomers;
    }

    public Customer cariNasabahByNama(String keyword) {
        for (int i = 0; i < numberOfCustomers; i++) {
            if (daftarNasabah[i].getNamaLengkap().toLowerCase().contains(keyword.toLowerCase())) {
                return daftarNasabah[i];
            }
        }
        return null;
    }
}
