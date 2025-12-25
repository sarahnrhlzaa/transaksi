package ui.ft.ccit.faculty.transaksi.detailtransaksi.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetailTransaksiRepository extends JpaRepository<DetailTransaksi, DetailTransaksi.DetailTransaksiId> {

    // cari detail transaksi berdasarkan kode transaksi
    @Query("SELECT d FROM DetailTransaksi d WHERE d.id.kodeTransaksi = ?1")
    List<DetailTransaksi> findByKodeTransaksi(String kodeTransaksi);

    // cari detail transaksi berdasarkan id barang
    @Query("SELECT d FROM DetailTransaksi d WHERE d.id.idBarang = ?1")
    List<DetailTransaksi> findByIdBarang(String idBarang);

    // hapus semua detail berdasarkan kode transaksi
    @Query("DELETE FROM DetailTransaksi d WHERE d.id.kodeTransaksi = ?1")
    void deleteByKodeTransaksi(String kodeTransaksi);
}