package ui.ft.ccit.faculty.transaksi.barang.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarangRepository extends JpaRepository<Barang, String> {

    // cari berdasarkan nama mengandung kata tertentu
    List<Barang> findByNamaContainingIgnoreCase(String keyword);

    // contoh lain: barang dengan stok kurang dari angka tertentu
    List<Barang> findByStokLessThan(Integer stok);
}
