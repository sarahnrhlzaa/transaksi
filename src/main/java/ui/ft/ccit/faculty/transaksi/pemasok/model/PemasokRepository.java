package ui.ft.ccit.faculty.transaksi.pemasok.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PemasokRepository extends JpaRepository<Pemasok, String> {

    // cari berdasarkan nama mengandung kata tertentu
    List<Pemasok> findByNamaPemasokContainingIgnoreCase(String keyword);

    // hitung berapa banyak pemasok dengan idPemasok dalam daftar tertentu
    long countByIdPemasokIn(List<String> idList);
}