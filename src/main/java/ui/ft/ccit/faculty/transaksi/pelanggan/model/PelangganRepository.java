package ui.ft.ccit.faculty.transaksi.pelanggan.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PelangganRepository extends JpaRepository<Pelanggan, String> {

    // cari berdasarkan nama mengandung kata tertentu
    List<Pelanggan> findByNamaContainingIgnoreCase(String keyword);

    // cari berdasarkan jenis kelamin
    List<Pelanggan> findByJenisKelamin(String jenisKelamin);

    // cari berdasarkan jenis pelanggan
    List<Pelanggan> findByJenisPelanggan(String jenisPelanggan);

    // cari berdasarkan kombinasi jenis kelamin dan jenis pelanggan
    List<Pelanggan> findByJenisKelaminAndJenisPelanggan(String jenisKelamin, String jenisPelanggan);

    // cari pelanggan yang lahir setelah tanggal tertentu (lebih muda)
    List<Pelanggan> findByTglLahirAfter(LocalDate date);

    // cari pelanggan yang lahir sebelum tanggal tertentu (lebih tua)
    List<Pelanggan> findByTglLahirBefore(LocalDate date);

    // hitung jumlah pelanggan berdasarkan jenis pelanggan
    long countByJenisPelanggan(String jenisPelanggan);

    // hitung jumlah pelanggan berdasarkan jenis kelamin
    long countByJenisKelamin(String jenisKelamin);

    // hitung berapa banyak pelanggan dengan idPelanggan dalam daftar tertentu
    long countByIdPelangganIn(List<String> idPelangganList);
}