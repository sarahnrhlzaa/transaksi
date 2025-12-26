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
        // validasi ID wajib diisi
        if (pelanggan.getIdPelanggan() == null || pelanggan.getIdPelanggan().isBlank()) {
            throw new InvalidDataException("idPelanggan", "wajib diisi");
        }

        // validasi ID sudah ada
        if (pelangganRepository.existsById(pelanggan.getIdPelanggan())) {
            throw new DataAlreadyExistsException("Pelanggan", pelanggan.getIdPelanggan());
        }

        // validasi field wajib dan business rules
        validatePelanggan(pelanggan);

        return pelangganRepository.save(pelanggan);
    }

    @Transactional
    public List<Pelanggan> saveBulk(List<Pelanggan> pelangganList) {
        for (Pelanggan pelanggan : pelangganList) {
            if (pelanggan.getIdPelanggan() == null || pelanggan.getIdPelanggan().isBlank()) {
                throw new InvalidDataException("idPelanggan", "wajib diisi untuk setiap pelanggan");
            }

            if (pelangganRepository.existsById(pelanggan.getIdPelanggan())) {
                throw new DataAlreadyExistsException("Pelanggan", pelanggan.getIdPelanggan());
            }

            validatePelanggan(pelanggan);
        }
        return pelangganRepository.saveAll(pelangganList);
    }

    // UPDATE
    public Pelanggan update(String id, Pelanggan updated) {
        Pelanggan existing = getById(id);

        validatePelanggan(updated);

        existing.setNama(updated.getNama());
        existing.setJenisKelamin(updated.getJenisKelamin());
        existing.setAlamat(updated.getAlamat());
        existing.setTelepon(updated.getTelepon());
        existing.setTglLahir(updated.getTglLahir());
        existing.setJenisPelanggan(updated.getJenisPelanggan());

        return pelangganRepository.save(existing);
    }

    // DELETE
    public void delete(String id) {
        if (!pelangganRepository.existsById(id)) {
            throw new DataNotFoundException("Pelanggan", id);
        }
        pelangganRepository.deleteById(id);
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
        long existingCount = pelangganRepository.countByIdPelangganIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        pelangganRepository.deleteAllById(ids);
    }

    // HELPER: validasi pelanggan
    private void validatePelanggan(Pelanggan pelanggan) {
        // validasi field wajib
        if (pelanggan.getNama() == null || pelanggan.getNama().isBlank()) {
            throw new InvalidDataException("nama", "wajib diisi");
        }
        if (pelanggan.getAlamat() == null || pelanggan.getAlamat().isBlank()) {
            throw new InvalidDataException("alamat", "wajib diisi");
        }
        if (pelanggan.getTglLahir() == null) {
            throw new InvalidDataException("tglLahir", "wajib diisi");
        }

        // validasi jenis kelamin
        if (pelanggan.getJenisKelamin() == null || pelanggan.getJenisKelamin().isBlank()) {
            pelanggan.setJenisKelamin("L"); // default
        } else {
            String jk = pelanggan.getJenisKelamin().toUpperCase();
            if (!jk.equals("L") && !jk.equals("P")) {
                throw new InvalidDataException("jenisKelamin", "harus 'L' atau 'P'");
            }
            pelanggan.setJenisKelamin(jk);
        }

        // validasi jenis pelanggan (S = Silver, G = Gold, P = Platinum, atau yang lain sesuai bisnis)
        if (pelanggan.getJenisPelanggan() == null || pelanggan.getJenisPelanggan().isBlank()) {
            pelanggan.setJenisPelanggan("S"); // default Silver
        } else {
            String jp = pelanggan.getJenisPelanggan().toUpperCase();
            // Sesuaikan dengan business rule: S=Silver, G=Gold, P=Platinum
            if (!jp.equals("S") && !jp.equals("G") && !jp.equals("P")) {
                throw new InvalidDataException("jenisPelanggan", "harus 'S' (Silver), 'G' (Gold), atau 'P' (Platinum)");
            }
            pelanggan.setJenisPelanggan(jp);
        }

        // validasi tanggal lahir (tidak boleh di masa depan)
        if (pelanggan.getTglLahir().isAfter(LocalDate.now())) {
            throw new InvalidDataException("tglLahir", "tidak boleh di masa depan");
        }

        // validasi umur minimal (misalnya 13 tahun untuk bisa jadi pelanggan)
        LocalDate minBirthDate = LocalDate.now().minusYears(13);
        if (pelanggan.getTglLahir().isAfter(minBirthDate)) {
            throw new InvalidDataException("tglLahir", "pelanggan harus berumur minimal 13 tahun");
        }
    }
}