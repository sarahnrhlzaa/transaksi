package ui.ft.ccit.faculty.transaksi.jenisbarang.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
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

    public JenisBarang getById(Integer id) {
        return jenisBarangRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("JenisBarang", id.toString()));
    }

    // CREATE
    public JenisBarang save(JenisBarang jenisBarang) {
        // Validasi nama jenis tidak boleh kosong
        if (jenisBarang.getNamaJenis() == null || jenisBarang.getNamaJenis().isBlank()) {
            throw new ui.ft.ccit.faculty.transaksi.InvalidDataException("namaJenis", null, "Nama jenis barang wajib diisi");
        }

        // Validasi panjang nama jenis
        if (jenisBarang.getNamaJenis().length() > 20) {
            throw new ui.ft.ccit.faculty.transaksi.InvalidDataException("namaJenis", jenisBarang.getNamaJenis(), "Nama jenis barang maksimal 20 karakter");
        }

        if (jenisBarang.getIdJenisBarang() != null && jenisBarangRepository.existsById(jenisBarang.getIdJenisBarang().intValue())) {
            throw new DataAlreadyExistsException("JenisBarang", jenisBarang.getIdJenisBarang().toString());
        }

        return jenisBarangRepository.save(jenisBarang);
    }

    @Transactional
    public List<JenisBarang> saveBulk(List<JenisBarang> jenisBarangList) {
        for (JenisBarang jenisBarang : jenisBarangList) {
            // Validasi nama jenis tidak boleh kosong
            if (jenisBarang.getNamaJenis() == null || jenisBarang.getNamaJenis().isBlank()) {
                throw new ui.ft.ccit.faculty.transaksi.InvalidDataException("namaJenis", null, "Nama jenis barang wajib diisi untuk setiap data");
            }

            // Validasi panjang nama jenis
            if (jenisBarang.getNamaJenis().length() > 20) {
                throw new ui.ft.ccit.faculty.transaksi.InvalidDataException("namaJenis", jenisBarang.getNamaJenis(), "Nama jenis barang maksimal 20 karakter");
            }

            if (jenisBarang.getIdJenisBarang() != null && jenisBarangRepository.existsById(jenisBarang.getIdJenisBarang().intValue())) {
                throw new DataAlreadyExistsException("JenisBarang", jenisBarang.getIdJenisBarang().toString());
            }
        }
        return jenisBarangRepository.saveAll(jenisBarangList);
    }

    // UPDATE
    public JenisBarang update(Integer id, JenisBarang updated) {
        JenisBarang existing = getById(id); // akan lempar DataNotFoundException

        // Validasi nama jenis tidak boleh kosong
        if (updated.getNamaJenis() == null || updated.getNamaJenis().isBlank()) {
            throw new ui.ft.ccit.faculty.transaksi.InvalidDataException("namaJenis", null, "Nama jenis barang wajib diisi");
        }

        // Validasi panjang nama jenis
        if (updated.getNamaJenis().length() > 20) {
            throw new ui.ft.ccit.faculty.transaksi.InvalidDataException("namaJenis", updated.getNamaJenis(), "Nama jenis barang maksimal 20 karakter");
        }

        existing.setNamaJenis(updated.getNamaJenis());

        return jenisBarangRepository.save(existing);
    }

    // DELETE
    @Transactional
    public void deleteBulk(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List ID tidak boleh kosong");
        }

        // hard limit untuk keamanan
        if (ids.size() > 100) {
            throw new IllegalArgumentException("Maksimal 100 data per bulk delete");
        }

        jenisBarangRepository.deleteAllById(ids);
    }

    public void delete(Integer id) {
        if (!jenisBarangRepository.existsById(id)) {
            throw new DataNotFoundException("JenisBarang", id.toString());
        }
        jenisBarangRepository.deleteById(id);
    }
}