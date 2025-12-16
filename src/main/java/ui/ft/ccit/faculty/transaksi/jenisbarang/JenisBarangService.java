package ui.ft.ccit.faculty.transaksi.jenisbarang;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
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
            throw new IllegalArgumentException("namaJenis wajib diisi");
        }

        // Validasi jika ID sudah diisi manual dan sudah ada
        if (jenisBarang.getIdJenisBarang() != null 
            && jenisBarangRepository.existsById(jenisBarang.getIdJenisBarang())) {
            throw new DataAlreadyExistsException("JenisBarang", String.valueOf(jenisBarang.getIdJenisBarang()));
        }

        return jenisBarangRepository.save(jenisBarang);
    }

    // UPDATE
    public JenisBarang update(Byte id, JenisBarang updated) {
        JenisBarang existing = getById(id);

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
}