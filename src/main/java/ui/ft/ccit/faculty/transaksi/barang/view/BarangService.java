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
        // Validasi idBarang
        if (barang.getIdBarang() == null || barang.getIdBarang().isBlank()) {
            throw new InvalidDataException("idBarang", null, "ID barang wajib diisi");
        }

        if (barang.getIdBarang().length() > 4) {
            throw new InvalidDataException("idBarang", barang.getIdBarang(), "ID barang maksimal 4 karakter");
        }

        // Validasi nama
        if (barang.getNama() == null || barang.getNama().isBlank()) {
            throw new InvalidDataException("nama", null, "Nama barang wajib diisi");
        }

        if (barang.getNama().length() > 255) {
            throw new InvalidDataException("nama", barang.getNama(), "Nama barang maksimal 255 karakter");
        }

        // Validasi stok
        if (barang.getStok() != null && barang.getStok() < 0) {
            throw new InvalidDataException("stok", barang.getStok(), "Stok tidak boleh negatif");
        }

        // Validasi harga
        if (barang.getHarga() != null && barang.getHarga() < 0) {
            throw new InvalidDataException("harga", barang.getHarga(), "Harga tidak boleh negatif");
        }

        // Validasi persenLaba
        if (barang.getPersenLaba() != null && (barang.getPersenLaba() < 0 || barang.getPersenLaba() > 100)) {
            throw new InvalidDataException("persenLaba", barang.getPersenLaba(), "Persentase laba harus antara 0-100");
        }

        // Validasi diskon
        if (barang.getDiskon() != null && (barang.getDiskon() < 0 || barang.getDiskon() > 100)) {
            throw new InvalidDataException("diskon", barang.getDiskon(), "Diskon harus antara 0-100");
        }

        if (barangRepository.existsById(barang.getIdBarang())) {
            throw new DataAlreadyExistsException("Barang", barang.getIdBarang());
        }

        return barangRepository.save(barang);
    }

    @Transactional
    public List<Barang> saveBulk(List<Barang> barangList) {
        for (Barang barang : barangList) {
            // Validasi idBarang
            if (barang.getIdBarang() == null || barang.getIdBarang().isBlank()) {
                throw new InvalidDataException("idBarang", null, "ID barang wajib diisi untuk setiap barang");
            }

            if (barang.getIdBarang().length() > 4) {
                throw new InvalidDataException("idBarang", barang.getIdBarang(), "ID barang maksimal 4 karakter");
            }

            // Validasi nama
            if (barang.getNama() == null || barang.getNama().isBlank()) {
                throw new InvalidDataException("nama", null, "Nama barang wajib diisi untuk setiap barang");
            }

            if (barang.getNama().length() > 255) {
                throw new InvalidDataException("nama", barang.getNama(), "Nama barang maksimal 255 karakter");
            }

            // Validasi stok
            if (barang.getStok() != null && barang.getStok() < 0) {
                throw new InvalidDataException("stok", barang.getStok(), "Stok tidak boleh negatif");
            }

            // Validasi harga
            if (barang.getHarga() != null && barang.getHarga() < 0) {
                throw new InvalidDataException("harga", barang.getHarga(), "Harga tidak boleh negatif");
            }

            // Validasi persenLaba
            if (barang.getPersenLaba() != null && (barang.getPersenLaba() < 0 || barang.getPersenLaba() > 100)) {
                throw new InvalidDataException("persenLaba", barang.getPersenLaba(), "Persentase laba harus antara 0-100");
            }

            // Validasi diskon
            if (barang.getDiskon() != null && (barang.getDiskon() < 0 || barang.getDiskon() > 100)) {
                throw new InvalidDataException("diskon", barang.getDiskon(), "Diskon harus antara 0-100");
            }

            if (barangRepository.existsById(barang.getIdBarang())) {
                throw new DataAlreadyExistsException("Barang", barang.getIdBarang());
            }
        }
        return barangRepository.saveAll(barangList);
    }

    // UPDATE
    public Barang update(String id, Barang updated) {
        Barang existing = getById(id); // akan lempar DataNotFoundException

        // Validasi nama
        if (updated.getNama() == null || updated.getNama().isBlank()) {
            throw new InvalidDataException("nama", null, "Nama barang wajib diisi");
        }

        if (updated.getNama().length() > 255) {
            throw new InvalidDataException("nama", updated.getNama(), "Nama barang maksimal 255 karakter");
        }

        // Validasi stok
        if (updated.getStok() != null && updated.getStok() < 0) {
            throw new InvalidDataException("stok", updated.getStok(), "Stok tidak boleh negatif");
        }

        // Validasi harga
        if (updated.getHarga() != null && updated.getHarga() < 0) {
            throw new InvalidDataException("harga", updated.getHarga(), "Harga tidak boleh negatif");
        }

        // Validasi persenLaba
        if (updated.getPersenLaba() != null && (updated.getPersenLaba() < 0 || updated.getPersenLaba() > 100)) {
            throw new InvalidDataException("persenLaba", updated.getPersenLaba(), "Persentase laba harus antara 0-100");
        }

        // Validasi diskon
        if (updated.getDiskon() != null && (updated.getDiskon() < 0 || updated.getDiskon() > 100)) {
            throw new InvalidDataException("diskon", updated.getDiskon(), "Diskon harus antara 0-100");
        }

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
}