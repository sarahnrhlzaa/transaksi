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
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

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

    // CREATE
    public Pemasok save(Pemasok pemasok) {
        // Validasi idPemasok
        if (pemasok.getIdPemasok() == null || pemasok.getIdPemasok().isBlank()) {
            throw new InvalidDataException("idPemasok", null, "ID pemasok wajib diisi");
        }

        if (pemasok.getIdPemasok().length() != 4) {
            throw new InvalidDataException("idPemasok", pemasok.getIdPemasok(), "ID pemasok harus 4 karakter");
        }

        // Validasi nama
        if (pemasok.getNamaPemasok() == null || pemasok.getNamaPemasok().isBlank()) {
            throw new InvalidDataException("nama", null, "Nama pemasok wajib diisi");
        }

        if (pemasok.getNamaPemasok().length() > 20) {
            throw new InvalidDataException("nama", pemasok.getNamaPemasok(), "Nama pemasok maksimal 20 karakter");
        }

        // Validasi alamat
        if (pemasok.getAlamat() == null || pemasok.getAlamat().isBlank()) {
            throw new InvalidDataException("alamat", null, "Alamat wajib diisi");
        }

        if (pemasok.getAlamat().length() > 50) {
            throw new InvalidDataException("alamat", pemasok.getAlamat(), "Alamat maksimal 50 karakter");
        }

        // Validasi telepon
        if (pemasok.getTelepon() == null || pemasok.getTelepon().isBlank()) {
            throw new InvalidDataException("telepon", null, "Telepon wajib diisi");
        }

        if (pemasok.getTelepon().length() > 15) {
            throw new InvalidDataException("telepon", pemasok.getTelepon(), "Telepon maksimal 15 karakter");
        }

        // Validasi email (opsional, tapi kalau diisi harus valid)
        if (pemasok.getEmail() != null && !pemasok.getEmail().isBlank()) {
            if (pemasok.getEmail().length() > 40) {
                throw new InvalidDataException("email", pemasok.getEmail(), "Email maksimal 40 karakter");
            }

            if (!EMAIL_PATTERN.matcher(pemasok.getEmail()).matches()) {
                throw new InvalidDataException("email", pemasok.getEmail(), "Format email tidak valid");
            }
        }

        if (pemasokRepository.existsById(pemasok.getIdPemasok())) {
            throw new DataAlreadyExistsException("Pemasok", pemasok.getIdPemasok());
        }

        return pemasokRepository.save(pemasok);
    }

    @Transactional
    public List<Pemasok> saveBulk(List<Pemasok> pemasokList) {
        for (Pemasok pemasok : pemasokList) {
            // Validasi idPemasok
            if (pemasok.getIdPemasok() == null || pemasok.getIdPemasok().isBlank()) {
                throw new InvalidDataException("idPemasok", null, "ID pemasok wajib diisi untuk setiap data");
            }

            if (pemasok.getIdPemasok().length() != 4) {
                throw new InvalidDataException("idPemasok", pemasok.getIdPemasok(), "ID pemasok harus 4 karakter");
            }

            // Validasi nama
            if (pemasok.getNamaPemasok() == null || pemasok.getNamaPemasok().isBlank()) {
                throw new InvalidDataException("nama", null, "Nama pemasok wajib diisi untuk setiap data");
            }

            if (pemasok.getNamaPemasok().length() > 20) {
                throw new InvalidDataException("nama", pemasok.getNamaPemasok(), "Nama pemasok maksimal 20 karakter");
            }

            // Validasi alamat
            if (pemasok.getAlamat() == null || pemasok.getAlamat().isBlank()) {
                throw new InvalidDataException("alamat", null, "Alamat wajib diisi untuk setiap data");
            }

            if (pemasok.getAlamat().length() > 50) {
                throw new InvalidDataException("alamat", pemasok.getAlamat(), "Alamat maksimal 50 karakter");
            }

            // Validasi telepon
            if (pemasok.getTelepon() == null || pemasok.getTelepon().isBlank()) {
                throw new InvalidDataException("telepon", null, "Telepon wajib diisi untuk setiap data");
            }

            if (pemasok.getTelepon().length() > 15) {
                throw new InvalidDataException("telepon", pemasok.getTelepon(), "Telepon maksimal 15 karakter");
            }

            // Validasi email (opsional, tapi kalau diisi harus valid)
            if (pemasok.getEmail() != null && !pemasok.getEmail().isBlank()) {
                if (pemasok.getEmail().length() > 40) {
                    throw new InvalidDataException("email", pemasok.getEmail(), "Email maksimal 40 karakter");
                }

                if (!EMAIL_PATTERN.matcher(pemasok.getEmail()).matches()) {
                    throw new InvalidDataException("email", pemasok.getEmail(), "Format email tidak valid");
                }
            }

            if (pemasokRepository.existsById(pemasok.getIdPemasok())) {
                throw new DataAlreadyExistsException("Pemasok", pemasok.getIdPemasok());
            }
        }
        return pemasokRepository.saveAll(pemasokList);
    }

    // UPDATE
    public Pemasok update(String id, Pemasok updated) {
        Pemasok existing = getById(id); // akan lempar DataNotFoundException

        // Validasi nama
        if (updated.getNamaPemasok() == null || updated.getNamaPemasok().isBlank()) {
            throw new InvalidDataException("nama", null, "Nama pemasok wajib diisi");
        }

        if (updated.getNamaPemasok().length() > 20) {
            throw new InvalidDataException("nama", updated.getNamaPemasok(), "Nama pemasok maksimal 20 karakter");
        }

        // Validasi alamat
        if (updated.getAlamat() == null || updated.getAlamat().isBlank()) {
            throw new InvalidDataException("alamat", null, "Alamat wajib diisi");
        }

        if (updated.getAlamat().length() > 50) {
            throw new InvalidDataException("alamat", updated.getAlamat(), "Alamat maksimal 50 karakter");
        }

        // Validasi telepon
        if (updated.getTelepon() == null || updated.getTelepon().isBlank()) {
            throw new InvalidDataException("telepon", null, "Telepon wajib diisi");
        }

        if (updated.getTelepon().length() > 15) {
            throw new InvalidDataException("telepon", updated.getTelepon(), "Telepon maksimal 15 karakter");
        }

        // Validasi email (opsional, tapi kalau diisi harus valid)
        if (updated.getEmail() != null && !updated.getEmail().isBlank()) {
            if (updated.getEmail().length() > 40) {
                throw new InvalidDataException("email", updated.getEmail(), "Email maksimal 40 karakter");
            }

            if (!EMAIL_PATTERN.matcher(updated.getEmail()).matches()) {
                throw new InvalidDataException("email", updated.getEmail(), "Format email tidak valid");
            }
        }

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

        pemasokRepository.deleteAllById(ids);
    }

    public void delete(String id) {
        if (!pemasokRepository.existsById(id)) {
            throw new DataNotFoundException("Pemasok", id);
        }
        pemasokRepository.deleteById(id);
    }
}