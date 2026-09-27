public class Silinder extends Lingkaran {

    private double tinggi;

    public Silinder(double tinggi, double radius, String warna) {
        super(radius, warna);
        this.tinggi = tinggi;
    }

    public double getTinggi() {
        return tinggi;
    }

    public void setTinggi(double t) {
        this.tinggi = t;
    }

    public double hitungVolume() {
        return hitungLuas() * tinggi;
    }

    @Override
    public void printInfo() {
        System.out.println("Silinder warna " + getWarna() + ", volume = " + hitungVolume());
    }
}
