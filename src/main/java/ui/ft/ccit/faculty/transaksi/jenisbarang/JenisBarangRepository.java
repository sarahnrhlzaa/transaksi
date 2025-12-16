package ui.ft.ccit.faculty.transaksi.jenisbarang;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JenisBarangRepository extends JpaRepository<JenisBarang, Byte> {
    
    // cari berdasarkan nama jenis mengandung kata tertentu
    List<JenisBarang> findByNamaJenisContainingIgnoreCase(String keyword);
}