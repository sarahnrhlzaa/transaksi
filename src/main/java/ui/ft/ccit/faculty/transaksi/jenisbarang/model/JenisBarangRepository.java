package ui.ft.ccit.faculty.transaksi.jenisbarang.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JenisBarangRepository extends JpaRepository<JenisBarang, Byte> {

    // cari berdasarkan nama mengandung kata tertentu
    List<JenisBarang> findByNamaJenisContainingIgnoreCase(String keyword);

    // cek apakah nama jenis sudah ada (untuk validasi uniqueness)
    boolean existsByNamaJenisIgnoreCase(String namaJenis);
}