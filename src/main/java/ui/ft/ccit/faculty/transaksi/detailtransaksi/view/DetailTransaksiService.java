package ui.ft.ccit.faculty.transaksi.detailtransaksi.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.InvalidDataException;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksi;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksiRepository;
import ui.ft.ccit.faculty.transaksi.transaksi.model.TransaksiRepository;
import ui.ft.ccit.faculty.transaksi.barang.model.Barang;
import ui.ft.ccit.faculty.transaksi.barang.model.BarangRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetailTransaksiService {

    private final DetailTransaksiRepository detailTransaksiRepository;
    private final TransaksiRepository transaksiRepository;
    private final BarangRepository barangRepository;

    public DetailTransaksiService(DetailTransaksiRepository detailTransaksiRepository,
                                  TransaksiRepository transaksiRepository,
                                  BarangRepository barangRepository) {
        this.detailTransaksiRepository = detailTransaksiRepository;
        this.transaksiRepository = transaksiRepository;
        this.barangRepository = barangRepository;
    }

    public List<DetailTransaksi> getAll() {
        return detailTransaksiRepository.findAll();
    }

    public List<DetailTransaksi> getAllWithPagination(int page, int size) {
        return detailTransaksiRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public DetailTransaksi getById(String kodeTransaksi, String idBarang) {
        DetailTransaksi.DetailTransaksiId id = new DetailTransaksi.DetailTransaksiId(kodeTransaksi, idBarang);
        return detailTransaksiRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("DetailTransaksi", 
                        kodeTransaksi + "-" + idBarang));
    }

    public List<DetailTransaksi> searchByTransaksi(String kodeTransaksi) {
        return detailTransaksiRepository.findByKodeTransaksi(kodeTransaksi);
    }

    public List<DetailTransaksi> searchByBarang(String idBarang) {
        return detailTransaksiRepository.findByIdBarang(idBarang);
    }

    // CREATE - DENGAN STOK MANAGEMENT
    public DetailTransaksi save(DetailTransaksi detail) {
        validateDetailTransaksi(detail);
        validateForeignKeys(detail);

        DetailTransaksi.DetailTransaksiId id = new DetailTransaksi.DetailTransaksiId(
                detail.getKodeTransaksi(), detail.getIdBarang());
        if (detailTransaksiRepository.existsById(id)) {
            throw new DataAlreadyExistsException("DetailTransaksi", 
                    detail.getKodeTransaksi() + "-" + detail.getIdBarang());
        }

        // KURANGI STOK BARANG (karena barang keluar/terjual)
        updateStokBarang(detail.getIdBarang(), detail.getJumlah(), false);

        return detailTransaksiRepository.save(detail);
    }

    @Transactional
    public List<DetailTransaksi> saveBulk(List<DetailTransaksi> detailList) {
        for (DetailTransaksi detail : detailList) {
            validateDetailTransaksi(detail);
            validateForeignKeys(detail);

            DetailTransaksi.DetailTransaksiId id = new DetailTransaksi.DetailTransaksiId(
                    detail.getKodeTransaksi(), detail.getIdBarang());
            if (detailTransaksiRepository.existsById(id)) {
                throw new DataAlreadyExistsException("DetailTransaksi", 
                        detail.getKodeTransaksi() + "-" + detail.getIdBarang());
            }

            // KURANGI STOK BARANG
            updateStokBarang(detail.getIdBarang(), detail.getJumlah(), false);
        }
        return detailTransaksiRepository.saveAll(detailList);
    }

    // UPDATE - DENGAN STOK MANAGEMENT
    public DetailTransaksi update(String kodeTransaksi, String idBarang, DetailTransaksi updated) {
        DetailTransaksi existing = getById(kodeTransaksi, idBarang);

        validateJumlahForUpdate(updated);

        // Hitung selisih jumlah untuk update stok
        short selisih = (short) (updated.getJumlah() - existing.getJumlah());

        if (selisih != 0) {
            if (selisih > 0) {
                // Jumlah bertambah -> kurangi stok (barang keluar lebih banyak)
                updateStokBarang(idBarang, selisih, false);
            } else {
                // Jumlah berkurang -> tambah stok (barang dikembalikan)
                updateStokBarang(idBarang, (short) Math.abs(selisih), true);
            }
        }

        existing.setJumlah(updated.getJumlah());
        return detailTransaksiRepository.save(existing);
    }

    // DELETE - DENGAN STOK MANAGEMENT
    public void delete(String kodeTransaksi, String idBarang) {
        DetailTransaksi detail = getById(kodeTransaksi, idBarang);

        // KEMBALIKAN STOK BARANG (karena detail transaksi dihapus)
        updateStokBarang(detail.getIdBarang(), detail.getJumlah(), true);

        DetailTransaksi.DetailTransaksiId id = new DetailTransaksi.DetailTransaksiId(kodeTransaksi, idBarang);
        detailTransaksiRepository.deleteById(id);
    }

    @Transactional
    public void deleteByTransaksi(String kodeTransaksi) {
        List<DetailTransaksi> details = detailTransaksiRepository.findByKodeTransaksi(kodeTransaksi);
        
        if (details.isEmpty()) {
            throw new DataNotFoundException("DetailTransaksi dengan kodeTransaksi", kodeTransaksi);
        }

        // KEMBALIKAN SEMUA STOK BARANG
        for (DetailTransaksi detail : details) {
            updateStokBarang(detail.getIdBarang(), detail.getJumlah(), true);
        }

        detailTransaksiRepository.deleteByKodeTransaksi(kodeTransaksi);
    }

    // VALIDATION METHODS
    private void validateDetailTransaksi(DetailTransaksi detail) {
        // Validasi kodeTransaksi
        if (detail.getKodeTransaksi() == null || detail.getKodeTransaksi().isBlank()) {
            throw new InvalidDataException("DetailTransaksi", "kodeTransaksi", "null/blank");
        }
        if (detail.getKodeTransaksi().length() != 4) {
            throw new InvalidDataException("DetailTransaksi", "kodeTransaksi",
                    detail.getKodeTransaksi() + " (harus 4 karakter)");
        }

        // Validasi idBarang
        if (detail.getIdBarang() == null || detail.getIdBarang().isBlank()) {
            throw new InvalidDataException("DetailTransaksi", "idBarang", "null/blank");
        }
        if (detail.getIdBarang().length() != 4) {
            throw new InvalidDataException("DetailTransaksi", "idBarang",
                    detail.getIdBarang() + " (harus 4 karakter)");
        }

        // Validasi jumlah
        if (detail.getJumlah() == null) {
            throw new InvalidDataException("DetailTransaksi", "jumlah", "null");
        }
        if (detail.getJumlah() <= 0) {
            throw new InvalidDataException("DetailTransaksi", "jumlah",
                    String.valueOf(detail.getJumlah()) + " (harus lebih dari 0)");
        }
    }

    private void validateJumlahForUpdate(DetailTransaksi detail) {
        if (detail.getJumlah() == null) {
            throw new InvalidDataException("DetailTransaksi", "jumlah", "null");
        }
        if (detail.getJumlah() <= 0) {
            throw new InvalidDataException("DetailTransaksi", "jumlah",
                    String.valueOf(detail.getJumlah()) + " (harus lebih dari 0)");
        }
    }

    private void validateForeignKeys(DetailTransaksi detail) {
        // Validasi kodeTransaksi exists
        if (!transaksiRepository.existsById(detail.getKodeTransaksi())) {
            throw new DataNotFoundException("Transaksi", detail.getKodeTransaksi());
        }

        // Validasi idBarang exists
        if (!barangRepository.existsById(detail.getIdBarang())) {
            throw new DataNotFoundException("Barang", detail.getIdBarang());
        }
    }

    // STOK MANAGEMENT
    private void updateStokBarang(String idBarang, short jumlah, boolean tambahStok) {
        Barang barang = barangRepository.findById(idBarang)
                .orElseThrow(() -> new DataNotFoundException("Barang", idBarang));

        short stokLama = barang.getStok();
        short stokBaru;

        if (tambahStok) {
            // TAMBAH STOK (kembalikan barang / pembatalan transaksi)
            stokBaru = (short) (stokLama + jumlah);
        } else {
            // KURANGI STOK (barang keluar / transaksi baru)
            stokBaru = (short) (stokLama - jumlah);
            
            // Validasi stok tidak boleh negatif
            if (stokBaru < 0) {
                throw new InvalidDataException("Barang", "stok",
                        "Stok tidak cukup! Stok tersedia: " + stokLama + 
                        ", diminta: " + jumlah);
            }
        }

        barang.setStok(stokBaru);
        barangRepository.save(barang);
    }
}