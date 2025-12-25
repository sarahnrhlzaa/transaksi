package ui.ft.ccit.faculty.transaksi.transaksi.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.InvalidDataException;
import ui.ft.ccit.faculty.transaksi.transaksi.model.Transaksi;
import ui.ft.ccit.faculty.transaksi.transaksi.model.TransaksiRepository;
import ui.ft.ccit.faculty.transaksi.pelanggan.model.PelangganRepository;
import ui.ft.ccit.faculty.transaksi.karyawan.model.KaryawanRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransaksiService {

    private final TransaksiRepository transaksiRepository;
    private final PelangganRepository pelangganRepository;
    private final KaryawanRepository karyawanRepository;

    public TransaksiService(TransaksiRepository transaksiRepository,
                           PelangganRepository pelangganRepository,
                           KaryawanRepository karyawanRepository) {
        this.transaksiRepository = transaksiRepository;
        this.pelangganRepository = pelangganRepository;
        this.karyawanRepository = karyawanRepository;
    }

    public List<Transaksi> getAll() {
        return transaksiRepository.findAll();
    }

    public List<Transaksi> getAllWithPagination(int page, int size) {
        return transaksiRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public Transaksi getById(String id) {
        return transaksiRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Transaksi", id));
    }

    public List<Transaksi> getByPelanggan(String idPelanggan) {
        return transaksiRepository.findByIdPelanggan(idPelanggan);
    }

    public List<Transaksi> getByKaryawan(String idKaryawan) {
        return transaksiRepository.findByIdKaryawan(idKaryawan);
    }

    public List<Transaksi> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return transaksiRepository.findByTglTransaksiBetween(start, end);
    }

    // CREATE
    public Transaksi save(Transaksi transaksi) {
        // Validasi kodeTransaksi
        if (transaksi.getKodeTransaksi() == null || transaksi.getKodeTransaksi().isBlank()) {
            throw new InvalidDataException("kodeTransaksi", null, "Kode transaksi wajib diisi");
        }

        if (transaksi.getKodeTransaksi().length() != 4) {
            throw new InvalidDataException("kodeTransaksi", transaksi.getKodeTransaksi(), "Kode transaksi harus 4 karakter");
        }

        // Validasi tglTransaksi
        if (transaksi.getTglTransaksi() == null) {
            throw new InvalidDataException("tglTransaksi", null, "Tanggal transaksi wajib diisi");
        }

        if (transaksi.getTglTransaksi().isAfter(LocalDateTime.now())) {
            throw new InvalidDataException("tglTransaksi", transaksi.getTglTransaksi(), "Tanggal transaksi tidak boleh di masa depan");
        }

        // Validasi idPelanggan
        if (transaksi.getIdPelanggan() == null || transaksi.getIdPelanggan().isBlank()) {
            throw new InvalidDataException("idPelanggan", null, "ID pelanggan wajib diisi");
        }

        if (!pelangganRepository.existsById(transaksi.getIdPelanggan())) {
            throw new DataNotFoundException("Pelanggan", transaksi.getIdPelanggan());
        }

        // Validasi idKaryawan
        if (transaksi.getIdKaryawan() == null || transaksi.getIdKaryawan().isBlank()) {
            throw new InvalidDataException("idKaryawan", null, "ID karyawan wajib diisi");
        }

        if (!karyawanRepository.existsById(transaksi.getIdKaryawan())) {
            throw new DataNotFoundException("Karyawan", transaksi.getIdKaryawan());
        }

        if (transaksiRepository.existsById(transaksi.getKodeTransaksi())) {
            throw new DataAlreadyExistsException("Transaksi", transaksi.getKodeTransaksi());
        }

        return transaksiRepository.save(transaksi);
    }

    @Transactional
    public List<Transaksi> saveBulk(List<Transaksi> transaksiList) {
        for (Transaksi transaksi : transaksiList) {
            // Validasi kodeTransaksi
            if (transaksi.getKodeTransaksi() == null || transaksi.getKodeTransaksi().isBlank()) {
                throw new InvalidDataException("kodeTransaksi", null, "Kode transaksi wajib diisi untuk setiap data");
            }

            if (transaksi.getKodeTransaksi().length() != 4) {
                throw new InvalidDataException("kodeTransaksi", transaksi.getKodeTransaksi(), "Kode transaksi harus 4 karakter");
            }

            // Validasi tglTransaksi
            if (transaksi.getTglTransaksi() == null) {
                throw new InvalidDataException("tglTransaksi", null, "Tanggal transaksi wajib diisi untuk setiap data");
            }

            if (transaksi.getTglTransaksi().isAfter(LocalDateTime.now())) {
                throw new InvalidDataException("tglTransaksi", transaksi.getTglTransaksi(), "Tanggal transaksi tidak boleh di masa depan");
            }

            // Validasi idPelanggan
            if (transaksi.getIdPelanggan() == null || transaksi.getIdPelanggan().isBlank()) {
                throw new InvalidDataException("idPelanggan", null, "ID pelanggan wajib diisi untuk setiap data");
            }

            if (!pelangganRepository.existsById(transaksi.getIdPelanggan())) {
                throw new DataNotFoundException("Pelanggan", transaksi.getIdPelanggan());
            }

            // Validasi idKaryawan
            if (transaksi.getIdKaryawan() == null || transaksi.getIdKaryawan().isBlank()) {
                throw new InvalidDataException("idKaryawan", null, "ID karyawan wajib diisi untuk setiap data");
            }

            if (!karyawanRepository.existsById(transaksi.getIdKaryawan())) {
                throw new DataNotFoundException("Karyawan", transaksi.getIdKaryawan());
            }

            if (transaksiRepository.existsById(transaksi.getKodeTransaksi())) {
                throw new DataAlreadyExistsException("Transaksi", transaksi.getKodeTransaksi());
            }
        }
        return transaksiRepository.saveAll(transaksiList);
    }

    // UPDATE
    public Transaksi update(String id, Transaksi updated) {
        Transaksi existing = getById(id); // akan lempar DataNotFoundException

        // Validasi tglTransaksi
        if (updated.getTglTransaksi() == null) {
            throw new InvalidDataException("tglTransaksi", null, "Tanggal transaksi wajib diisi");
        }

        if (updated.getTglTransaksi().isAfter(LocalDateTime.now())) {
            throw new InvalidDataException("tglTransaksi", updated.getTglTransaksi(), "Tanggal transaksi tidak boleh di masa depan");
        }

        // Validasi idPelanggan
        if (updated.getIdPelanggan() == null || updated.getIdPelanggan().isBlank()) {
            throw new InvalidDataException("idPelanggan", null, "ID pelanggan wajib diisi");
        }

        if (!pelangganRepository.existsById(updated.getIdPelanggan())) {
            throw new DataNotFoundException("Pelanggan", updated.getIdPelanggan());
        }

        // Validasi idKaryawan
        if (updated.getIdKaryawan() == null || updated.getIdKaryawan().isBlank()) {
            throw new InvalidDataException("idKaryawan", null, "ID karyawan wajib diisi");
        }

        if (!karyawanRepository.existsById(updated.getIdKaryawan())) {
            throw new DataNotFoundException("Karyawan", updated.getIdKaryawan());
        }

        existing.setTglTransaksi(updated.getTglTransaksi());
        existing.setIdPelanggan(updated.getIdPelanggan());
        existing.setIdKaryawan(updated.getIdKaryawan());

        return transaksiRepository.save(existing);
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

        transaksiRepository.deleteAllById(ids);
    }

    public void delete(String id) {
        if (!transaksiRepository.existsById(id)) {
            throw new DataNotFoundException("Transaksi", id);
        }
        transaksiRepository.deleteById(id);
    }
}