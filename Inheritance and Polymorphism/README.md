# Tugas 4 — Encapsulation, Inheritance, dan Polymorphism

Program Java untuk mendemonstrasikan tiga pilar utama Object-Oriented Programming
(**Encapsulation**, **Inheritance**, **Polymorphism**) menggunakan studi kasus
bangun geometri: Bentuk, BujurSangkar, Lingkaran, dan Silinder.

---

## Struktur Class

```
Bentuk (parent)
 ├── BujurSangkar
 └── Lingkaran
      └── Silinder
```

| File | Deskripsi |
|---|---|
| `Bentuk.java` | Class induk dengan atribut `warna`, getter/setter, dan method `printInfo()`. |
| `BujurSangkar.java` | Turunan `Bentuk`. Memiliki atribut `sisi` dan method `hitungLuas()`. |
| `Lingkaran.java` | Turunan `Bentuk`. Memiliki atribut `radius`, konstanta `PHI`, dan method `hitungLuas()`. |
| `Silinder.java` | Turunan `Lingkaran` (pewarisan berjenjang). Memiliki atribut `tinggi` dan method `hitungVolume()`. |
| `Main.java` | Program utama yang menjalankan demo ketiga konsep OOP. |

---

## Konsep OOP yang Diterapkan

### 1. Encapsulation

Atribut `warna` pada class `Bentuk` diakses melalui method getter dan setter,
bukan secara langsung. Hal ini menjaga kontrol terhadap data yang masuk dan keluar.

```java
public String getWarna() {
    return warna;
}

public void setWarna(String warna) {
    this.warna = warna;
}
```

Pada `Main.java`, user diminta memasukkan warna baru melalui `Scanner`.
Perubahan dilakukan menggunakan `setWarna()` sebagai contoh encapsulation.

### 2. Inheritance

`BujurSangkar` dan `Lingkaran` mewarisi class `Bentuk` menggunakan keyword `extends`,
sehingga otomatis memiliki atribut `warna` beserta getter/setter-nya:

```java
public class BujurSangkar extends Bentuk {
    public BujurSangkar(double sisi, String warna) {
        super(warna);
        this.sisi = sisi;
    }
}
```

`Silinder` mewarisi `Lingkaran` (bukan langsung dari `Bentuk`), sehingga mendapatkan
atribut `radius` dan method `hitungLuas()` dari `Lingkaran`, lalu memanfaatkannya
untuk menghitung volume:

```java
public class Silinder extends Lingkaran {
    public double hitungVolume() {
        return hitungLuas() * tinggi;
    }
}
```

### 3. Polymorphism

Setiap subclass meng-override method `printInfo()` milik `Bentuk` dengan
implementasi masing-masing. Saat dipanggil lewat referensi bertipe `Bentuk`,
Java menjalankan versi override sesuai objek aslinya (runtime polymorphism):

```java
Bentuk[] semuaBentuk = { bentuk1, kotak, bulat, tabung };
for (Bentuk b : semuaBentuk) {
    b.printInfo();
}
```

Output yang dihasilkan berbeda-beda sesuai tipe objek:
```
Bentuk berwarna Ungu
Bujursangkar berwarna Biru, luas = 25.0
Lingkaran Kuning, luas = 153.9380400258997
Silinder warna Hijau, volume = 1539.380400258997
```

---

## Cara Menjalankan

```bash
javac *.java
java Main
```

Program akan meminta satu input warna baru lewat Scanner, kemudian menampilkan
demo Encapsulation, Inheritance, dan Polymorphism secara berurutan.

---

## Contoh Output

```
========================================
       DEMO ENCAPSULATION
========================================
Warna awal (via getter): Merah
Masukkan warna baru: Ungu
Warna setelah diubah (via getter): Ungu
Bentuk berwarna Ungu

========================================
       DEMO INHERITANCE
========================================
BujurSangkar mewarisi atribut warna dari Bentuk:
  Warna  : Biru
  Sisi   : 5.0
  Luas   : 25.0

Lingkaran mewarisi atribut warna dari Bentuk:
  Warna  : Kuning
  Radius : 7.0
  Luas   : 153.9380400258997

Silinder mewarisi dari Lingkaran (berjenjang):
  Warna  : Hijau
  Radius : 7.0
  Tinggi : 10.0
  Volume : 1539.380400258997

========================================
       DEMO POLYMORPHISM
========================================
Memanggil printInfo() melalui referensi tipe Bentuk:
----------------------------------------
Bentuk berwarna Ungu
Bujursangkar berwarna Biru, luas = 25.0
Lingkaran Kuning, luas = 153.9380400258997
Silinder warna Hijau, volume = 1539.380400258997
----------------------------------------
```
