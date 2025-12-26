package ui.ft.ccit.faculty.transaksi.karyawan.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface KaryawanRepository extends JpaRepository<Karyawan, String> {

    // cari berdasarkan nama mengandung kata tertentu
    List<Karyawan> findByNamaContainingIgnoreCase(String keyword);

    // cari berdasarkan jenis kelamin
    List<Karyawan> findByJenisKelamin(String jenisKelamin);

    // cari karyawan dengan gaji di atas nilai tertentu
    List<Karyawan> findByGajiGreaterThanEqual(Double minGaji);

    // cari karyawan dengan gaji di bawah nilai tertentu
    List<Karyawan> findByGajiLessThanEqual(Double maxGaji);

    // cari karyawan berdasarkan range gaji
    List<Karyawan> findByGajiBetween(Double minGaji, Double maxGaji);

    // cari karyawan yang lahir setelah tanggal tertentu (lebih muda)
    List<Karyawan> findByTglLahirAfter(LocalDate date);

    // cari karyawan yang lahir sebelum tanggal tertentu (lebih tua)
    List<Karyawan> findByTglLahirBefore(LocalDate date);

    // hitung jumlah karyawan berdasarkan jenis kelamin
    long countByJenisKelamin(String jenisKelamin);

    // hitung berapa banyak karyawan dengan idKaryawan dalam daftar tertentu
    long countByIdKaryawanIn(List<String> idKaryawanList);

    // custom query: total gaji semua karyawan
    @Query("SELECT SUM(k.gaji) FROM Karyawan k")
    Double getTotalGaji();

    // custom query: rata-rata gaji
    @Query("SELECT AVG(k.gaji) FROM Karyawan k")
    Double getAverageGaji();
}