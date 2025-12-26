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
        if (barang.getIdBarang() == null || barang.getIdBarang().isBlank()) {
            throw new InvalidDataException("idBarang", "wajib diisi");
        }

        if (barangRepository.existsById(barang.getIdBarang())) {
            throw new DataAlreadyExistsException("Barang", barang.getIdBarang());
        }

        return barangRepository.save(barang);
    }

    @Transactional
    public List<Barang> saveBulk(List<Barang> barangList) {
        for (Barang barang : barangList) {
            if (barang.getIdBarang() == null || barang.getIdBarang().isBlank()) {
                throw new InvalidDataException("idBarang", "wajib diisi untuk setiap barang");
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
            throw new InvalidDataException("ids", "List ID tidak boleh kosong");
        }

        // hard limit untuk keamanan
        if (ids.size() > 100) {
            throw new InvalidDataException("ids", "Maksimal 100 data per bulk delete");
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