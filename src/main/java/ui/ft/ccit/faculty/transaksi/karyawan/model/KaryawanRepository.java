package ui.ft.ccit.faculty.transaksi.karyawan.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KaryawanRepository extends JpaRepository<Karyawan, String> {

    // cari berdasarkan nama mengandung kata tertentu
    List<Karyawan> findByNamaContainingIgnoreCase(String keyword);

    // hitung berapa banyak karyawan dengan idKaryawan dalam daftar tertentu
    long countByIdKaryawanIn(List<String> idList);
}