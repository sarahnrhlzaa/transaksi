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
        return jenisBarangRepository.findById(Integer.valueOf(id))
                .orElseThrow(() -> new DataNotFoundException("JenisBarang", String.valueOf(id)));
    }

    public List<JenisBarang> searchByNama(String keyword) {
        return jenisBarangRepository.findByNamaJenisContainingIgnoreCase(keyword);
    }

    // CREATE
    public JenisBarang save(JenisBarang jenisBarang) {
        validateJenisBarang(jenisBarang);

        // Untuk auto-increment, biasanya id tidak perlu divalidasi ketat
        // Tapi jika user mengirim id manual, cek dulu
        if (jenisBarang.getIdJenisBarang() != null) {
            if (jenisBarangRepository.existsById(Integer.valueOf(jenisBarang.getIdJenisBarang()))) {
                throw new DataAlreadyExistsException("JenisBarang", String.valueOf(jenisBarang.getIdJenisBarang()));
            }
        }

        return jenisBarangRepository.save(jenisBarang);
    }

    @Transactional
    public List<JenisBarang> saveBulk(List<JenisBarang> jenisBarangList) {
        for (JenisBarang jb : jenisBarangList) {
            validateJenisBarang(jb);

            // Cek duplikasi jika id diberikan
            if (jb.getIdJenisBarang() != null) {
                if (jenisBarangRepository.existsById(Integer.valueOf(jb.getIdJenisBarang()))) {
                    throw new DataAlreadyExistsException("JenisBarang", String.valueOf(jb.getIdJenisBarang()));
                }
            }
        }
        return jenisBarangRepository.saveAll(jenisBarangList);
    }

    // UPDATE
    public JenisBarang update(Byte id, JenisBarang updated) {
        JenisBarang existing = getById(id); // akan lempar DataNotFoundException

        validateJenisBarangForUpdate(updated);

        existing.setNamaJenis(updated.getNamaJenis());

        return jenisBarangRepository.save(existing);
    }

    // DELETE
    @Transactional
    public void deleteBulk(List<Byte> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List ID tidak boleh kosong");
        }

        // hard limit untuk keamanan
        if (ids.size() > 100) {
            throw new IllegalArgumentException("Maksimal 100 data per bulk delete");
        }

        // Convert Byte to Integer untuk repository
        List<Integer> intIds = ids.stream()
                .map(Byte::intValue)
                .toList();

        // validasi: pastikan semua ID ada
        long existingCount = jenisBarangRepository.countByIdJenisBarangIn(intIds);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        jenisBarangRepository.deleteAllById(intIds);
    }

    public void delete(Byte id) {
        if (!jenisBarangRepository.existsById(Integer.valueOf(id))) {
            throw new DataNotFoundException("JenisBarang", String.valueOf(id));
        }
        jenisBarangRepository.deleteById(Integer.valueOf(id));
    }

    // VALIDATION METHODS
    private void validateJenisBarang(JenisBarang jenisBarang) {
        // Validasi namaJenis
        if (jenisBarang.getNamaJenis() == null || jenisBarang.getNamaJenis().isBlank()) {
            throw new InvalidDataException("JenisBarang", "namaJenis", "null/blank");
        }
        if (jenisBarang.getNamaJenis().length() > 20) {
            throw new InvalidDataException("JenisBarang", "namaJenis", jenisBarang.getNamaJenis() + " (maksimal 20 karakter)");
        }
    }

    private void validateJenisBarangForUpdate(JenisBarang jenisBarang) {
        // Validasi namaJenis
        if (jenisBarang.getNamaJenis() == null || jenisBarang.getNamaJenis().isBlank()) {
            throw new InvalidDataException("JenisBarang", "namaJenis", "null/blank");
        }
        if (jenisBarang.getNamaJenis().length() > 20) {
            throw new InvalidDataException("JenisBarang", "namaJenis", jenisBarang.getNamaJenis() + " (maksimal 20 karakter)");
        }
    }
}