package ui.ft.ccit.faculty.transaksi.transaksi.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransaksiRepository extends JpaRepository<Transaksi, String> {

    // cari transaksi berdasarkan id pelanggan
    List<Transaksi> findByIdPelanggan(String idPelanggan);

    // cari transaksi berdasarkan id karyawan
    List<Transaksi> findByIdKaryawan(String idKaryawan);

    // cari transaksi berdasarkan rentang tanggal
    List<Transaksi> findByTglTransaksiBetween(LocalDateTime start, LocalDateTime end);

    // hitung berapa banyak transaksi dengan kodeTransaksi dalam daftar tertentu
    long countByKodeTransaksiIn(List<String> kodeList);
}