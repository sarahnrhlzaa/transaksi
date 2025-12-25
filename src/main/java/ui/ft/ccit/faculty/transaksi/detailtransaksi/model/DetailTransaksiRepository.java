package ui.ft.ccit.faculty.transaksi.detailtransaksi.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetailTransaksiRepository extends JpaRepository<DetailTransaksi, DetailTransaksi.DetailTransaksiId> {
    
    // Cari semua detail berdasarkan kode transaksi
    List<DetailTransaksi> findByKodeTransaksi(String kodeTransaksi);
    
    // Cari semua detail berdasarkan id barang
    List<DetailTransaksi> findByIdBarang(String idBarang);
    
    // Hapus semua detail dari satu transaksi
    void deleteByKodeTransaksi(String kodeTransaksi);
}