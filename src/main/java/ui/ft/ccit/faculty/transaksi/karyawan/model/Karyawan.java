package ui.ft.ccit.faculty.transaksi.karyawan.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "karyawan")
public class Karyawan {

    @Id
    @Column(name = "id_karyawan", length = 4)
    private String idKaryawan;

    @Column(name = "nama", length = 20, nullable = false)
    private String nama;

    @Column(name = "jenis_kelamin", length = 1, nullable = false)
    private String jenisKelamin = "L";

    @Column(name = "alamat", length = 50, nullable = false)
    private String alamat;

    @Column(name = "telepon", length = 15)
    private String telepon;

    @Column(name = "tgl_lahir", nullable = false)
    private LocalDate tglLahir;

    @Column(name = "gaji", nullable = false)
    private Double gaji;

    protected Karyawan() {
        // untuk JPA
    }

    public Karyawan(String idKaryawan, String nama, String jenisKelamin, 
                    String alamat, LocalDate tglLahir, Double gaji) {
        this.idKaryawan = idKaryawan;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.tglLahir = tglLahir;
        this.gaji = gaji;
    }

    public Karyawan(String idKaryawan, String nama, String jenisKelamin, 
                    String alamat, String telepon, LocalDate tglLahir, Double gaji) {
        this.idKaryawan = idKaryawan;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.telepon = telepon;
        this.tglLahir = tglLahir;
        this.gaji = gaji;
    }

    // === GETTERS & SETTERS ===

    public String getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(String idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public LocalDate getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(LocalDate tglLahir) {
        this.tglLahir = tglLahir;
    }

    public Double getGaji() {
        return gaji;
    }

    public void setGaji(Double gaji) {
        this.gaji = gaji;
    }
}