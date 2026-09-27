public class Account {
    private String noRekening;
    private double saldo;

    private static int counter = 100;

    public Account(double saldoAwal) {
        this.noRekening = "REK-" + String.format("%04d", counter++);
        this.saldo = Math.max(saldoAwal, 0);
    }

    public String getNoRekening() {
        return noRekening;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setor(double jumlah) {
        if (jumlah > 0) {
            saldo += jumlah;
            System.out.println("  >> Setor Rp" + formatRupiah(jumlah) + " berhasil.");
        } else {
            System.out.println("  >> Gagal: jumlah setor tidak valid.");
        }
    }

    public void tarik(double jumlah) {
        if (jumlah <= 0) {
            System.out.println("  >> Gagal: jumlah tarik tidak valid.");
        } else if (jumlah > saldo) {
            System.out.println("  >> Gagal: saldo Rp" + formatRupiah(saldo) + " tidak cukup untuk menarik Rp" + formatRupiah(jumlah) + ".");
        } else {
            saldo -= jumlah;
            System.out.println("  >> Tarik Rp" + formatRupiah(jumlah) + " berhasil.");
        }
    }

    public String ringkasan() {
        return noRekening + " | Saldo: Rp" + formatRupiah(saldo);
    }

    private String formatRupiah(double nilai) {
        return String.format("%,.0f", nilai);
    }
}
