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

    public List<Pelanggan> searchByNama(String keyword) {
        return pelangganRepository.findByNamaContainingIgnoreCase(keyword);
    }

    // CREATE
    public Pelanggan save(Pelanggan pelanggan) {
        validatePelanggan(pelanggan);

        if (pelangganRepository.existsById(pelanggan.getIdPelanggan())) {
            throw new DataAlreadyExistsException("Pelanggan", pelanggan.getIdPelanggan());
        }

        return pelangganRepository.save(pelanggan);
    }

    @Transactional
    public List<Pelanggan> saveBulk(List<Pelanggan> pelangganList) {
        for (Pelanggan pelanggan : pelangganList) {
            validatePelanggan(pelanggan);

            if (pelangganRepository.existsById(pelanggan.getIdPelanggan())) {
                throw new DataAlreadyExistsException("Pelanggan", pelanggan.getIdPelanggan());
            }
        }
        return pelangganRepository.saveAll(pelangganList);
    }

    // UPDATE
    public Pelanggan update(String id, Pelanggan updated) {
        Pelanggan existing = getById(id); // akan lempar DataNotFoundException

        // Validasi data yang akan diupdate (skip validasi idPelanggan karena tidak berubah)
        validatePelangganForUpdate(updated);

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

        // validasi: pastikan semua ID ada
        long existingCount = pelangganRepository.countByIdPelangganIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        pelangganRepository.deleteAllById(ids);
    }

    public void delete(String id) {
        if (!pelangganRepository.existsById(id)) {
            throw new DataNotFoundException("Pelanggan", id);
        }
        pelangganRepository.deleteById(id);
    }

    // VALIDATION METHODS
    private void validatePelanggan(Pelanggan pelanggan) {
        // Validasi idPelanggan
        if (pelanggan.getIdPelanggan() == null || pelanggan.getIdPelanggan().isBlank()) {
            throw new InvalidDataException("Pelanggan", "idPelanggan", "null/blank");
        }
        if (pelanggan.getIdPelanggan().length() != 4) {
            throw new InvalidDataException("Pelanggan", "idPelanggan",
                    pelanggan.getIdPelanggan() + " (harus 4 karakter)");
        }

        // Validasi nama
        if (pelanggan.getNama() == null || pelanggan.getNama().isBlank()) {
            throw new InvalidDataException("Pelanggan", "nama", "null/blank");
        }
        if (pelanggan.getNama().length() > 20) {
            throw new InvalidDataException("Pelanggan", "nama",
                    pelanggan.getNama() + " (maksimal 20 karakter)");
        }

        // Validasi jenis kelamin
        if (pelanggan.getJenisKelamin() == null || pelanggan.getJenisKelamin().isBlank()) {
            pelanggan.setJenisKelamin("L"); // Set default
        } else {
            String jk = pelanggan.getJenisKelamin().toUpperCase();
            if (!jk.equals("L") && !jk.equals("P")) {
                throw new InvalidDataException("Pelanggan", "jenisKelamin",
                        jk + " (harus 'L' atau 'P')");
            }
            pelanggan.setJenisKelamin(jk);
        }

        // Validasi alamat
        if (pelanggan.getAlamat() == null || pelanggan.getAlamat().isBlank()) {
            throw new InvalidDataException("Pelanggan", "alamat", "null/blank");
        }
        if (pelanggan.getAlamat().length() > 50) {
            throw new InvalidDataException("Pelanggan", "alamat",
                    "terlalu panjang (maksimal 50 karakter)");
        }

        // Validasi telepon (optional)
        if (pelanggan.getTelepon() != null && !pelanggan.getTelepon().isBlank()) {
            if (pelanggan.getTelepon().length() > 15) {
                throw new InvalidDataException("Pelanggan", "telepon",
                        pelanggan.getTelepon() + " (maksimal 15 karakter)");
            }
            // Validasi format telepon (hanya angka, +, -, spasi, dan kurung)
            if (!pelanggan.getTelepon().matches("[0-9+\\-\\s()]+")) {
                throw new InvalidDataException("Pelanggan", "telepon",
                        pelanggan.getTelepon() + " (format tidak valid)");
            }
        }

        // Validasi tanggal lahir
        if (pelanggan.getTglLahir() == null) {
            throw new InvalidDataException("Pelanggan", "tglLahir", "null");
        }
        // Validasi tanggal lahir tidak di masa depan
        if (pelanggan.getTglLahir().isAfter(LocalDate.now())) {
            throw new InvalidDataException("Pelanggan", "tglLahir",
                    pelanggan.getTglLahir().toString() + " (tidak boleh di masa depan)");
        }
        // Validasi umur maksimal 120 tahun (lebih toleran untuk pelanggan)
        LocalDate maxDate = LocalDate.now().minusYears(120);
        if (pelanggan.getTglLahir().isBefore(maxDate)) {
            throw new InvalidDataException("Pelanggan", "tglLahir",
                    pelanggan.getTglLahir().toString() + " (umur maksimal 120 tahun)");
        }

        // Validasi jenis pelanggan
        if (pelanggan.getJenisPelanggan() == null || pelanggan.getJenisPelanggan().isBlank()) {
            pelanggan.setJenisPelanggan("S"); // Set default 'S' untuk Silver
        } else {
            String jp = pelanggan.getJenisPelanggan().toUpperCase();
            // Asumsikan jenis pelanggan: S (Silver), G (Gold), P (Platinum)
            if (!jp.equals("S") && !jp.equals("G") && !jp.equals("P")) {
                throw new InvalidDataException("Pelanggan", "jenisPelanggan",
                        jp + " (harus 'S', 'G', atau 'P')");
            }
            pelanggan.setJenisPelanggan(jp);
        }
    }

    private void validatePelangganForUpdate(Pelanggan pelanggan) {
        // Validasi nama
        if (pelanggan.getNama() == null || pelanggan.getNama().isBlank()) {
            throw new InvalidDataException("Pelanggan", "nama", "null/blank");
        }
        if (pelanggan.getNama().length() > 20) {
            throw new InvalidDataException("Pelanggan", "nama",
                    pelanggan.getNama() + " (maksimal 20 karakter)");
        }

        // Validasi jenis kelamin
        if (pelanggan.getJenisKelamin() == null || pelanggan.getJenisKelamin().isBlank()) {
            throw new InvalidDataException("Pelanggan", "jenisKelamin", "null/blank");
        } else {
            String jk = pelanggan.getJenisKelamin().toUpperCase();
            if (!jk.equals("L") && !jk.equals("P")) {
                throw new InvalidDataException("Pelanggan", "jenisKelamin",
                        jk + " (harus 'L' atau 'P')");
            }
        }

        // Validasi alamat
        if (pelanggan.getAlamat() == null || pelanggan.getAlamat().isBlank()) {
            throw new InvalidDataException("Pelanggan", "alamat", "null/blank");
        }
        if (pelanggan.getAlamat().length() > 50) {
            throw new InvalidDataException("Pelanggan", "alamat",
                    "terlalu panjang (maksimal 50 karakter)");
        }

        // Validasi telepon (optional)
        if (pelanggan.getTelepon() != null && !pelanggan.getTelepon().isBlank()) {
            if (pelanggan.getTelepon().length() > 15) {
                throw new InvalidDataException("Pelanggan", "telepon",
                        pelanggan.getTelepon() + " (maksimal 15 karakter)");
            }
            // Validasi format telepon
            if (!pelanggan.getTelepon().matches("[0-9+\\-\\s()]+")) {
                throw new InvalidDataException("Pelanggan", "telepon",
                        pelanggan.getTelepon() + " (format tidak valid)");
            }
        }

        // Validasi tanggal lahir
        if (pelanggan.getTglLahir() == null) {
            throw new InvalidDataException("Pelanggan", "tglLahir", "null");
        }
        // Validasi tanggal lahir tidak di masa depan
        if (pelanggan.getTglLahir().isAfter(LocalDate.now())) {
            throw new InvalidDataException("Pelanggan", "tglLahir",
                    pelanggan.getTglLahir().toString() + " (tidak boleh di masa depan)");
        }
        // Validasi umur maksimal 120 tahun
        LocalDate maxDate = LocalDate.now().minusYears(120);
        if (pelanggan.getTglLahir().isBefore(maxDate)) {
            throw new InvalidDataException("Pelanggan", "tglLahir",
                    pelanggan.getTglLahir().toString() + " (umur maksimal 120 tahun)");
        }

        // Validasi jenis pelanggan
        if (pelanggan.getJenisPelanggan() == null || pelanggan.getJenisPelanggan().isBlank()) {
            throw new InvalidDataException("Pelanggan", "jenisPelanggan", "null/blank");
        } else {
            String jp = pelanggan.getJenisPelanggan().toUpperCase();
            // Asumsikan jenis pelanggan: S (Silver), G (Gold), P (Platinum)
            if (!jp.equals("S") && !jp.equals("G") && !jp.equals("P")) {
                throw new InvalidDataException("Pelanggan", "jenisPelanggan",
                        jp + " (harus 'S', 'G', atau 'P')");
            }
        }
    }
}