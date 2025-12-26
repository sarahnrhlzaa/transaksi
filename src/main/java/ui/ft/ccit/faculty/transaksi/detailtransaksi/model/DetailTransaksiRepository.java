package ui.ft.ccit.faculty.transaksi.detailtransaksi.model;

import org.springframework.data.jpa.repository.JpaRepository;

import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksi.DetailTransaksiId;

import java.util.List;

public interface DetailTransaksiRepository extends JpaRepository<DetailTransaksi, DetailTransaksiId> {

    // cari semua detail transaksi berdasarkan kode transaksi
    List<DetailTransaksi> findByIdKodeTransaksi(String kodeTransaksi);

    // cari semua detail transaksi berdasarkan id barang
    List<DetailTransaksi> findByIdIdBarang(String idBarang);

    // cek apakah sudah ada detail transaksi dengan kode transaksi tertentu
    boolean existsByIdKodeTransaksi(String kodeTransaksi);

    // cek apakah sudah ada detail transaksi dengan id barang tertentu
    boolean existsByIdIdBarang(String idBarang);

    // hapus semua detail transaksi berdasarkan kode transaksi
    void deleteByIdKodeTransaksi(String kodeTransaksi);

    // hapus semua detail transaksi berdasarkan id barang
    void deleteByIdIdBarang(String idBarang);

    // hitung total detail transaksi untuk kode transaksi tertentu
    long countByIdKodeTransaksi(String kodeTransaksi);

    // hitung total detail transaksi untuk id barang tertentu
    long countByIdIdBarang(String idBarang);

    // hitung berapa banyak detail transaksi dengan ID dalam daftar tertentu (untuk bulk validation)
    long countByIdIn(List<DetailTransaksiId> ids);
}