package ui.ft.ccit.faculty.transaksi.pemasok.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.InvalidDataException;
import ui.ft.ccit.faculty.transaksi.pemasok.model.Pemasok;
import ui.ft.ccit.faculty.transaksi.pemasok.model.PemasokRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PemasokService {

    private final PemasokRepository pemasokRepository;

    public PemasokService(PemasokRepository pemasokRepository) {
        this.pemasokRepository = pemasokRepository;
    }

    public List<Pemasok> getAll() {
        return pemasokRepository.findAll();
    }

    public List<Pemasok> getAllWithPagination(int page, int size) {
        return pemasokRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public Pemasok getById(String id) {
        return pemasokRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Pemasok", id));
    }

    public List<Pemasok> searchByNama(String keyword) {
        return pemasokRepository.findByNamaContainingIgnoreCase(keyword);
    }

    // CREATE
    public Pemasok save(Pemasok pemasok) {
        // validasi ID wajib diisi
        if (pemasok.getIdPemasok() == null || pemasok.getIdPemasok().isBlank()) {
            throw new InvalidDataException("idPemasok", "wajib diisi");
        }

        // validasi ID sudah ada
        if (pemasokRepository.existsById(pemasok.getIdPemasok())) {
            throw new DataAlreadyExistsException("Pemasok", pemasok.getIdPemasok());
        }

        // validasi field wajib
        validateRequiredFields(pemasok);

        // validasi email unique (jika diisi)
        if (pemasok.getEmail() != null && !pemasok.getEmail().isBlank()) {
            if (pemasokRepository.existsByEmailIgnoreCase(pemasok.getEmail())) {
                throw new InvalidDataException("email", "sudah digunakan pemasok lain");
            }
        }

        return pemasokRepository.save(pemasok);
    }

    @Transactional
    public List<Pemasok> saveBulk(List<Pemasok> pemasokList) {
        for (Pemasok pemasok : pemasokList) {
            if (pemasok.getIdPemasok() == null || pemasok.getIdPemasok().isBlank()) {
                throw new InvalidDataException("idPemasok", "wajib diisi untuk setiap pemasok");
            }

            if (pemasokRepository.existsById(pemasok.getIdPemasok())) {
                throw new DataAlreadyExistsException("Pemasok", pemasok.getIdPemasok());
            }

            validateRequiredFields(pemasok);
        }
        return pemasokRepository.saveAll(pemasokList);
    }

    // UPDATE
    public Pemasok update(String id, Pemasok updated) {
        Pemasok existing = getById(id);

        validateRequiredFields(updated);

        // validasi email unique (jika diubah)
        if (updated.getEmail() != null && !updated.getEmail().isBlank()) {
            if (!updated.getEmail().equalsIgnoreCase(existing.getEmail()) &&
                    pemasokRepository.existsByEmailIgnoreCase(updated.getEmail())) {
                throw new InvalidDataException("email", "sudah digunakan pemasok lain");
            }
        }

        existing.setNama(updated.getNama());
        existing.setAlamat(updated.getAlamat());
        existing.setTelepon(updated.getTelepon());
        existing.setEmail(updated.getEmail());

        return pemasokRepository.save(existing);
    }

    // DELETE
    public void delete(String id) {
        if (!pemasokRepository.existsById(id)) {
            throw new DataNotFoundException("Pemasok", id);
        }
        pemasokRepository.deleteById(id);
    }

    @Transactional
    public void deleteBulk(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidDataException("ids", "List ID tidak boleh kosong");
        }

        if (ids.size() > 100) {
            throw new InvalidDataException("ids", "Maksimal 100 data per bulk delete");
        }

        // validasi: pastikan semua ID ada
        long existingCount = pemasokRepository.countByIdPemasokIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        pemasokRepository.deleteAllById(ids);
    }

    // HELPER: validasi field wajib
    private void validateRequiredFields(Pemasok pemasok) {
        if (pemasok.getNama() == null || pemasok.getNama().isBlank()) {
            throw new InvalidDataException("nama", "wajib diisi");
        }
        if (pemasok.getAlamat() == null || pemasok.getAlamat().isBlank()) {
            throw new InvalidDataException("alamat", "wajib diisi");
        }
        if (pemasok.getTelepon() == null || pemasok.getTelepon().isBlank()) {
            throw new InvalidDataException("telepon", "wajib diisi");
        }
    }
}