import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("       DEMO ENCAPSULATION");
        System.out.println("========================================");

        Bentuk bentuk1 = new Bentuk("Merah");
        System.out.println("Warna awal (via getter): " + bentuk1.getWarna());

        System.out.print("Masukkan warna baru: ");
        String warnaBaru = input.nextLine();
        bentuk1.setWarna(warnaBaru);

        System.out.println("Warna setelah diubah (via getter): " + bentuk1.getWarna());
        bentuk1.printInfo();

        System.out.println();
        System.out.println("========================================");
        System.out.println("       DEMO INHERITANCE");
        System.out.println("========================================");

        BujurSangkar kotak = new BujurSangkar(5.0, "Biru");
        System.out.println("BujurSangkar mewarisi atribut warna dari Bentuk:");
        System.out.println("  Warna  : " + kotak.getWarna());
        System.out.println("  Sisi   : " + kotak.getSisi());
        System.out.println("  Luas   : " + kotak.hitungLuas());

        Lingkaran bulat = new Lingkaran(7.0, "Kuning");
        System.out.println("\nLingkaran mewarisi atribut warna dari Bentuk:");
        System.out.println("  Warna  : " + bulat.getWarna());
        System.out.println("  Radius : " + bulat.getRadius());
        System.out.println("  Luas   : " + bulat.hitungLuas());

        Silinder tabung = new Silinder(10.0, 7.0, "Hijau");
        System.out.println("\nSilinder mewarisi dari Lingkaran (berjenjang):");
        System.out.println("  Warna  : " + tabung.getWarna());
        System.out.println("  Radius : " + tabung.getRadius());
        System.out.println("  Tinggi : " + tabung.getTinggi());
        System.out.println("  Volume : " + tabung.hitungVolume());

        System.out.println();
        System.out.println("========================================");
        System.out.println("       DEMO POLYMORPHISM");
        System.out.println("========================================");

        Bentuk[] semuaBentuk = { bentuk1, kotak, bulat, tabung };

        System.out.println("Memanggil printInfo() melalui referensi tipe Bentuk:");
        System.out.println("----------------------------------------");
        for (Bentuk b : semuaBentuk) {
            b.printInfo();
        }
        System.out.println("----------------------------------------");

        input.close();
    }
}
