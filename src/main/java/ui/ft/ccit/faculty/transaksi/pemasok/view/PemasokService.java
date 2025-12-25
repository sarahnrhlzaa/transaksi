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
import java.util.regex.Pattern;

@Service
@Transactional
public class PemasokService {

    private final PemasokRepository pemasokRepository;

    // Regex untuk validasi email sederhana
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

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
        return pemasokRepository.findByNamaPemasokContainingIgnoreCase(keyword);
    }

    // CREATE
    public Pemasok save(Pemasok pemasok) {
        validatePemasok(pemasok);

        if (pemasokRepository.existsById(pemasok.getIdPemasok())) {
            throw new DataAlreadyExistsException("Pemasok", pemasok.getIdPemasok());
        }

        return pemasokRepository.save(pemasok);
    }

    @Transactional
    public List<Pemasok> saveBulk(List<Pemasok> pemasokList) {
        for (Pemasok pemasok : pemasokList) {
            validatePemasok(pemasok);

            if (pemasokRepository.existsById(pemasok.getIdPemasok())) {
                throw new DataAlreadyExistsException("Pemasok", pemasok.getIdPemasok());
            }
        }
        return pemasokRepository.saveAll(pemasokList);
    }

    // UPDATE
    public Pemasok update(String id, Pemasok updated) {
        Pemasok existing = getById(id); // akan lempar DataNotFoundException

        // Validasi data yang akan diupdate (skip validasi idPemasok karena tidak berubah)
        validatePemasokForUpdate(updated);

        existing.setNamaPemasok(updated.getNamaPemasok());
        existing.setAlamat(updated.getAlamat());
        existing.setTelepon(updated.getTelepon());
        existing.setEmail(updated.getEmail());

        return pemasokRepository.save(existing);
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
        long existingCount = pemasokRepository.countByIdPemasokIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        pemasokRepository.deleteAllById(ids);
    }

    public void delete(String id) {
        if (!pemasokRepository.existsById(id)) {
            throw new DataNotFoundException("Pemasok", id);
        }
        pemasokRepository.deleteById(id);
    }

    // VALIDATION METHODS
    private void validatePemasok(Pemasok pemasok) {
        // Validasi idPemasok
        if (pemasok.getIdPemasok() == null || pemasok.getIdPemasok().isBlank()) {
            throw new InvalidDataException("Pemasok", "idPemasok", "null/blank");
        }
        if (pemasok.getIdPemasok().length() != 4) {
            throw new InvalidDataException("Pemasok", "idPemasok",
                    pemasok.getIdPemasok() + " (harus 4 karakter)");
        }

        // Validasi nama
        if (pemasok.getNamaPemasok() == null || pemasok.getNamaPemasok().isBlank()) {
            throw new InvalidDataException("Pemasok", "nama", "null/blank");
        }
        if (pemasok.getNamaPemasok().length() > 20) {
            throw new InvalidDataException("Pemasok", "nama",
                    pemasok.getNamaPemasok() + " (maksimal 20 karakter)");
        }

        // Validasi alamat
        if (pemasok.getAlamat() == null || pemasok.getAlamat().isBlank()) {
            throw new InvalidDataException("Pemasok", "alamat", "null/blank");
        }
        if (pemasok.getAlamat().length() > 50) {
            throw new InvalidDataException("Pemasok", "alamat",
                    "terlalu panjang (maksimal 50 karakter)");
        }

        // Validasi telepon
        if (pemasok.getTelepon() == null || pemasok.getTelepon().isBlank()) {
            throw new InvalidDataException("Pemasok", "telepon", "null/blank");
        }
        if (pemasok.getTelepon().length() > 15) {
            throw new InvalidDataException("Pemasok", "telepon",
                    pemasok.getTelepon() + " (maksimal 15 karakter)");
        }
        // Validasi format telepon (hanya angka, +, -, spasi, dan kurung)
        if (!pemasok.getTelepon().matches("[0-9+\\-\\s()]+")) {
            throw new InvalidDataException("Pemasok", "telepon",
                    pemasok.getTelepon() + " (format tidak valid)");
        }

        // Validasi email (optional)
        if (pemasok.getEmail() != null && !pemasok.getEmail().isBlank()) {
            if (pemasok.getEmail().length() > 40) {
                throw new InvalidDataException("Pemasok", "email",
                        "terlalu panjang (maksimal 40 karakter)");
            }
            if (!EMAIL_PATTERN.matcher(pemasok.getEmail()).matches()) {
                throw new InvalidDataException("Pemasok", "email",
                        pemasok.getEmail() + " (format email tidak valid)");
            }
        }
    }

    private void validatePemasokForUpdate(Pemasok pemasok) {
        // Validasi nama
        if (pemasok.getNamaPemasok() == null || pemasok.getNamaPemasok().isBlank()) {
            throw new InvalidDataException("Pemasok", "nama", "null/blank");
        }
        if (pemasok.getNamaPemasok().length() > 20) {
            throw new InvalidDataException("Pemasok", "nama",
                    pemasok.getNamaPemasok() + " (maksimal 20 karakter)");
        }

        // Validasi alamat
        if (pemasok.getAlamat() == null || pemasok.getAlamat().isBlank()) {
            throw new InvalidDataException("Pemasok", "alamat", "null/blank");
        }
        if (pemasok.getAlamat().length() > 50) {
            throw new InvalidDataException("Pemasok", "alamat",
                    "terlalu panjang (maksimal 50 karakter)");
        }

        // Validasi telepon
        if (pemasok.getTelepon() == null || pemasok.getTelepon().isBlank()) {
            throw new InvalidDataException("Pemasok", "telepon", "null/blank");
        }
        if (pemasok.getTelepon().length() > 15) {
            throw new InvalidDataException("Pemasok", "telepon",
                    pemasok.getTelepon() + " (maksimal 15 karakter)");
        }
        // Validasi format telepon
        if (!pemasok.getTelepon().matches("[0-9+\\-\\s()]+")) {
            throw new InvalidDataException("Pemasok", "telepon",
                    pemasok.getTelepon() + " (format tidak valid)");
        }

        // Validasi email (optional)
        if (pemasok.getEmail() != null && !pemasok.getEmail().isBlank()) {
            if (pemasok.getEmail().length() > 40) {
                throw new InvalidDataException("Pemasok", "email",
                        "terlalu panjang (maksimal 40 karakter)");
            }
            if (!EMAIL_PATTERN.matcher(pemasok.getEmail()).matches()) {
                throw new InvalidDataException("Pemasok", "email",
                        pemasok.getEmail() + " (format email tidak valid)");
            }
        }
    }
}