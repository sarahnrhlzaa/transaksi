package ui.ft.ccit.faculty.transaksi.jenisbarang.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JenisBarangRepository extends JpaRepository<JenisBarang, Integer> {

    // cari berdasarkan nama mengandung kata tertentu
    List<JenisBarang> findByNamaJenisContainingIgnoreCase(String keyword);

    // hitung berapa banyak jenis barang dengan id dalam daftar tertentu
    long countByIdJenisBarangIn(List<Integer> idList);
}