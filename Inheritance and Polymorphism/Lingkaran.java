public class Lingkaran extends Bentuk {

    private double radius;

    public static final double PHI = 3.14159265358979;

    public Lingkaran(double radius, String warna) {
        super(warna);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double r) {
        this.radius = r;
    }

    public double hitungLuas() {
        return PHI * radius * radius;
    }

    @Override
    public void printInfo() {
        System.out.println("Lingkaran " + getWarna() + ", luas = " + hitungLuas());
    }
}
