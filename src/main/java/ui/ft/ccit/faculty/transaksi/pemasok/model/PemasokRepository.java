package ui.ft.ccit.faculty.transaksi.pemasok.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PemasokRepository extends JpaRepository<Pemasok, String> {

    // cari berdasarkan nama mengandung kata tertentu
    List<Pemasok> findByNamaContainingIgnoreCase(String keyword);

    // cari berdasarkan alamat mengandung kata tertentu
    List<Pemasok> findByAlamatContainingIgnoreCase(String keyword);

    // cari berdasarkan telepon
    List<Pemasok> findByTeleponContaining(String telepon);

    // cek apakah email sudah digunakan (untuk validasi uniqueness)
    boolean existsByEmailIgnoreCase(String email);

    // hitung berapa banyak pemasok dengan idPemasok dalam daftar tertentu
    long countByIdPemasokIn(List<String> idPemasokList);
}