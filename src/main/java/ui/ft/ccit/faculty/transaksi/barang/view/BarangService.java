package ui.ft.ccit.faculty.transaksi.barang.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.InvalidDataException;
import ui.ft.ccit.faculty.transaksi.barang.model.Barang;
import ui.ft.ccit.faculty.transaksi.barang.model.BarangRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BarangService {

    private final BarangRepository barangRepository;

    public BarangService(BarangRepository barangRepository) {
        this.barangRepository = barangRepository;
    }

    public List<Barang> getAll() {
        return barangRepository.findAll();
    }

    public List<Barang> getAllWithPagination(int page, int size) {
        return barangRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public Barang getById(String id) {
        return barangRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Barang", id));
    }

    public List<Barang> searchByNama(String keyword) {
        return barangRepository.findByNamaContainingIgnoreCase(keyword);
    }

    // CREATE
    public Barang save(Barang barang) {
        validateBarang(barang);

        if (barangRepository.existsById(barang.getIdBarang())) {
            throw new DataAlreadyExistsException("Barang", barang.getIdBarang());
        }

        return barangRepository.save(barang);
    }

    @Transactional
    public List<Barang> saveBulk(List<Barang> barangList) {
        for (Barang barang : barangList) {
            validateBarang(barang);

            if (barangRepository.existsById(barang.getIdBarang())) {
                throw new DataAlreadyExistsException("Barang", barang.getIdBarang());
            }
        }
        return barangRepository.saveAll(barangList);
    }

    // UPDATE
    public Barang update(String id, Barang updated) {
        Barang existing = getById(id); // akan lempar DataNotFoundException

        // Validasi data yang akan diupdate (skip validasi idBarang karena tidak berubah)
        validateBarangForUpdate(updated);

        existing.setNama(updated.getNama());
        existing.setStok(updated.getStok());
        existing.setHarga(updated.getHarga());
        existing.setPersenLaba(updated.getPersenLaba());
        existing.setDiskon(updated.getDiskon());
        existing.setIdJenisBarang(updated.getIdJenisBarang());
        existing.setIdPemasok(updated.getIdPemasok());

        return barangRepository.save(existing);
    }

    // DELETE
    @Transactional
    public void deleteBulk(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List ID tidak boleh kosong");
        }

        // hard limit untuk keamanan
        if (ids.size() > 100) {
            throw new IllegalArgumentException("Maksimal 100 data per bulk delete");
        }

        // validasi: pastikan semua ID ada
        long existingCount = barangRepository.countByIdBarangIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        barangRepository.deleteAllById(ids);
    }

    public void delete(String id) {
        if (!barangRepository.existsById(id)) {
            throw new DataNotFoundException("Barang", id);
        }
        barangRepository.deleteById(id);
    }

    // VALIDATION METHODS
    private void validateBarang(Barang barang) {
        // Validasi idBarang
        if (barang.getIdBarang() == null || barang.getIdBarang().isBlank()) {
            throw new InvalidDataException("Barang", "idBarang", "null/blank");
        }
        if (barang.getIdBarang().length() > 4) {
            throw new InvalidDataException("Barang", "idBarang", barang.getIdBarang() + " (maksimal 4 karakter)");
        }

        // Validasi nama
        if (barang.getNama() == null || barang.getNama().isBlank()) {
            throw new InvalidDataException("Barang", "nama", "null/blank");
        }
        if (barang.getNama().length() > 255) {
            throw new InvalidDataException("Barang", "nama", "terlalu panjang (maksimal 255 karakter)");
        }

        // Validasi stok
        if (barang.getStok() == null) {
            throw new InvalidDataException("Barang", "stok", "null");
        }
        if (barang.getStok() < 0) {
            throw new InvalidDataException("Barang", "stok", String.valueOf(barang.getStok()) + " (tidak boleh negatif)");
        }

        // Validasi harga
        if (barang.getHarga() == null) {
            throw new InvalidDataException("Barang", "harga", "null");
        }
        if (barang.getHarga() < 0) {
            throw new InvalidDataException("Barang", "harga", String.valueOf(barang.getHarga()) + " (tidak boleh negatif)");
        }

        // Validasi persenLaba
        if (barang.getPersenLaba() == null) {
            throw new InvalidDataException("Barang", "persenLaba", "null");
        }
        if (barang.getPersenLaba() < 0 || barang.getPersenLaba() > 100) {
            throw new InvalidDataException("Barang", "persenLaba", String.valueOf(barang.getPersenLaba()) + " (harus antara 0-100)");
        }

        // Validasi diskon
        if (barang.getDiskon() == null) {
            barang.setDiskon(0.0); // Set default jika null
        }
        if (barang.getDiskon() < 0 || barang.getDiskon() > 100) {
            throw new InvalidDataException("Barang", "diskon", String.valueOf(barang.getDiskon()) + " (harus antara 0-100)");
        }

        // Validasi idPemasok
        if (barang.getIdPemasok() != null && barang.getIdPemasok().length() > 4) {
            throw new InvalidDataException("Barang", "idPemasok", barang.getIdPemasok() + " (maksimal 4 karakter)");
        }
    }

    private void validateBarangForUpdate(Barang barang) {
        // Validasi nama
        if (barang.getNama() == null || barang.getNama().isBlank()) {
            throw new InvalidDataException("Barang", "nama", "null/blank");
        }
        if (barang.getNama().length() > 255) {
            throw new InvalidDataException("Barang", "nama", "terlalu panjang (maksimal 255 karakter)");
        }

        // Validasi stok
        if (barang.getStok() == null) {
            throw new InvalidDataException("Barang", "stok", "null");
        }
        if (barang.getStok() < 0) {
            throw new InvalidDataException("Barang", "stok", String.valueOf(barang.getStok()) + " (tidak boleh negatif)");
        }

        // Validasi harga
        if (barang.getHarga() == null) {
            throw new InvalidDataException("Barang", "harga", "null");
        }
        if (barang.getHarga() < 0) {
            throw new InvalidDataException("Barang", "harga", String.valueOf(barang.getHarga()) + " (tidak boleh negatif)");
        }

        // Validasi persenLaba
        if (barang.getPersenLaba() == null) {
            throw new InvalidDataException("Barang", "persenLaba", "null");
        }
        if (barang.getPersenLaba() < 0 || barang.getPersenLaba() > 100) {
            throw new InvalidDataException("Barang", "persenLaba", String.valueOf(barang.getPersenLaba()) + " (harus antara 0-100)");
        }

        // Validasi diskon
        if (barang.getDiskon() == null) {
            throw new InvalidDataException("Barang", "diskon", "null");
        }
        if (barang.getDiskon() < 0 || barang.getDiskon() > 100) {
            throw new InvalidDataException("Barang", "diskon", String.valueOf(barang.getDiskon()) + " (harus antara 0-100)");
        }

        // Validasi idPemasok
        if (barang.getIdPemasok() != null && barang.getIdPemasok().length() > 4) {
            throw new InvalidDataException("Barang", "idPemasok", barang.getIdPemasok() + " (maksimal 4 karakter)");
        }
    }
}