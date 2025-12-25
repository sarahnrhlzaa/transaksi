package ui.ft.ccit.faculty.transaksi.pelanggan.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.InvalidDataException;
import ui.ft.ccit.faculty.transaksi.pelanggan.model.Pelanggan;
import ui.ft.ccit.faculty.transaksi.pelanggan.model.PelangganRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PelangganService {

    private final PelangganRepository pelangganRepository;

    public PelangganService(PelangganRepository pelangganRepository) {
        this.pelangganRepository = pelangganRepository;
    }

    public List<Pelanggan> getAll() {
        return pelangganRepository.findAll();
    }

    public List<Pelanggan> getAllWithPagination(int page, int size) {
        return pelangganRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public Pelanggan getById(String id) {
        return pelangganRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Pelanggan", id));
    }

    // CREATE
    public Pelanggan save(Pelanggan pelanggan) {
        // Validasi idPelanggan
        if (pelanggan.getIdPelanggan() == null || pelanggan.getIdPelanggan().isBlank()) {
            throw new InvalidDataException("idPelanggan", null, "ID pelanggan wajib diisi");
        }

        if (pelanggan.getIdPelanggan().length() != 4) {
            throw new InvalidDataException("idPelanggan", pelanggan.getIdPelanggan(), "ID pelanggan harus 4 karakter");
        }

        // Validasi nama
        if (pelanggan.getNama() == null || pelanggan.getNama().isBlank()) {
            throw new InvalidDataException("nama", null, "Nama pelanggan wajib diisi");
        }

        if (pelanggan.getNama().length() > 20) {
            throw new InvalidDataException("nama", pelanggan.getNama(), "Nama pelanggan maksimal 20 karakter");
        }

        // Validasi jenis kelamin
        if (pelanggan.getJenisKelamin() == null || pelanggan.getJenisKelamin().isBlank()) {
            throw new InvalidDataException("jenisKelamin", null, "Jenis kelamin wajib diisi");
        }

        if (!pelanggan.getJenisKelamin().equals("L") && !pelanggan.getJenisKelamin().equals("P")) {
            throw new InvalidDataException("jenisKelamin", pelanggan.getJenisKelamin(), "Jenis kelamin harus 'L' atau 'P'");
        }

        // Validasi alamat
        if (pelanggan.getAlamat() == null || pelanggan.getAlamat().isBlank()) {
            throw new InvalidDataException("alamat", null, "Alamat wajib diisi");
        }

        if (pelanggan.getAlamat().length() > 50) {
            throw new InvalidDataException("alamat", pelanggan.getAlamat(), "Alamat maksimal 50 karakter");
        }

        // Validasi telepon (opsional, tapi kalau diisi harus valid)
        if (pelanggan.getTelepon() != null && !pelanggan.getTelepon().isBlank()) {
            if (pelanggan.getTelepon().length() > 15) {
                throw new InvalidDataException("telepon", pelanggan.getTelepon(), "Telepon maksimal 15 karakter");
            }
        }

        // Validasi tgl_lahir
        if (pelanggan.getTglLahir() == null) {
            throw new InvalidDataException("tglLahir", null, "Tanggal lahir wajib diisi");
        }

        if (pelanggan.getTglLahir().isAfter(LocalDate.now())) {
            throw new InvalidDataException("tglLahir", pelanggan.getTglLahir(), "Tanggal lahir tidak boleh di masa depan");
        }

        // Validasi jenis pelanggan
        if (pelanggan.getJenisPelanggan() == null || pelanggan.getJenisPelanggan().isBlank()) {
            throw new InvalidDataException("jenisPelanggan", null, "Jenis pelanggan wajib diisi");
        }

        if (!pelanggan.getJenisPelanggan().equals("S") && !pelanggan.getJenisPelanggan().equals("M")) {
            throw new InvalidDataException("jenisPelanggan", pelanggan.getJenisPelanggan(), "Jenis pelanggan harus 'S' (Silver) atau 'M' (Member)");
        }

        if (pelangganRepository.existsById(pelanggan.getIdPelanggan())) {
            throw new DataAlreadyExistsException("Pelanggan", pelanggan.getIdPelanggan());
        }

        return pelangganRepository.save(pelanggan);
    }

    @Transactional
    public List<Pelanggan> saveBulk(List<Pelanggan> pelangganList) {
        for (Pelanggan pelanggan : pelangganList) {
            // Validasi idPelanggan
            if (pelanggan.getIdPelanggan() == null || pelanggan.getIdPelanggan().isBlank()) {
                throw new InvalidDataException("idPelanggan", null, "ID pelanggan wajib diisi untuk setiap data");
            }

            if (pelanggan.getIdPelanggan().length() != 4) {
                throw new InvalidDataException("idPelanggan", pelanggan.getIdPelanggan(), "ID pelanggan harus 4 karakter");
            }

            // Validasi nama
            if (pelanggan.getNama() == null || pelanggan.getNama().isBlank()) {
                throw new InvalidDataException("nama", null, "Nama pelanggan wajib diisi untuk setiap data");
            }

            if (pelanggan.getNama().length() > 20) {
                throw new InvalidDataException("nama", pelanggan.getNama(), "Nama pelanggan maksimal 20 karakter");
            }

            // Validasi jenis kelamin
            if (pelanggan.getJenisKelamin() == null || pelanggan.getJenisKelamin().isBlank()) {
                throw new InvalidDataException("jenisKelamin", null, "Jenis kelamin wajib diisi untuk setiap data");
            }

            if (!pelanggan.getJenisKelamin().equals("L") && !pelanggan.getJenisKelamin().equals("P")) {
                throw new InvalidDataException("jenisKelamin", pelanggan.getJenisKelamin(), "Jenis kelamin harus 'L' atau 'P'");
            }

            // Validasi alamat
            if (pelanggan.getAlamat() == null || pelanggan.getAlamat().isBlank()) {
                throw new InvalidDataException("alamat", null, "Alamat wajib diisi untuk setiap data");
            }

            if (pelanggan.getAlamat().length() > 50) {
                throw new InvalidDataException("alamat", pelanggan.getAlamat(), "Alamat maksimal 50 karakter");
            }

            // Validasi telepon (opsional)
            if (pelanggan.getTelepon() != null && !pelanggan.getTelepon().isBlank()) {
                if (pelanggan.getTelepon().length() > 15) {
                    throw new InvalidDataException("telepon", pelanggan.getTelepon(), "Telepon maksimal 15 karakter");
                }
            }

            // Validasi tgl_lahir
            if (pelanggan.getTglLahir() == null) {
                throw new InvalidDataException("tglLahir", null, "Tanggal lahir wajib diisi untuk setiap data");
            }

            if (pelanggan.getTglLahir().isAfter(LocalDate.now())) {
                throw new InvalidDataException("tglLahir", pelanggan.getTglLahir(), "Tanggal lahir tidak boleh di masa depan");
            }

            // Validasi jenis pelanggan
            if (pelanggan.getJenisPelanggan() == null || pelanggan.getJenisPelanggan().isBlank()) {
                throw new InvalidDataException("jenisPelanggan", null, "Jenis pelanggan wajib diisi untuk setiap data");
            }

            if (!pelanggan.getJenisPelanggan().equals("S") && !pelanggan.getJenisPelanggan().equals("M")) {
                throw new InvalidDataException("jenisPelanggan", pelanggan.getJenisPelanggan(), "Jenis pelanggan harus 'S' (Silver) atau 'M' (Member)");
            }

            if (pelangganRepository.existsById(pelanggan.getIdPelanggan())) {
                throw new DataAlreadyExistsException("Pelanggan", pelanggan.getIdPelanggan());
            }
        }
        return pelangganRepository.saveAll(pelangganList);
    }

    // UPDATE
    public Pelanggan update(String id, Pelanggan updated) {
        Pelanggan existing = getById(id); // akan lempar DataNotFoundException

        // Validasi nama
        if (updated.getNama() == null || updated.getNama().isBlank()) {
            throw new InvalidDataException("nama", null, "Nama pelanggan wajib diisi");
        }

        if (updated.getNama().length() > 20) {
            throw new InvalidDataException("nama", updated.getNama(), "Nama pelanggan maksimal 20 karakter");
        }

        // Validasi jenis kelamin
        if (updated.getJenisKelamin() == null || updated.getJenisKelamin().isBlank()) {
            throw new InvalidDataException("jenisKelamin", null, "Jenis kelamin wajib diisi");
        }

        if (!updated.getJenisKelamin().equals("L") && !updated.getJenisKelamin().equals("P")) {
            throw new InvalidDataException("jenisKelamin", updated.getJenisKelamin(), "Jenis kelamin harus 'L' atau 'P'");
        }

        // Validasi alamat
        if (updated.getAlamat() == null || updated.getAlamat().isBlank()) {
            throw new InvalidDataException("alamat", null, "Alamat wajib diisi");
        }

        if (updated.getAlamat().length() > 50) {
            throw new InvalidDataException("alamat", updated.getAlamat(), "Alamat maksimal 50 karakter");
        }

        // Validasi telepon (opsional)
        if (updated.getTelepon() != null && !updated.getTelepon().isBlank()) {
            if (updated.getTelepon().length() > 15) {
                throw new InvalidDataException("telepon", updated.getTelepon(), "Telepon maksimal 15 karakter");
            }
        }

        // Validasi tgl_lahir
        if (updated.getTglLahir() == null) {
            throw new InvalidDataException("tglLahir", null, "Tanggal lahir wajib diisi");
        }

        if (updated.getTglLahir().isAfter(LocalDate.now())) {
            throw new InvalidDataException("tglLahir", updated.getTglLahir(), "Tanggal lahir tidak boleh di masa depan");
        }

        // Validasi jenis pelanggan
        if (updated.getJenisPelanggan() == null || updated.getJenisPelanggan().isBlank()) {
            throw new InvalidDataException("jenisPelanggan", null, "Jenis pelanggan wajib diisi");
        }

        if (!updated.getJenisPelanggan().equals("S") && !updated.getJenisPelanggan().equals("M")) {
            throw new InvalidDataException("jenisPelanggan", updated.getJenisPelanggan(), "Jenis pelanggan harus 'S' (Silver) atau 'M' (Member)");
        }

        existing.setNama(updated.getNama());
        existing.setJenisKelamin(updated.getJenisKelamin());
        existing.setAlamat(updated.getAlamat());
        existing.setTelepon(updated.getTelepon());
        existing.setTglLahir(updated.getTglLahir());
        existing.setJenisPelanggan(updated.getJenisPelanggan());

        return pelangganRepository.save(existing);
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

        pelangganRepository.deleteAllById(ids);
    }

    public void delete(String id) {
        if (!pelangganRepository.existsById(id)) {
            throw new DataNotFoundException("Pelanggan", id);
        }
        pelangganRepository.deleteById(id);
    }
}