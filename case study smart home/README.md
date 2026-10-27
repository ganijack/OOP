# Sistem Manajemen Smart Home — Studi Kasus Abstract Class, Interface, Inheritance & Polymorphism

**Nama:** THORIQ ABDILLAH FALIAN KUSUMA  
**NIM:** F1D02410098  
**Kelas:** 5A  
**Mata Kuliah:** Pemrograman Berorientasi Objek (PBO)  
**Bahasa:** Java (JDK 11+)

---

## 1. Studi Kasus

Sistem Manajemen **Smart Home IoT** adalah simulasi ekosistem rumah pintar terpadu yang mengendalikan berbagai perangkat otomatisasi rumah (*smart appliances* & *security*).

- `SmartHub` bertindak sebagai sentral kendali yang mengelola banyak `SmartDevice`.
- Jenis perangkat pintar yang didukung:
  - `SmartLamp` (Lampu pintar dengan kontrol warna, tingkat kecerahan, dan jadwal aktif).
  - `SmartAC` (Pendingin ruangan dengan kendali suhu, kecepatan kipas, Wi-Fi, dan jadwal).
  - `SmartCamera` (Kamera pengawas keamanan berbaterai dengan Night Vision dan perekaman cloud/lokal).
  - `SmartDoorbellCamera` (Bel pintu kamera pintar dengan sensor deteksi gerakan tamu PIR dan bel nada dering).
- Setiap perangkat memiliki fungsi operasional yang unik serta konsumsi daya listrik (Watt) dinamis.
- Kemampuan khusus dipasang menggunakan **Interface**:
  - Hanya perangkat yang membutuhkan koneksi internet yang mengimplementasikan `WiFiConnectable`.
  - Hanya perangkat nirkabel yang mengimplementasikan `BatteryPowered`.
  - Perangkat yang mendukung timer otomasi mengimplementasikan `Schedulable`.

### Pemetaan Konsep OOP pada Studi Kasus:

| Konsep OOP | Implementasi pada Proyek |
|---|---|
| **Abstract Class** | `SmartDevice`, `SmartHub` |
| **Abstract Method** | `SmartDevice.operate()`, `SmartDevice.calculatePowerConsumption()`, `SmartDevice.getStatusSummary()`, `SmartHub.canAccess(User)` |
| **Interface** | `WiFiConnectable`, `BatteryPowered`, `Schedulable` |
| **Inheritance (Biasa)** | `SmartLamp extends SmartDevice`, `SmartAC extends SmartDevice`, `SmartCamera extends SmartDevice`, `StandardHomeHub extends SmartHub`, `SecureCloudHub extends SmartHub` |
| **Inheritance (Bertingkat / Multilevel)** | `SmartDevice` $\rightarrow$ `SmartCamera` $\rightarrow$ `SmartDoorbellCamera` |
| **Polymorphism (Runtime Overriding)** | Pemanggilan `device.operate()`, `device.calculatePowerConsumption()`, `hub.canAccess(user)` melalui referensi superclass |
| **Polymorphism (Interface-based)** | Monitoring serentak via `List<BatteryPowered>` dan pengaturan timer via `List<Schedulable>` |
| **Encapsulation & Validation** | Atribut dibuat `private`, sanitasi input melalui class `Validator`, dan error reporting menggunakan `ValidationException` |

---

## 2. Struktur Proyek

```
case-study-smarthome/
├── README.md
├── docs/
│   ├── class-diagram.mmd          # Source code Mermaid UML
│   └── class-diagram.png          # Gambar render Class Diagram UML
└── src/smarthome/
    ├── Main.java                  # Program demonstrasi pengujian utama
    ├── contract/                  # DAFTAR INTERFACE ("Can-Do" Capability)
    │   ├── WiFiConnectable.java   # Kontrak kapabilitas konektivitas Wi-Fi
    │   ├── BatteryPowered.java    # Kontrak kapabilitas baterai & isi ulang
    │   └── Schedulable.java       # Kontrak kapabilitas timer/penjadwalan
    ├── exception/                 # Custom Runtime Exception
    │   └── ValidationException.java
    ├── validation/                # Validasi data & batasan nilai
    │   └── Validator.java
    ├── model/                     # HIERARKI PERANGKAT (SmartDevice Hierarchy)
    │   ├── User.java              # Model penghuni rumah (Admin, Member, Guest)
    │   ├── SmartDevice.java       # ABSTRACT CLASS (Superclass semua perangkat)
    │   ├── SmartLamp.java         # implements Schedulable
    │   ├── SmartAC.java           # implements Schedulable, WiFiConnectable
    │   ├── SmartCamera.java       # implements WiFiConnectable, BatteryPowered
    │   └── SmartDoorbellCamera.java # extends SmartCamera (MULTILEVEL INHERITANCE)
    └── hub/                       # HIERARKI KONTROLER (SmartHub Hierarchy)
        ├── SmartHub.java          # ABSTRACT CLASS (Superclass pengelola ekosistem)
        ├── StandardHomeHub.java   # Hub lokal (akses terbuka keluarga)
        └── SecureCloudHub.java    # Hub cloud (akses khusus Admin/Owner)
```

---

## 3. Cara Menjalankan Program

### Di Windows (PowerShell):
```powershell
# 1. Kompilasi seluruh file Java ke folder 'out'
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName

# 2. Jalankan program utama
java -cp out smarthome.Main
```

### Di Linux / macOS (Bash):
```bash
# 1. Kompilasi seluruh file Java ke folder 'out'
javac -d out $(find src -name "*.java")

# 2. Jalankan program utama
java -cp out smarthome.Main
```

---

## 4. Class Diagram (UML)

File gambar diagram resolusi tinggi tersedia di: [`docs/class-diagram.png`](docs/class-diagram.png).

![Class Diagram](docs/class-diagram.png)

### Ringkasan Relasi:
1. **Hierarki Perangkat:**
   ```
   SmartDevice (abstract)
   ├── SmartLamp (implements Schedulable)
   ├── SmartAC (implements Schedulable, WiFiConnectable)
   └── SmartCamera (implements WiFiConnectable, BatteryPowered)
       └── SmartDoorbellCamera (Multilevel Inheritance)
   ```
2. **Hierarki Hub:**
   ```
   SmartHub (abstract)
   ├── StandardHomeHub
   └── SecureCloudHub
   ```

---

## 5. Penjelasan Mendalam Konsep OOP

### 5.1 Abstract Class dan Abstract Method

**Abstract class** adalah kelas yang bersifat konseptual dan tidak dapat diinstansiasi secara langsung menggunakan kata kunci `new`. Tujuannya adalah mendefinisikan atribut umum dan kerangka dasar yang wajib dimiliki oleh semua kelas turunannya.

Contoh pada `SmartDevice`:
```java
// KODE INI AKAN MENYEBABKAN ERROR KOMPILASI:
SmartDevice device = new SmartDevice("D-01", "Alat", "Kamar", 10.0); 
// Error: SmartDevice is abstract; cannot be instantiated
```
*Alasannya*: Di dunia nyata, tidak ada objek fisik bernama "Perangkat Pintar" saja. Yang ada adalah bentuk nyatanya seperti Lampu, AC, atau Kamera.

`SmartDevice` mendefinisikan **abstract method** tanpa tubuh (*body*):
```java
public abstract void operate();
public abstract double calculatePowerConsumption();
public abstract String getStatusSummary();
```
* Subclass **wajib meng-override** method-method ini. Jika ada subclass yang lupa mengimplementasikannya, program tidak akan bisa dikompilasi.
* Selain method abstrak, `SmartDevice` juga menyediakan **concrete method** (seperti `turnOn()`, `turnOff()`, dan `describe()`) yang sudah memiliki logika bawaan sehingga tidak perlu ditulis ulang oleh kelas anak.

Konsep serupa juga diterapkan pada abstract class kedua, yaitu `SmartHub`:
```java
public abstract boolean canAccess(User user);
public abstract String getHubType();
```
Method `canAccess(User user)` dibuat abstrak karena aturan keamanan akses antara hub lokal rumah dan hub cloud jarak jauh sangat berbeda.

---

### 5.2 Interface ("Can-Do" Capability)

**Interface** adalah kontrak perilaku/kemampuan (*contract of capability*). Kelas yang mengimplementasikan interface terikat perjanjian untuk menyediakan implementasi konkret dari method yang ada di interface tersebut.

```java
public interface BatteryPowered {
    int getBatteryLevel();
    boolean isLowBattery();
    void recharge(int amount);
}
```

| Interface | Diimplementasikan Oleh | Alasan Desain |
|---|---|---|
| `WiFiConnectable` | `SmartAC`, `SmartCamera` | Tidak semua alat rumah tangga terhubung ke internet. |
| `BatteryPowered` | `SmartCamera` (dan `SmartDoorbellCamera`) | Smart AC dan Lampu dicolokkan ke listrik PLN langsung, sedangkan kamera luar ruangan portabel memakai baterai. |
| `Schedulable` | `SmartLamp`, `SmartAC` | Fitur otomasi menyala dan mati di jam tertentu. |

#### Mengapa tidak menaruh method `getBatteryLevel()` langsung di abstract class `SmartDevice`?
Jika ditaruh di `SmartDevice`, maka `SmartLamp` dan `SmartAC` akan terpaksa memiliki method baterai, padahal keduanya tidak memiliki baterai sama sekali. Interface mencegah *interface pollution* dan menerapkan prinsip **Interface Segregation**.

#### Multiple Interface Implementation
Di Java, sebuah class hanya boleh memiliki **satu superclass** (`extends`), namun dapat mengimplementasikan **banyak interface** (`implements`).
Contoh pada `SmartAC`:
```java
public class SmartAC extends SmartDevice implements Schedulable, WiFiConnectable {
    // Mewarisi sifat perangkat pintar, sekaligus memiliki kapabilitas Wi-Fi dan Jadwal
}
```

---

### 5.3 Inheritance & Multilevel Inheritance

Pewarisan (*inheritance*) memungkinkan subclass menggunakan kembali kode atribut dan method milik superclass tanpa menulis ulang.

#### Single Inheritance:
`SmartLamp`, `SmartAC`, dan `SmartCamera` langsung mewarisi atribut (`id`, `name`, `room`, `isOn`, `basePowerWatt`) dan method dasar (`turnOn()`, `turnOff()`) dari `SmartDevice`.

#### Multilevel Inheritance (Pewarisan Bertingkat):
Hierarki: `SmartDevice` $\rightarrow$ `SmartCamera` $\rightarrow$ `SmartDoorbellCamera`.

`SmartDoorbellCamera` adalah turunan level 2. Class ini mewarisi:
1. Seluruh properti `SmartDevice` (status on/off, ID, ruangan).
2. Seluruh properti dan interface `SmartCamera` (kemampuan Wi-Fi, baterai, sensor video, night vision).
3. Menambahkan fitur unik baru: sensor deteksi gerak tamu (PIR) dan tombol bel pintu (`pressDoorbell()`).

```java
public class SmartDoorbellCamera extends SmartCamera {
    public SmartDoorbellCamera(...) {
        super(id, name, room, basePowerWatt, resolution, initialBattery); // memanggil konstruktor induk
    }

    public void pressDoorbell() {
        System.out.println("DING-DONG! Tombol bel ditekan!");
        startRecording(); // memanggil method warisan dari SmartCamera tanpa perlu buat ulang
    }
}
```

---

### 5.4 Polymorphism (Polimorfisme)

Polimorfisme berarti **"banyak bentuk"** — kemampuan mengeksekusi method yang sama melalui tipe referensi umum (parent/interface), tetapi menghasilkan perilaku spesifik sesuai dengan objek aslinya saat runtime.

#### 1. Runtime Polymorphism via Abstract Class:
Perhatikan method `calculateTotalPowerConsumption()` pada `SmartHub`:
```java
double totalWatt = 0.0;
for (SmartDevice device : devices) {
    // Tipe variabel adalah SmartDevice, tetapi method yang dipanggil
    // secara dinamis mengikuti logika SmartLamp, SmartAC, atau SmartCamera
    totalWatt += device.calculatePowerConsumption();
}
```
* Saat giliran `SmartLamp`: konsumsi daya dihitung berdasarkan `basePower * brightness%`.
* Saat giliran `SmartAC`: konsumsi daya dihitung berdasarkan beban kompresor suhu dan level fan.
* Saat giliran `SmartDoorbellCamera`: konsumsi daya otomatis menyertakan sensor PIR standby.

#### 2. Interface-Based Polymorphism:
Memperlakukan objek yang berbeda berdasarkan kemampuan bersamanya:
```java
List<BatteryPowered> batteryDevices = List.of(backyardCamera, frontDoorbell);

for (BatteryPowered dev : batteryDevices) {
    if (dev.isLowBattery()) {
        dev.recharge(50); // Menangani pengisian daya serentak tanpa peduli jenis kameranya
    }
}
```

#### 3. Polymorphic Access Control pada `SmartHub`:
```java
List<SmartHub> hubs = List.of(localHub, cloudHub);
for (SmartHub hub : hubs) {
    hub.canAccess(user); // StandardHomeHub mengizinkan semua anggota, SecureCloudHub hanya Admin
}
```

---

### 5.5 Encapsulation dan Validasi Data

Enkapsulasi memastikan integritas data internal objek terlindungi dari modifikasi langsung di luar aturan bisnis.

1. **Private Fields**: Semua atribut dideklarasikan `private`.
2. **Controlled Accessors**: Perubahan status hanya melalui method tervalidasi.
3. **Custom Exception & Validator**:
   Jika pengguna mencoba memasukkan nilai di luar jangkauan logika:
   ```java
   // Mengatur kecerahan lampu ke 150% (batas: 1-100%)
   livingRoomLamp.setBrightness(150); 
   // Memicu: ValidationException("Tingkat Kecerahan Lampu harus bernilai antara 1 dan 100")
   
   // Mengatur suhu AC ke 35°C (batas normal: 16-30°C)
   bedroomAC.setTargetTemperature(35);
   // Memicu: ValidationException("Suhu Pendingin Ruangan harus bernilai antara 16 dan 30")
   ```

---

## 6. Output Eksekusi Program

Berikut adalah cuplikan hasil saat program `smarthome.Main` dijalankan:

```text
===============================================================================================
                   STUDI KASUS SISTEM MANAJEMEN SMART HOME IOT                                
             Penerapan Abstract Class, Interface, Inheritance, & Polymorphism                 
===============================================================================================
 Nama   : THORIQ ABDILLAH FALIAN KUSUMA
 NIM    : F1D02410098
 Kelas  : 5A
 Matkul : Pemrograman Berorientasi Objek (PBO)
===============================================================================================

>>> 1. INISIALISASI PENGGUNA SISTEM
  * Thoriq Abdillah (USR-01 [ADMIN])
  * Falian (USR-02 [MEMBER])
  * Budi Santoso (USR-03 [GUEST])

>>> 2. REGISTRASI & SETUP PERANGKAT SMART HOME
  Semua perangkat berhasil diinstansiasi dengan benar.

>>> 3. MENGHUBUNGKAN PERANGKAT KE JARINGAN WI-FI & JADWAL OTOMATIS
[AC-01] AC terhubung ke Wi-Fi 'Thoriq_Home_5G' dengan IP: 192.168.1.144.
[CAM-01] Kamera streaming online via Wi-Fi 'Thoriq_Home_5G' [IP: 192.168.1.171].
[DBL-01] Kamera streaming online via Wi-Fi 'Thoriq_Home_5G' [IP: 192.168.1.152].
[LMP-01] Jadwal lampu berhasil disetel: 18:00 s/d 06:00.
[AC-01] Jadwal AC aktif disetel: 21:30 s/d 05:00.

>>> 4. MENGAKTIFKAN & MENJALANKAN FUNGSI OPERASIONAL PERANGKAT
[LMP-01] Lampu Ruang Tamu di Ruang Tamu berhasil DINYALAKAN.
[LMP-01] Lampu Ruang Tamu memancarkan cahaya Warm White dengan kecerahan 80% ke seluruh Ruang Tamu.
[AC-01] AC Kamar Utama di Kamar Tidur berhasil DINYALAKAN.
[AC-01] AC AC Kamar Utama sedang menghembuskan udara sejuk di Kamar Tidur | Target: 22 C | Fan: Level 3.
[CAM-01] Kamera Halaman Belakang di Halaman berhasil DINYALAKAN.
[CAM-01] Mode Night Vision DIAKTIFKAN.
[CAM-01] Mulai merekam video beresolusi 2K QHD ke penyimpanan lokal/cloud.
[CAM-01] Kamera Kamera Halaman Belakang memantau area Halaman [2K QHD, NightVision: ON, Baterai: 18%].
[DBL-01] Bel Pintu Utama di Pintu Depan berhasil DINYALAKAN.
[DBL-01] Kamera Bel Pintu Utama memantau area Pintu Depan [1080p Full HD, NightVision: OFF, Baterai: 85%].
   -> [Fitur Khusus Bel] PIR Sensor: SIAGA | Volume Chime: 8/10.
[ALERT GERAKAN di Pintu Depan]: Seseorang kurir paket terdeteksi di depan pintu!

[DING-DONG!] Tombol bel di Pintu Depan (Bel Pintu Utama) DITEKAN oleh seseorang! (Vol: 8/10)
>> Mengirim notifikasi push ke smartphone pemilik rumah...
[DBL-01] Mulai merekam video beresolusi 1080p Full HD ke penyimpanan lokal/cloud.

>>> 5. DEMO HIERARKI DUA: SMARTHUB (ABSTRACT CLASS & ACCESS CONTROL)
[Home Local Hub] Perangkat 'Lampu Ruang Tamu' (LMP-01) berhasil didaftarkan ke hub.
[Home Local Hub] Perangkat 'AC Kamar Utama' (AC-01) berhasil didaftarkan ke hub.
[Home Local Hub] Perangkat 'Kamera Halaman Belakang' (CAM-01) berhasil didaftarkan ke hub.
[Home Local Hub] Perangkat 'Bel Pintu Utama' (DBL-01) berhasil didaftarkan ke hub.
[AWS Smart Cloud Hub] Perangkat 'AC Kamar Utama' (AC-01) berhasil didaftarkan ke hub.
[AWS Smart Cloud Hub] Perangkat 'Bel Pintu Utama' (DBL-01) berhasil didaftarkan ke hub.

===============================================================================================
SMART HOME DASHBOARD - [Home Local Hub] (Tipe: Local Standard Hub (Akses Terbuka untuk Semua Penghuni))
Pemilik: Thoriq Abdillah (USR-01 [ADMIN]) | Total Perangkat: 4 Unit
-----------------------------------------------------------------------------------------------
 * [LMP-01 ] Lampu Ruang Tamu     | Ruang: Ruang Tamu   | Status: [ON ] | Daya: 9,6W     | Warna: Warm White, Kecerahan: 80% | Jadwal [18:00 - 06:00]
 * [AC-01  ] AC Kamar Utama       | Ruang: Kamar Tidur  | Status: [ON ] | Daya: 775,0W   | Suhu: 22 C, Fan: Lv3 | WiFi: 192.168.1.144 | Jadwal [21:30 - 05:00]
 * [CAM-01 ] Kamera Halaman Belakang | Ruang: Halaman      | Status: [ON ] | Daya: 10,5W    | Res: 2K QHD | Rekam: REC | Bat: 18% [LOW!] | WiFi: 192.168.1.171
 * [DBL-01 ] Bel Pintu Utama      | Ruang: Pintu Depan  | Status: [ON ] | Daya: 8,7W     | Res: 1080p Full HD | Rekam: REC | Bat: 85% | WiFi: 192.168.1.152 | Chime: Lv8 | PIR: ON
-----------------------------------------------------------------------------------------------
TOTAL ESTIMASI DAYA KONSUMSI SAAT INI: 803,80 WATT
===============================================================================================

>>> UJI AKSES PENGGUNA TERHADAP KEDUA HUB (POLYMORPHISM):
Pemeriksaan izin pada [Home Local Hub]:
   - User 'Thoriq Abdillah ' -> Akses: DIIZINKAN (Granted)
   - User 'Falian          ' -> Akses: DIIZINKAN (Granted)
   - User 'Budi Santoso    ' -> Akses: DIIZINKAN (Granted)
Pemeriksaan izin pada [AWS Smart Cloud Hub]:
   - User 'Thoriq Abdillah ' -> Akses: DIIZINKAN (Granted)
   - User 'Falian          ' -> Akses: DITOLAK (Denied)
   - User 'Budi Santoso    ' -> Akses: DITOLAK (Denied)

>>> 6. DEMONSTRASI POLIMORFISME BERBASIS INTERFACE
[A] Monitoring Perangkat Berbaterai (List<BatteryPowered>):
   * Kamera Halaman Belakang   | Sisa Baterai:  18% | Status Kritis: PERLU DICAS!
     --> Mengisi daya otomatis untuk Kamera Halaman Belakang...
[CAM-01] Baterai kamera diisi ulang (+50%). Sisa baterai sekarang: 68%.
   * Bel Pintu Utama           | Sisa Baterai:  85% | Status Kritis: Aman

[B] Manajemen Jadwal Otomatis (List<Schedulable>):
   * Lampu Ruang Tamu     -> Jadwal [18:00 - 06:00]
   * AC Kamar Utama       -> Jadwal [21:30 - 05:00]

>>> 7. PENGUJIAN ATURAN VALIDASI DATA (ENCAPSULATION)
Mencoba melakukan input di luar batas normal untuk menguji proteksi data:
  [Test 1] Menyetel suhu AC ke 35 C: GAGAL (Sesuai Harapan) -> Suhu Pendingin Ruangan (C) harus bernilai antara 16 dan 30 (diberikan: 35).
  [Test 2] Menyetel kecerahan lampu ke 150%: GAGAL (Sesuai Harapan) -> Tingkat Kecerahan Lampu harus bernilai antara 1 dan 100 (diberikan: 150).
  [Test 3] Menghubungkan WiFi dengan SSID kosong: GAGAL (Sesuai Harapan) -> SSID Wi-Fi tidak boleh kosong atau null.

>>> 8. POLIMORFISME RUTINITAS: SHUTDOWN SELURUH PERANGKAT OLEH ADMIN

>>> Eksekusi Rutinitas 'Matikan Semua Perangkat' oleh Thoriq Abdillah...
[LMP-01] Lampu Ruang Tamu di Ruang Tamu berhasil DIMATIKAN.
[AC-01] AC Kamar Utama di Kamar Tidur berhasil DIMATIKAN.
[CAM-01] Kamera Halaman Belakang di Halaman berhasil DIMATIKAN.
[DBL-01] Bel Pintu Utama di Pintu Depan berhasil DIMATIKAN.
[SUKSES] Seluruh perangkat di Home Local Hub berhasil dipadamkan.

===============================================================================================
SMART HOME DASHBOARD - [Home Local Hub] (Tipe: Local Standard Hub (Akses Terbuka untuk Semua Penghuni))
Pemilik: Thoriq Abdillah (USR-01 [ADMIN]) | Total Perangkat: 4 Unit
-----------------------------------------------------------------------------------------------
 * [LMP-01 ] Lampu Ruang Tamu     | Ruang: Ruang Tamu   | Status: [OFF] | Daya: 0,5W     | Warna: Warm White, Kecerahan: 80% | Jadwal [18:00 - 06:00]
 * [AC-01  ] AC Kamar Utama       | Ruang: Kamar Tidur  | Status: [OFF] | Daya: 2,5W     | Suhu: 22 C, Fan: Lv3 | WiFi: 192.168.1.144 | Jadwal [21:30 - 05:00]
 * [CAM-01 ] Kamera Halaman Belakang | Ruang: Halaman      | Status: [OFF] | Daya: 0,8W     | Res: 2K QHD | Rekam: REC | Bat: 68% | WiFi: 192.168.1.171
 * [DBL-01 ] Bel Pintu Utama      | Ruang: Pintu Depan  | Status: [OFF] | Daya: 2,0W     | Res: 1080p Full HD | Rekam: REC | Bat: 85% | WiFi: 192.168.1.152 | Chime: Lv8 | PIR: ON
-----------------------------------------------------------------------------------------------
TOTAL ESTIMASI DAYA KONSUMSI SAAT INI: 5,80 WATT
===============================================================================================

[SUKSES] Seluruh pengujian konsep OOP Smart Home telah berhasil dijalankan!
```

---

## 7. Format Jawaban Pengumpulan Forum

Salin teks berikut untuk di-submit ke forum kuliah:

```text
Subject: 5A_F1D02410098_THORIQ ABDILLAH FALIAN KUSUMA

Message:
[Sisipkan / Upload Gambar docs/class-diagram.png]

Link Repository (Public):
https://github.com/ganijack/OOP/tree/main/case%20study%20smart%20home

Brief Explanation:
Studi kasus ini berjudul "Sistem Manajemen Smart Home IoT", sebuah simulasi pengendalian ekosistem rumah pintar terpadu. Sistem ini mengelola berbagai perangkat otomatisasi seperti lampu pintar, AC, kamera pengawas, dan bel pintu pintar melalui sentral kendali (SmartHub).

Konsep OOP yang diimplementasikan:
1. Abstract Class & Abstract Method:
   - SmartDevice mendefinisikan abstract method operate(), calculatePowerConsumption(), dan getStatusSummary().
   - SmartHub mendefinisikan abstract method canAccess(User) dan getHubType().
2. Interface:
   - WiFiConnectable diimplementasikan oleh SmartAC dan SmartCamera.
   - BatteryPowered diimplementasikan oleh SmartCamera.
   - Schedulable diimplementasikan oleh SmartLamp dan SmartAC.
   Hal ini memisahkan kapabilitas agar hanya dipasang pada perangkat yang relevan (Interface Segregation).
3. Inheritance:
   - Single Inheritance: SmartLamp, SmartAC, SmartCamera terhadap SmartDevice, serta StandardHomeHub dan SecureCloudHub terhadap SmartHub.
   - Multilevel Inheritance: SmartDevice -> SmartCamera -> SmartDoorbellCamera. SmartDoorbellCamera mewarisi seluruh kemampuan SmartCamera dan menambahkan sensor gerak PIR serta tombol bel pintu.
4. Polymorphism:
   - Runtime Polymorphism: Pemanggilan calculatePowerConsumption() dan canAccess(User) dieksekusi melalui tipe induk/abstrak, namun perilakunya dinamis mengikuti objek aslinya tanpa pengecekan instanceof.
   - Interface Polymorphism: Pengelolaan koleksi berbasis interface List<BatteryPowered> dan List<Schedulable>.
5. Encapsulation:
   - Seluruh field bersifat private, mutasi status hanya dapat melalui method dengan validasi ketat menggunakan class Validator dan custom ValidationException.

Penjelasan lengkap kode dan dokumentasi teknis tersedia pada file README.md di repositori.
```
