public class Customer {
    private String namaDepan;
    private String namaBelakang;
    private Account[] daftarAkun;
    private int jumlahAkun;

    public Customer(String namaDepan, String namaBelakang) {
        this.namaDepan = namaDepan;
        this.namaBelakang = namaBelakang;
        this.daftarAkun = new Account[5];
        this.jumlahAkun = 0;
    }

    public String getNamaDepan() {
        return namaDepan;
    }

    public String getNamaBelakang() {
        return namaBelakang;
    }

    public String getNamaLengkap() {
        return namaDepan + " " + namaBelakang;
    }

    public boolean tambahAkun(Account akun) {
        if (jumlahAkun >= daftarAkun.length) {
            System.out.println("  >> Batas akun tercapai untuk " + getNamaLengkap());
            return false;
        }
        daftarAkun[jumlahAkun] = akun;
        jumlahAkun++;
        return true;
    }

    public Account getAkun(int idx) {
        if (idx >= 0 && idx < jumlahAkun) {
            return daftarAkun[idx];
        }
        return null;
    }

    public int getJumlahAkun() {
        return jumlahAkun;
    }

    public double hitungTotalSaldo() {
        double total = 0;
        for (int i = 0; i < jumlahAkun; i++) {
            total += daftarAkun[i].getSaldo();
        }
        return total;
    }

    public String info() {
        return String.format("%-22s | %d akun | Total: Rp%,.0f",
                getNamaLengkap(), jumlahAkun, hitungTotalSaldo());
    }
}
