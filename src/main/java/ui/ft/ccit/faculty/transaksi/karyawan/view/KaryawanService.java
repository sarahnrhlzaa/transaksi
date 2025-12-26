package ui.ft.ccit.faculty.transaksi.karyawan.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.InvalidDataException;
import ui.ft.ccit.faculty.transaksi.karyawan.model.Karyawan;
import ui.ft.ccit.faculty.transaksi.karyawan.model.KaryawanRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class KaryawanService {

    private final KaryawanRepository karyawanRepository;

    public KaryawanService(KaryawanRepository karyawanRepository) {
        this.karyawanRepository = karyawanRepository;
    }

    public List<Karyawan> getAll() {
        return karyawanRepository.findAll();
    }

    public List<Karyawan> getAllWithPagination(int page, int size) {
        return karyawanRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public Karyawan getById(String id) {
        return karyawanRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Karyawan", id));
    }

    public List<Karyawan> searchByNama(String keyword) {
        return karyawanRepository.findByNamaContainingIgnoreCase(keyword);
    }

    // CREATE
    public Karyawan save(Karyawan karyawan) {
        // validasi ID wajib diisi
        if (karyawan.getIdKaryawan() == null || karyawan.getIdKaryawan().isBlank()) {
            throw new InvalidDataException("idKaryawan", "wajib diisi");
        }

        // validasi ID sudah ada
        if (karyawanRepository.existsById(karyawan.getIdKaryawan())) {
            throw new DataAlreadyExistsException("Karyawan", karyawan.getIdKaryawan());
        }

        // validasi field wajib dan business rules
        validateKaryawan(karyawan);

        return karyawanRepository.save(karyawan);
    }

    @Transactional
    public List<Karyawan> saveBulk(List<Karyawan> karyawanList) {
        for (Karyawan karyawan : karyawanList) {
            if (karyawan.getIdKaryawan() == null || karyawan.getIdKaryawan().isBlank()) {
                throw new InvalidDataException("idKaryawan", "wajib diisi untuk setiap karyawan");
            }

            if (karyawanRepository.existsById(karyawan.getIdKaryawan())) {
                throw new DataAlreadyExistsException("Karyawan", karyawan.getIdKaryawan());
            }

            validateKaryawan(karyawan);
        }
        return karyawanRepository.saveAll(karyawanList);
    }

    // UPDATE
    public Karyawan update(String id, Karyawan updated) {
        Karyawan existing = getById(id);

        validateKaryawan(updated);

        existing.setNama(updated.getNama());
        existing.setJenisKelamin(updated.getJenisKelamin());
        existing.setAlamat(updated.getAlamat());
        existing.setTelepon(updated.getTelepon());
        existing.setTglLahir(updated.getTglLahir());
        existing.setGaji(updated.getGaji());

        return karyawanRepository.save(existing);
    }

    // DELETE
    public void delete(String id) {
        if (!karyawanRepository.existsById(id)) {
            throw new DataNotFoundException("Karyawan", id);
        }
        karyawanRepository.deleteById(id);
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
        long existingCount = karyawanRepository.countByIdKaryawanIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        karyawanRepository.deleteAllById(ids);
    }

    // HELPER: validasi karyawan
    private void validateKaryawan(Karyawan karyawan) {
        // validasi field wajib
        if (karyawan.getNama() == null || karyawan.getNama().isBlank()) {
            throw new InvalidDataException("nama", "wajib diisi");
        }
        if (karyawan.getAlamat() == null || karyawan.getAlamat().isBlank()) {
            throw new InvalidDataException("alamat", "wajib diisi");
        }
        if (karyawan.getTglLahir() == null) {
            throw new InvalidDataException("tglLahir", "wajib diisi");
        }
        if (karyawan.getGaji() == null) {
            throw new InvalidDataException("gaji", "wajib diisi");
        }

        // validasi jenis kelamin
        if (karyawan.getJenisKelamin() == null || karyawan.getJenisKelamin().isBlank()) {
            karyawan.setJenisKelamin("L"); // default
        } else {
            String jk = karyawan.getJenisKelamin().toUpperCase();
            if (!jk.equals("L") && !jk.equals("P")) {
                throw new InvalidDataException("jenisKelamin", "harus 'L' atau 'P'");
            }
            karyawan.setJenisKelamin(jk);
        }

        // validasi gaji
        if (karyawan.getGaji() <= 0) {
            throw new InvalidDataException("gaji", "harus lebih besar dari 0");
        }

        // validasi tanggal lahir (tidak boleh di masa depan)
        if (karyawan.getTglLahir().isAfter(LocalDate.now())) {
            throw new InvalidDataException("tglLahir", "tidak boleh di masa depan");
        }

        // validasi umur minimal (misalnya 17 tahun)
        LocalDate minBirthDate = LocalDate.now().minusYears(17);
        if (karyawan.getTglLahir().isAfter(minBirthDate)) {
            throw new InvalidDataException("tglLahir", "karyawan harus berumur minimal 17 tahun");
        }
    }
}