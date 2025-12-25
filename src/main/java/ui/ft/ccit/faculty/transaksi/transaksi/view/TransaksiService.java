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

    public Transaksi getByKode(String kode) {
        return transaksiRepository.findById(kode)
                .orElseThrow(() -> new DataNotFoundException("Transaksi", kode));
    }

    public List<Transaksi> searchByPelanggan(String idPelanggan) {
        return transaksiRepository.findByIdPelanggan(idPelanggan);
    }

    public List<Transaksi> searchByKaryawan(String idKaryawan) {
        return transaksiRepository.findByIdKaryawan(idKaryawan);
    }

    public List<Transaksi> searchByTanggal(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new InvalidDataException("Transaksi",
                    "Tanggal mulai tidak boleh lebih besar dari tanggal akhir");
        }
        return transaksiRepository.findByTglTransaksiBetween(start, end);
    }

    // CREATE
    public Transaksi save(Transaksi transaksi) {
        validateTransaksi(transaksi);
        validateForeignKeys(transaksi);

        if (transaksiRepository.existsById(transaksi.getKodeTransaksi())) {
            throw new DataAlreadyExistsException("Transaksi", transaksi.getKodeTransaksi());
        }

        return transaksiRepository.save(transaksi);
    }

    @Transactional
    public List<Transaksi> saveBulk(List<Transaksi> transaksiList) {
        for (Transaksi transaksi : transaksiList) {
            validateTransaksi(transaksi);
            validateForeignKeys(transaksi);

            if (transaksiRepository.existsById(transaksi.getKodeTransaksi())) {
                throw new DataAlreadyExistsException("Transaksi", transaksi.getKodeTransaksi());
            }
        }
        return transaksiRepository.saveAll(transaksiList);
    }

    // UPDATE
    public Transaksi update(String kode, Transaksi updated) {
        Transaksi existing = getByKode(kode);

        validateTransaksiForUpdate(updated);
        validateForeignKeys(updated);

        existing.setTglTransaksi(updated.getTglTransaksi());
        existing.setIdPelanggan(updated.getIdPelanggan());
        existing.setIdKaryawan(updated.getIdKaryawan());

        return transaksiRepository.save(existing);
    }

    // DELETE
    @Transactional
    public void deleteBulk(List<String> kodeList) {
        if (kodeList == null || kodeList.isEmpty()) {
            throw new IllegalArgumentException("List kode tidak boleh kosong");
        }

        if (kodeList.size() > 100) {
            throw new IllegalArgumentException("Maksimal 100 data per bulk delete");
        }

        long existingCount = transaksiRepository.countByKodeTransaksiIn(kodeList);
        if (existingCount != kodeList.size()) {
            throw new IllegalStateException("Sebagian kode tidak ditemukan, operasi dibatalkan");
        }

        transaksiRepository.deleteAllById(kodeList);
    }

    public void delete(String kode) {
        if (!transaksiRepository.existsById(kode)) {
            throw new DataNotFoundException("Transaksi", kode);
        }
        transaksiRepository.deleteById(kode);
    }

    // VALIDATION METHODS
    private void validateTransaksi(Transaksi transaksi) {
        if (transaksi.getKodeTransaksi() == null || transaksi.getKodeTransaksi().isBlank()) {
            throw new InvalidDataException("Transaksi", "kodeTransaksi", "null/blank");
        }
        if (transaksi.getKodeTransaksi().length() != 4) {
            throw new InvalidDataException("Transaksi", "kodeTransaksi",
                    transaksi.getKodeTransaksi() + " (harus 4 karakter)");
        }

        if (transaksi.getTglTransaksi() == null) {
            throw new InvalidDataException("Transaksi", "tglTransaksi", "null");
        }
        if (transaksi.getTglTransaksi().isAfter(LocalDateTime.now())) {
            throw new InvalidDataException("Transaksi", "tglTransaksi",
                    transaksi.getTglTransaksi().toString() + " (tidak boleh di masa depan)");
        }
        LocalDateTime minDate = LocalDateTime.now().minusYears(10);
        if (transaksi.getTglTransaksi().isBefore(minDate)) {
            throw new InvalidDataException("Transaksi", "tglTransaksi",
                    transaksi.getTglTransaksi().toString() + " (maksimal 10 tahun ke belakang)");
        }

        if (transaksi.getIdPelanggan() == null || transaksi.getIdPelanggan().isBlank()) {
            throw new InvalidDataException("Transaksi", "idPelanggan", "null/blank");
        }
        if (transaksi.getIdPelanggan().length() != 4) {
            throw new InvalidDataException("Transaksi", "idPelanggan",
                    transaksi.getIdPelanggan() + " (harus 4 karakter)");
        }

        if (transaksi.getIdKaryawan() == null || transaksi.getIdKaryawan().isBlank()) {
            throw new InvalidDataException("Transaksi", "idKaryawan", "null/blank");
        }
        if (transaksi.getIdKaryawan().length() != 4) {
            throw new InvalidDataException("Transaksi", "idKaryawan",
                    transaksi.getIdKaryawan() + " (harus 4 karakter)");
        }
    }

    private void validateTransaksiForUpdate(Transaksi transaksi) {
        if (transaksi.getTglTransaksi() == null) {
            throw new InvalidDataException("Transaksi", "tglTransaksi", "null");
        }
        if (transaksi.getTglTransaksi().isAfter(LocalDateTime.now())) {
            throw new InvalidDataException("Transaksi", "tglTransaksi",
                    transaksi.getTglTransaksi().toString() + " (tidak boleh di masa depan)");
        }
        LocalDateTime minDate = LocalDateTime.now().minusYears(10);
        if (transaksi.getTglTransaksi().isBefore(minDate)) {
            throw new InvalidDataException("Transaksi", "tglTransaksi",
                    transaksi.getTglTransaksi().toString() + " (maksimal 10 tahun ke belakang)");
        }

        if (transaksi.getIdPelanggan() == null || transaksi.getIdPelanggan().isBlank()) {
            throw new InvalidDataException("Transaksi", "idPelanggan", "null/blank");
        }
        if (transaksi.getIdPelanggan().length() != 4) {
            throw new InvalidDataException("Transaksi", "idPelanggan",
                    transaksi.getIdPelanggan() + " (harus 4 karakter)");
        }

        if (transaksi.getIdKaryawan() == null || transaksi.getIdKaryawan().isBlank()) {
            throw new InvalidDataException("Transaksi", "idKaryawan", "null/blank");
        }
        if (transaksi.getIdKaryawan().length() != 4) {
            throw new InvalidDataException("Transaksi", "idKaryawan",
                    transaksi.getIdKaryawan() + " (harus 4 karakter)");
        }
    }

    private void validateForeignKeys(Transaksi transaksi) {
        if (!pelangganRepository.existsById(transaksi.getIdPelanggan())) {
            throw new DataNotFoundException("Pelanggan", transaksi.getIdPelanggan());
        }

        if (!karyawanRepository.existsById(transaksi.getIdKaryawan())) {
            throw new DataNotFoundException("Karyawan", transaksi.getIdKaryawan());
        }
    }
}