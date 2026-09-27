import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static Bank bank = new Bank("Kopdes Merah Putih");

    public static void main(String[] args) {
        isiDataContoh();

        int menu;
        do {
            tampilkanHeader();
            menu = bacaAngka(">> ");

            if (menu == 1) prosesRegistrasi();
            else if (menu == 2) prosesDaftarNasabah();
            else if (menu == 3) prosesBukaRekening();
            else if (menu == 4) prosesSetor();
            else if (menu == 5) prosesTarik();
            else if (menu == 6) prosesCekSaldo();
            else if (menu == 0) System.out.println("\nTerima kasih telah menggunakan " + bank.getNama() + "!");
            else System.out.println("  >> Menu tidak tersedia.");
        } while (menu != 0);
    }

    private static void isiDataContoh() {
        bank.addCustomer("Thoriq", "Abdillah");
        bank.getCustomer(0).tambahAkun(new Account(750000));

        bank.addCustomer("Halis", "Ibrahim");
        bank.getCustomer(1).tambahAkun(new Account(300000));
    }

    private static void tampilkanHeader() {
        System.out.println();
        System.out.println("================================");
        System.out.println("   " + bank.getNama().toUpperCase());
        System.out.println("   Jumlah Nasabah: " + bank.getNumOfCustomers());
        System.out.println("================================");
        System.out.println(" [1] Registrasi Nasabah");
        System.out.println(" [2] Daftar Nasabah");
        System.out.println(" [3] Buka Rekening Baru");
        System.out.println(" [4] Setor Tunai");
        System.out.println(" [5] Tarik Tunai");
        System.out.println(" [6] Cek Saldo");
        System.out.println(" [0] Keluar");
        System.out.println("--------------------------------");
    }

    // ---------- 1. Registrasi ----------
    private static void prosesRegistrasi() {
        System.out.println("\n-- Registrasi Nasabah Baru --");
        String depan = bacaTeks("Nama depan  : ");
        String belakang = bacaTeks("Nama belakang: ");

        bank.addCustomer(depan, belakang);
        int pos = bank.getNumOfCustomers() - 1;
        Customer baru = bank.getCustomer(pos);

        double setorAwal = bacaDesimal("Setoran awal (Rp): ");
        baru.tambahAkun(new Account(setorAwal));

        System.out.println("  >> Nasabah " + baru.getNamaLengkap() + " terdaftar dengan rekening " + baru.getAkun(0).getNoRekening());
    }

    // ---------- 2. Daftar Nasabah ----------
    private static void prosesDaftarNasabah() {
        System.out.println("\n-- Daftar Nasabah --");
        int total = bank.getNumOfCustomers();
        if (total == 0) {
            System.out.println("  (belum ada nasabah)");
            return;
        }
        for (int i = 0; i < total; i++) {
            System.out.println("  " + (i + 1) + ". " + bank.getCustomer(i).info());
        }
    }

    // ---------- 3. Buka Rekening ----------
    private static void prosesBukaRekening() {
        System.out.println("\n-- Buka Rekening Baru --");
        Customer nasabah = mintaPilihNasabah();
        if (nasabah == null) return;

        double setorAwal = bacaDesimal("Setoran awal (Rp): ");
        Account rekeningBaru = new Account(setorAwal);

        if (nasabah.tambahAkun(rekeningBaru)) {
            System.out.println("  >> Rekening " + rekeningBaru.getNoRekening() + " berhasil dibuka untuk " + nasabah.getNamaLengkap());
        }
    }

    // ---------- 4. Setor ----------
    private static void prosesSetor() {
        System.out.println("\n-- Setor Tunai --");
        Customer nasabah = mintaPilihNasabah();
        if (nasabah == null) return;

        Account rek = mintaPilihRekening(nasabah);
        if (rek == null) return;

        double jumlah = bacaDesimal("Jumlah setor (Rp): ");
        rek.setor(jumlah);
        System.out.println("  >> Sisa saldo: Rp" + String.format("%,.0f", rek.getSaldo()));
    }

    // ---------- 5. Tarik ----------
    private static void prosesTarik() {
        System.out.println("\n-- Tarik Tunai --");
        Customer nasabah = mintaPilihNasabah();
        if (nasabah == null) return;

        Account rek = mintaPilihRekening(nasabah);
        if (rek == null) return;

        double jumlah = bacaDesimal("Jumlah tarik (Rp): ");
        rek.tarik(jumlah);
        System.out.println("  >> Sisa saldo: Rp" + String.format("%,.0f", rek.getSaldo()));
    }

    // ---------- 6. Cek Saldo ----------
    private static void prosesCekSaldo() {
        System.out.println("\n-- Cek Saldo --");
        Customer nasabah = mintaPilihNasabah();
        if (nasabah == null) return;

        System.out.println("  Nasabah : " + nasabah.getNamaLengkap());
        System.out.println("  Rekening:");

        for (int i = 0; i < nasabah.getJumlahAkun(); i++) {
            System.out.println("    " + (i + 1) + ") " + nasabah.getAkun(i).ringkasan());
        }
        System.out.println("  ----------------------------------------");
        System.out.println("  Total Saldo: Rp" + String.format("%,.0f", nasabah.hitungTotalSaldo()));
    }

    // ========== Utilitas ==========

    private static Customer mintaPilihNasabah() {
        prosesDaftarNasabah();
        if (bank.getNumOfCustomers() == 0) return null;

        int pilih = bacaAngka("Pilih nasabah (nomor): ") - 1;
        Customer c = bank.getCustomer(pilih);
        if (c == null) {
            System.out.println("  >> Nasabah tidak ditemukan.");
        }
        return c;
    }

    private static Account mintaPilihRekening(Customer nasabah) {
        int jml = nasabah.getJumlahAkun();
        if (jml == 0) {
            System.out.println("  >> Nasabah ini belum punya rekening.");
            return null;
        }

        if (jml == 1) {
            System.out.println("  Rekening: " + nasabah.getAkun(0).ringkasan());
            return nasabah.getAkun(0);
        }

        System.out.println("  Pilih rekening:");
        for (int i = 0; i < jml; i++) {
            System.out.println("    " + (i + 1) + ") " + nasabah.getAkun(i).ringkasan());
        }
        int pilih = bacaAngka("  Nomor rekening: ") - 1;
        Account rek = nasabah.getAkun(pilih);
        if (rek == null) {
            System.out.println("  >> Rekening tidak ditemukan.");
        }
        return rek;
    }

    private static String bacaTeks(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int bacaAngka(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            sc.nextLine();
            System.out.print("  Input angka! " + prompt);
        }
        int hasil = sc.nextInt();
        sc.nextLine();
        return hasil;
    }

    private static double bacaDesimal(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextDouble()) {
            sc.nextLine();
            System.out.print("  Input angka! " + prompt);
        }
        double hasil = sc.nextDouble();
        sc.nextLine();
        return hasil;
    }
}
