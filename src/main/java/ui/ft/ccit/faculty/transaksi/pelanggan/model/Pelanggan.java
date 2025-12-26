package ui.ft.ccit.faculty.transaksi.pelanggan.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pelanggan")
public class Pelanggan {

    @Id
    @Column(name = "id_pelanggan", length = 4)
    private String idPelanggan;

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

    @Column(name = "jenis_pelanggan", length = 1, nullable = false)
    private String jenisPelanggan = "S";

    protected Pelanggan() {
        // untuk JPA
    }

    public Pelanggan(String idPelanggan, String nama, String jenisKelamin, 
                     String alamat, LocalDate tglLahir, String jenisPelanggan) {
        this.idPelanggan = idPelanggan;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.tglLahir = tglLahir;
        this.jenisPelanggan = jenisPelanggan;
    }

    public Pelanggan(String idPelanggan, String nama, String jenisKelamin, 
                     String alamat, String telepon, LocalDate tglLahir, String jenisPelanggan) {
        this.idPelanggan = idPelanggan;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.telepon = telepon;
        this.tglLahir = tglLahir;
        this.jenisPelanggan = jenisPelanggan;
    }

    // === GETTERS & SETTERS ===

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
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

    public String getJenisPelanggan() {
        return jenisPelanggan;
    }

    public void setJenisPelanggan(String jenisPelanggan) {
        this.jenisPelanggan = jenisPelanggan;
    }
}