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
        validateKaryawan(karyawan);

        if (karyawanRepository.existsById(karyawan.getIdKaryawan())) {
            throw new DataAlreadyExistsException("Karyawan", karyawan.getIdKaryawan());
        }

        return karyawanRepository.save(karyawan);
    }

    @Transactional
    public List<Karyawan> saveBulk(List<Karyawan> karyawanList) {
        for (Karyawan karyawan : karyawanList) {
            validateKaryawan(karyawan);

            if (karyawanRepository.existsById(karyawan.getIdKaryawan())) {
                throw new DataAlreadyExistsException("Karyawan", karyawan.getIdKaryawan());
            }
        }
        return karyawanRepository.saveAll(karyawanList);
    }

    // UPDATE
    public Karyawan update(String id, Karyawan updated) {
        Karyawan existing = getById(id); // akan lempar DataNotFoundException

        // Validasi data yang akan diupdate (skip validasi idKaryawan karena tidak berubah)
        validateKaryawanForUpdate(updated);

        existing.setNama(updated.getNama());
        existing.setJenisKelamin(updated.getJenisKelamin());
        existing.setAlamat(updated.getAlamat());
        existing.setTelepon(updated.getTelepon());
        existing.setTglLahir(updated.getTglLahir());
        existing.setGaji(updated.getGaji());

        return karyawanRepository.save(existing);
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
        long existingCount = karyawanRepository.countByIdKaryawanIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        karyawanRepository.deleteAllById(ids);
    }

    public void delete(String id) {
        if (!karyawanRepository.existsById(id)) {
            throw new DataNotFoundException("Karyawan", id);
        }
        karyawanRepository.deleteById(id);
    }

    // VALIDATION METHODS
    private void validateKaryawan(Karyawan karyawan) {
        // Validasi idKaryawan
        if (karyawan.getIdKaryawan() == null || karyawan.getIdKaryawan().isBlank()) {
            throw new InvalidDataException("Karyawan", "idKaryawan", "null/blank");
        }
        if (karyawan.getIdKaryawan().length() != 4) {
            throw new InvalidDataException("Karyawan", "idKaryawan",
                    karyawan.getIdKaryawan() + " (harus 4 karakter)");
        }

        // Validasi nama
        if (karyawan.getNama() == null || karyawan.getNama().isBlank()) {
            throw new InvalidDataException("Karyawan", "nama", "null/blank");
        }
        if (karyawan.getNama().length() > 20) {
            throw new InvalidDataException("Karyawan", "nama",
                    karyawan.getNama() + " (maksimal 20 karakter)");
        }

        // Validasi jenis kelamin
        if (karyawan.getJenisKelamin() == null || karyawan.getJenisKelamin().isBlank()) {
            karyawan.setJenisKelamin("L"); // Set default
        } else {
            String jk = karyawan.getJenisKelamin().toUpperCase();
            if (!jk.equals("L") && !jk.equals("P")) {
                throw new InvalidDataException("Karyawan", "jenisKelamin",
                        jk + " (harus 'L' atau 'P')");
            }
            karyawan.setJenisKelamin(jk);
        }

        // Validasi alamat
        if (karyawan.getAlamat() == null || karyawan.getAlamat().isBlank()) {
            throw new InvalidDataException("Karyawan", "alamat", "null/blank");
        }
        if (karyawan.getAlamat().length() > 50) {
            throw new InvalidDataException("Karyawan", "alamat",
                    "terlalu panjang (maksimal 50 karakter)");
        }

        // Validasi telepon (optional)
        if (karyawan.getTelepon() != null && !karyawan.getTelepon().isBlank()) {
            if (karyawan.getTelepon().length() > 15) {
                throw new InvalidDataException("Karyawan", "telepon",
                        karyawan.getTelepon() + " (maksimal 15 karakter)");
            }
            // Validasi format telepon (hanya angka, +, -, spasi, dan kurung)
            if (!karyawan.getTelepon().matches("[0-9+\\-\\s()]+")) {
                throw new InvalidDataException("Karyawan", "telepon",
                        karyawan.getTelepon() + " (format tidak valid)");
            }
        }

        // Validasi tanggal lahir
        if (karyawan.getTglLahir() == null) {
            throw new InvalidDataException("Karyawan", "tglLahir", "null");
        }
        // Validasi umur minimal 17 tahun
        LocalDate minDate = LocalDate.now().minusYears(17);
        if (karyawan.getTglLahir().isAfter(minDate)) {
            throw new InvalidDataException("Karyawan", "tglLahir",
                    karyawan.getTglLahir().toString() + " (umur minimal 17 tahun)");
        }
        // Validasi tanggal lahir tidak di masa depan
        if (karyawan.getTglLahir().isAfter(LocalDate.now())) {
            throw new InvalidDataException("Karyawan", "tglLahir",
                    karyawan.getTglLahir().toString() + " (tidak boleh di masa depan)");
        }
        // Validasi umur maksimal 100 tahun
        LocalDate maxDate = LocalDate.now().minusYears(100);
        if (karyawan.getTglLahir().isBefore(maxDate)) {
            throw new InvalidDataException("Karyawan", "tglLahir",
                    karyawan.getTglLahir().toString() + " (umur maksimal 100 tahun)");
        }

        // Validasi gaji
        if (karyawan.getGaji() == null) {
            throw new InvalidDataException("Karyawan", "gaji", "null");
        }
        if (karyawan.getGaji() < 0) {
            throw new InvalidDataException("Karyawan", "gaji",
                    String.valueOf(karyawan.getGaji()) + " (tidak boleh negatif)");
        }
        // Validasi gaji minimal (contoh: UMR minimal)
        if (karyawan.getGaji() < 1000000) {
            throw new InvalidDataException("Karyawan", "gaji",
                    String.valueOf(karyawan.getGaji()) + " (minimal Rp 1.000.000)");
        }
    }

    private void validateKaryawanForUpdate(Karyawan karyawan) {
        // Validasi nama
        if (karyawan.getNama() == null || karyawan.getNama().isBlank()) {
            throw new InvalidDataException("Karyawan", "nama", "null/blank");
        }
        if (karyawan.getNama().length() > 20) {
            throw new InvalidDataException("Karyawan", "nama",
                    karyawan.getNama() + " (maksimal 20 karakter)");
        }

        // Validasi jenis kelamin
        if (karyawan.getJenisKelamin() == null || karyawan.getJenisKelamin().isBlank()) {
            throw new InvalidDataException("Karyawan", "jenisKelamin", "null/blank");
        } else {
            String jk = karyawan.getJenisKelamin().toUpperCase();
            if (!jk.equals("L") && !jk.equals("P")) {
                throw new InvalidDataException("Karyawan", "jenisKelamin",
                        jk + " (harus 'L' atau 'P')");
            }
        }

        // Validasi alamat
        if (karyawan.getAlamat() == null || karyawan.getAlamat().isBlank()) {
            throw new InvalidDataException("Karyawan", "alamat", "null/blank");
        }
        if (karyawan.getAlamat().length() > 50) {
            throw new InvalidDataException("Karyawan", "alamat",
                    "terlalu panjang (maksimal 50 karakter)");
        }

        // Validasi telepon (optional)
        if (karyawan.getTelepon() != null && !karyawan.getTelepon().isBlank()) {
            if (karyawan.getTelepon().length() > 15) {
                throw new InvalidDataException("Karyawan", "telepon",
                        karyawan.getTelepon() + " (maksimal 15 karakter)");
            }
            // Validasi format telepon
            if (!karyawan.getTelepon().matches("[0-9+\\-\\s()]+")) {
                throw new InvalidDataException("Karyawan", "telepon",
                        karyawan.getTelepon() + " (format tidak valid)");
            }
        }

        // Validasi tanggal lahir
        if (karyawan.getTglLahir() == null) {
            throw new InvalidDataException("Karyawan", "tglLahir", "null");
        }
        // Validasi umur minimal 17 tahun
        LocalDate minDate = LocalDate.now().minusYears(17);
        if (karyawan.getTglLahir().isAfter(minDate)) {
            throw new InvalidDataException("Karyawan", "tglLahir",
                    karyawan.getTglLahir().toString() + " (umur minimal 17 tahun)");
        }
        // Validasi tanggal lahir tidak di masa depan
        if (karyawan.getTglLahir().isAfter(LocalDate.now())) {
            throw new InvalidDataException("Karyawan", "tglLahir",
                    karyawan.getTglLahir().toString() + " (tidak boleh di masa depan)");
        }
        // Validasi umur maksimal 100 tahun
        LocalDate maxDate = LocalDate.now().minusYears(100);
        if (karyawan.getTglLahir().isBefore(maxDate)) {
            throw new InvalidDataException("Karyawan", "tglLahir",
                    karyawan.getTglLahir().toString() + " (umur maksimal 100 tahun)");
        }

        // Validasi gaji
        if (karyawan.getGaji() == null) {
            throw new InvalidDataException("Karyawan", "gaji", "null");
        }
        if (karyawan.getGaji() < 0) {
            throw new InvalidDataException("Karyawan", "gaji",
                    String.valueOf(karyawan.getGaji()) + " (tidak boleh negatif)");
        }
        // Validasi gaji minimal
        if (karyawan.getGaji() < 1000000) {
            throw new InvalidDataException("Karyawan", "gaji",
                    String.valueOf(karyawan.getGaji()) + " (minimal Rp 1.000.000)");
        }
    }
}