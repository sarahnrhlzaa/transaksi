package ui.ft.ccit.faculty.transaksi.transaksi.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TransaksiRepository extends JpaRepository<Transaksi, String> {
    
    // Cari transaksi berdasarkan pelanggan
    List<Transaksi> findByIdPelanggan(String idPelanggan);
    
    // Cari transaksi berdasarkan karyawan
    List<Transaksi> findByIdKaryawan(String idKaryawan);
    
    // Cari transaksi dalam range tanggal
    List<Transaksi> findByTglTransaksiBetween(LocalDateTime start, LocalDateTime end);
}