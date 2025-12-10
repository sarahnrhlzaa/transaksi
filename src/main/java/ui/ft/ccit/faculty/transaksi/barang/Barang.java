package ui.ft.ccit.faculty.transaksi.barang;

import jakarta.persistence.*;

@Entity
@Table(name = "barang")
public class Barang {

    @Id
    @Column(name = "id_barang", length = 4)
    private String idBarang;

    @Column(name = "nama", length = 255)
    private String nama;

    @Column(name = "stok")
    private short stok;

    @Column(name = "harga")
    private Double harga;

    @Column(name = "persen_laba")
    private Double persenLaba;

    @Column(name = "diskon")
    private Double diskon;

    @Column(name = "id_jenis_brg")
    private Byte idJenisBarang;

    @Column(name = "id_pemasok", length = 4)
    private String idPemasok;

    protected Barang() {
        // for JPA
    }

    public Barang(String idBarang, String nama, short stok, Double harga,
            Double persenLaba, Double diskon,
            Byte idJenisBarang, String idPemasok) {
        this.idBarang = idBarang;
        this.nama = nama;
        this.stok = stok;
        this.harga = harga;
        this.persenLaba = persenLaba;
        this.diskon = diskon;
        this.idJenisBarang = idJenisBarang;
        this.idPemasok = idPemasok;
    }

    // getters & setters

    public String getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public short getStok() {
        return stok;
    }

    public void setStok(short stok) {
        this.stok = stok;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public Double getPersenLaba() {
        return persenLaba;
    }

    public void setPersenLaba(Double persenLaba) {
        this.persenLaba = persenLaba;
    }

    public Double getDiskon() {
        return diskon;
    }

    public void setDiskon(Double diskon) {
        this.diskon = diskon;
    }

    public Byte getIdJenisBarang() {
        return idJenisBarang;
    }

    public void setIdJenisBarang(Byte idJenisBarang) {
        this.idJenisBarang = idJenisBarang;
    }

    public String getIdPemasok() {
        return idPemasok;
    }

    public void setIdPemasok(String idPemasok) {
        this.idPemasok = idPemasok;
    }
}
