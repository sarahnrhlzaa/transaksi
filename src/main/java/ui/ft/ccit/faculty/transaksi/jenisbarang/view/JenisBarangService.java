package ui.ft.ccit.faculty.transaksi.jenisbarang.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.InvalidDataException;
import ui.ft.ccit.faculty.transaksi.jenisbarang.model.JenisBarang;
import ui.ft.ccit.faculty.transaksi.jenisbarang.model.JenisBarangRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JenisBarangService {

    private final JenisBarangRepository jenisBarangRepository;

    public JenisBarangService(JenisBarangRepository jenisBarangRepository) {
        this.jenisBarangRepository = jenisBarangRepository;
    }

    public List<JenisBarang> getAll() {
        return jenisBarangRepository.findAll();
    }

    public List<JenisBarang> getAllWithPagination(int page, int size) {
        return jenisBarangRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public JenisBarang getById(Byte id) {
        return jenisBarangRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("JenisBarang", String.valueOf(id)));
    }

    public List<JenisBarang> searchByNama(String keyword) {
        return jenisBarangRepository.findByNamaJenisContainingIgnoreCase(keyword);
    }

    // CREATE
    public JenisBarang save(JenisBarang jenisBarang) {
        if (jenisBarang.getNamaJenis() == null || jenisBarang.getNamaJenis().isBlank()) {
            throw new InvalidDataException("namaJenis", "wajib diisi");
        }

        // validasi unique name
        if (jenisBarangRepository.existsByNamaJenisIgnoreCase(jenisBarang.getNamaJenis())) {
            throw new DataAlreadyExistsException("JenisBarang", jenisBarang.getNamaJenis());
        }

        return jenisBarangRepository.save(jenisBarang);
    }

    @Transactional
    public List<JenisBarang> saveBulk(List<JenisBarang> jenisBarangList) {
        for (JenisBarang jenisBarang : jenisBarangList) {
            if (jenisBarang.getNamaJenis() == null || jenisBarang.getNamaJenis().isBlank()) {
                throw new InvalidDataException("namaJenis", "wajib diisi untuk setiap jenis barang");
            }

            if (jenisBarangRepository.existsByNamaJenisIgnoreCase(jenisBarang.getNamaJenis())) {
                throw new DataAlreadyExistsException("JenisBarang", jenisBarang.getNamaJenis());
            }
        }
        return jenisBarangRepository.saveAll(jenisBarangList);
    }

    // UPDATE
    public JenisBarang update(Byte id, JenisBarang updated) {
        JenisBarang existing = getById(id);

        if (updated.getNamaJenis() == null || updated.getNamaJenis().isBlank()) {
            throw new InvalidDataException("namaJenis", "wajib diisi");
        }

        // cek apakah nama baru sudah dipakai oleh jenis lain
        if (!existing.getNamaJenis().equalsIgnoreCase(updated.getNamaJenis()) &&
                jenisBarangRepository.existsByNamaJenisIgnoreCase(updated.getNamaJenis())) {
            throw new DataAlreadyExistsException("JenisBarang", updated.getNamaJenis());
        }

        existing.setNamaJenis(updated.getNamaJenis());

        return jenisBarangRepository.save(existing);
    }

    // DELETE
    public void delete(Byte id) {
        if (!jenisBarangRepository.existsById(id)) {
            throw new DataNotFoundException("JenisBarang", String.valueOf(id));
        }
        jenisBarangRepository.deleteById(id);
    }

    @Transactional
    public void deleteBulk(List<Byte> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidDataException("ids", "List ID tidak boleh kosong");
        }

        if (ids.size() > 100) {
            throw new InvalidDataException("ids", "Maksimal 100 data per bulk delete");
        }

        // validasi: pastikan semua ID ada
        long existingCount = jenisBarangRepository.findAllById(ids).size();
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        jenisBarangRepository.deleteAllById(ids);
    }
}