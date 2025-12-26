package ui.ft.ccit.faculty.transaksi.pemasok.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pemasok")
public class Pemasok {

    @Id
    @Column(name = "id_pemasok", length = 4)
    private String idPemasok;

    @Column(name = "nama", length = 20, nullable = false)
    private String nama;

    @Column(name = "alamat", length = 50, nullable = false)
    private String alamat;

    @Column(name = "telepon", length = 15, nullable = false)
    private String telepon;

    @Column(name = "email", length = 40)
    private String email;

    protected Pemasok() {
        // untuk JPA
    }

    public Pemasok(String idPemasok, String nama, String alamat, String telepon) {
        this.idPemasok = idPemasok;
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
    }

    public Pemasok(String idPemasok, String nama, String alamat, String telepon, String email) {
        this.idPemasok = idPemasok;
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
        this.email = email;
    }

    // === GETTERS & SETTERS ===

    public String getIdPemasok() {
        return idPemasok;
    }

    public void setIdPemasok(String idPemasok) {
        this.idPemasok = idPemasok;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}