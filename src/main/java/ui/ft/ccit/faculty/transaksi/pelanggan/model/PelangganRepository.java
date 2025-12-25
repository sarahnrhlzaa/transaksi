package ui.ft.ccit.faculty.transaksi.pelanggan.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PelangganRepository extends JpaRepository<Pelanggan, String> {

    // cari berdasarkan nama mengandung kata tertentu
    List<Pelanggan> findByNamaContainingIgnoreCase(String keyword);

    // hitung berapa banyak pelanggan dengan idPelanggan dalam daftar tertentu
    long countByIdPelangganIn(List<String> idList);
}