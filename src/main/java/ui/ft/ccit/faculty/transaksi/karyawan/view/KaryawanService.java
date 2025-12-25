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

    // CREATE
    public Karyawan save(Karyawan karyawan) {
        // Validasi idKaryawan
        if (karyawan.getIdKaryawan() == null || karyawan.getIdKaryawan().isBlank()) {
            throw new InvalidDataException("idKaryawan", null, "ID karyawan wajib diisi");
        }

        if (karyawan.getIdKaryawan().length() != 4) {
            throw new InvalidDataException("idKaryawan", karyawan.getIdKaryawan(), "ID karyawan harus 4 karakter");
        }

        // Validasi nama
        if (karyawan.getNama() == null || karyawan.getNama().isBlank()) {
            throw new InvalidDataException("nama", null, "Nama karyawan wajib diisi");
        }

        if (karyawan.getNama().length() > 20) {
            throw new InvalidDataException("nama", karyawan.getNama(), "Nama karyawan maksimal 20 karakter");
        }

        // Validasi jenis kelamin
        if (karyawan.getJenisKelamin() == null || karyawan.getJenisKelamin().isBlank()) {
            throw new InvalidDataException("jenisKelamin", null, "Jenis kelamin wajib diisi");
        }

        if (!karyawan.getJenisKelamin().equals("L") && !karyawan.getJenisKelamin().equals("P")) {
            throw new InvalidDataException("jenisKelamin", karyawan.getJenisKelamin(), "Jenis kelamin harus 'L' atau 'P'");
        }

        // Validasi alamat
        if (karyawan.getAlamat() == null || karyawan.getAlamat().isBlank()) {
            throw new InvalidDataException("alamat", null, "Alamat wajib diisi");
        }

        if (karyawan.getAlamat().length() > 50) {
            throw new InvalidDataException("alamat", karyawan.getAlamat(), "Alamat maksimal 50 karakter");
        }

        // Validasi telepon (opsional, tapi kalau diisi harus valid)
        if (karyawan.getTelepon() != null && !karyawan.getTelepon().isBlank()) {
            if (karyawan.getTelepon().length() > 15) {
                throw new InvalidDataException("telepon", karyawan.getTelepon(), "Telepon maksimal 15 karakter");
            }
        }

        // Validasi tgl_lahir
        if (karyawan.getTglLahir() == null) {
            throw new InvalidDataException("tglLahir", null, "Tanggal lahir wajib diisi");
        }

        if (karyawan.getTglLahir().isAfter(LocalDate.now())) {
            throw new InvalidDataException("tglLahir", karyawan.getTglLahir(), "Tanggal lahir tidak boleh di masa depan");
        }

        // Validasi umur minimal 17 tahun
        if (karyawan.getTglLahir().isAfter(LocalDate.now().minusYears(17))) {
            throw new InvalidDataException("tglLahir", karyawan.getTglLahir(), "Karyawan harus berusia minimal 17 tahun");
        }

        // Validasi gaji
        if (karyawan.getGaji() == null) {
            throw new InvalidDataException("gaji", null, "Gaji wajib diisi");
        }

        if (karyawan.getGaji() < 0) {
            throw new InvalidDataException("gaji", karyawan.getGaji(), "Gaji tidak boleh negatif");
        }

        if (karyawanRepository.existsById(karyawan.getIdKaryawan())) {
            throw new DataAlreadyExistsException("Karyawan", karyawan.getIdKaryawan());
        }

        return karyawanRepository.save(karyawan);
    }

    @Transactional
    public List<Karyawan> saveBulk(List<Karyawan> karyawanList) {
        for (Karyawan karyawan : karyawanList) {
            // Validasi idKaryawan
            if (karyawan.getIdKaryawan() == null || karyawan.getIdKaryawan().isBlank()) {
                throw new InvalidDataException("idKaryawan", null, "ID karyawan wajib diisi untuk setiap data");
            }

            if (karyawan.getIdKaryawan().length() != 4) {
                throw new InvalidDataException("idKaryawan", karyawan.getIdKaryawan(), "ID karyawan harus 4 karakter");
            }

            // Validasi nama
            if (karyawan.getNama() == null || karyawan.getNama().isBlank()) {
                throw new InvalidDataException("nama", null, "Nama karyawan wajib diisi untuk setiap data");
            }

            if (karyawan.getNama().length() > 20) {
                throw new InvalidDataException("nama", karyawan.getNama(), "Nama karyawan maksimal 20 karakter");
            }

            // Validasi jenis kelamin
            if (karyawan.getJenisKelamin() == null || karyawan.getJenisKelamin().isBlank()) {
                throw new InvalidDataException("jenisKelamin", null, "Jenis kelamin wajib diisi untuk setiap data");
            }

            if (!karyawan.getJenisKelamin().equals("L") && !karyawan.getJenisKelamin().equals("P")) {
                throw new InvalidDataException("jenisKelamin", karyawan.getJenisKelamin(), "Jenis kelamin harus 'L' atau 'P'");
            }

            // Validasi alamat
            if (karyawan.getAlamat() == null || karyawan.getAlamat().isBlank()) {
                throw new InvalidDataException("alamat", null, "Alamat wajib diisi untuk setiap data");
            }

            if (karyawan.getAlamat().length() > 50) {
                throw new InvalidDataException("alamat", karyawan.getAlamat(), "Alamat maksimal 50 karakter");
            }

            // Validasi telepon (opsional)
            if (karyawan.getTelepon() != null && !karyawan.getTelepon().isBlank()) {
                if (karyawan.getTelepon().length() > 15) {
                    throw new InvalidDataException("telepon", karyawan.getTelepon(), "Telepon maksimal 15 karakter");
                }
            }

            // Validasi tgl_lahir
            if (karyawan.getTglLahir() == null) {
                throw new InvalidDataException("tglLahir", null, "Tanggal lahir wajib diisi untuk setiap data");
            }

            if (karyawan.getTglLahir().isAfter(LocalDate.now())) {
                throw new InvalidDataException("tglLahir", karyawan.getTglLahir(), "Tanggal lahir tidak boleh di masa depan");
            }

            if (karyawan.getTglLahir().isAfter(LocalDate.now().minusYears(17))) {
                throw new InvalidDataException("tglLahir", karyawan.getTglLahir(), "Karyawan harus berusia minimal 17 tahun");
            }

            // Validasi gaji
            if (karyawan.getGaji() == null) {
                throw new InvalidDataException("gaji", null, "Gaji wajib diisi untuk setiap data");
            }

            if (karyawan.getGaji() < 0) {
                throw new InvalidDataException("gaji", karyawan.getGaji(), "Gaji tidak boleh negatif");
            }

            if (karyawanRepository.existsById(karyawan.getIdKaryawan())) {
                throw new DataAlreadyExistsException("Karyawan", karyawan.getIdKaryawan());
            }
        }
        return karyawanRepository.saveAll(karyawanList);
    }

    // UPDATE
    public Karyawan update(String id, Karyawan updated) {
        Karyawan existing = getById(id); // akan lempar DataNotFoundException

        // Validasi nama
        if (updated.getNama() == null || updated.getNama().isBlank()) {
            throw new InvalidDataException("nama", null, "Nama karyawan wajib diisi");
        }

        if (updated.getNama().length() > 20) {
            throw new InvalidDataException("nama", updated.getNama(), "Nama karyawan maksimal 20 karakter");
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

        if (updated.getTglLahir().isAfter(LocalDate.now().minusYears(17))) {
            throw new InvalidDataException("tglLahir", updated.getTglLahir(), "Karyawan harus berusia minimal 17 tahun");
        }

        // Validasi gaji
        if (updated.getGaji() == null) {
            throw new InvalidDataException("gaji", null, "Gaji wajib diisi");
        }

        if (updated.getGaji() < 0) {
            throw new InvalidDataException("gaji", updated.getGaji(), "Gaji tidak boleh negatif");
        }

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

        karyawanRepository.deleteAllById(ids);
    }

    public void delete(String id) {
        if (!karyawanRepository.existsById(id)) {
            throw new DataNotFoundException("Karyawan", id);
        }
        karyawanRepository.deleteById(id);
    }
}