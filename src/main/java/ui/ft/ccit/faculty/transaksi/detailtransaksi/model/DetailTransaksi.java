package ui.ft.ccit.faculty.transaksi.detailtransaksi.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "detail_transaksi")
public class DetailTransaksi {

    @EmbeddedId
    private DetailTransaksiId id;

    @Column(name = "jumlah", nullable = false)
    private Short jumlah;

    protected DetailTransaksi() {
        // untuk JPA
    }

    public DetailTransaksi(String kodeTransaksi, String idBarang, Short jumlah) {
        this.id = new DetailTransaksiId(kodeTransaksi, idBarang);
        this.jumlah = jumlah;
    }

    public DetailTransaksi(DetailTransaksiId id, Short jumlah) {
        this.id = id;
        this.jumlah = jumlah;
    }

    // === GETTERS & SETTERS ===

    public DetailTransaksiId getId() {
        return id;
    }

    public void setId(DetailTransaksiId id) {
        this.id = id;
    }

    public String getKodeTransaksi() {
        return id != null ? id.getKodeTransaksi() : null;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        if (this.id == null) {
            this.id = new DetailTransaksiId();
        }
        this.id.setKodeTransaksi(kodeTransaksi);
    }

    public String getIdBarang() {
        return id != null ? id.getIdBarang() : null;
    }

    public void setIdBarang(String idBarang) {
        if (this.id == null) {
            this.id = new DetailTransaksiId();
        }
        this.id.setIdBarang(idBarang);
    }

    public Short getJumlah() {
        return jumlah;
    }

    public void setJumlah(Short jumlah) {
        this.jumlah = jumlah;
    }

    // === EMBEDDED ID CLASS ===
    @Embeddable
    public static class DetailTransaksiId implements Serializable {

        @Column(name = "kode_transaksi", length = 4)
        private String kodeTransaksi;

        @Column(name = "id_barang", length = 4)
        private String idBarang;

        public DetailTransaksiId() {
        }

        public DetailTransaksiId(String kodeTransaksi, String idBarang) {
            this.kodeTransaksi = kodeTransaksi;
            this.idBarang = idBarang;
        }

        public String getKodeTransaksi() {
            return kodeTransaksi;
        }

        public void setKodeTransaksi(String kodeTransaksi) {
            this.kodeTransaksi = kodeTransaksi;
        }

        public String getIdBarang() {
            return idBarang;
        }

        public void setIdBarang(String idBarang) {
            this.idBarang = idBarang;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DetailTransaksiId that = (DetailTransaksiId) o;
            return Objects.equals(kodeTransaksi, that.kodeTransaksi) &&
                   Objects.equals(idBarang, that.idBarang);
        }

        @Override
        public int hashCode() {
            return Objects.hash(kodeTransaksi, idBarang);
        }
    }
}