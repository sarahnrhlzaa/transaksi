package ui.ft.ccit.faculty.transaksi.detailtransaksi.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.InvalidDataException;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksi;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksi.DetailTransaksiId;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksiRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetailTransaksiService {

    private final DetailTransaksiRepository detailTransaksiRepository;

    public DetailTransaksiService(DetailTransaksiRepository detailTransaksiRepository) {
        this.detailTransaksiRepository = detailTransaksiRepository;
    }

    public List<DetailTransaksi> getAll() {
        return detailTransaksiRepository.findAll();
    }

    public List<DetailTransaksi> getAllWithPagination(int page, int size) {
        return detailTransaksiRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public DetailTransaksi getById(DetailTransaksiId id) {
        return detailTransaksiRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                    "DetailTransaksi",
                    id.getKodeTransaksi() + "-" + id.getIdBarang()
                ));
    }

    public List<DetailTransaksi> getByKodeTransaksi(String kodeTransaksi) {
        return detailTransaksiRepository.findByIdKodeTransaksi(kodeTransaksi);
    }

    public List<DetailTransaksi> getByIdBarang(String idBarang) {
        return detailTransaksiRepository.findByIdIdBarang(idBarang);
    }

    // CREATE
    public DetailTransaksi save(DetailTransaksi detailTransaksi) {
        // validasi ID wajib diisi
        if (detailTransaksi.getId() == null) {
            throw new InvalidDataException("id", "wajib diisi");
        }

        if (detailTransaksi.getKodeTransaksi() == null || detailTransaksi.getKodeTransaksi().isBlank()) {
            throw new InvalidDataException("kodeTransaksi", "wajib diisi");
        }

        if (detailTransaksi.getIdBarang() == null || detailTransaksi.getIdBarang().isBlank()) {
            throw new InvalidDataException("idBarang", "wajib diisi");
        }

        // validasi ID sudah ada
        if (detailTransaksiRepository.existsById(detailTransaksi.getId())) {
            throw new DataAlreadyExistsException(
                "DetailTransaksi",
                detailTransaksi.getKodeTransaksi() + "-" + detailTransaksi.getIdBarang()
            );
        }

        // validasi field wajib
        validateRequiredFields(detailTransaksi);

        return detailTransaksiRepository.save(detailTransaksi);
    }

    @Transactional
    public List<DetailTransaksi> saveBulk(List<DetailTransaksi> detailTransaksiList) {
        for (DetailTransaksi detailTransaksi : detailTransaksiList) {
            if (detailTransaksi.getId() == null) {
                throw new InvalidDataException("id", "wajib diisi untuk setiap detail transaksi");
            }

            if (detailTransaksi.getKodeTransaksi() == null || detailTransaksi.getKodeTransaksi().isBlank()) {
                throw new InvalidDataException("kodeTransaksi", "wajib diisi untuk setiap detail transaksi");
            }

            if (detailTransaksi.getIdBarang() == null || detailTransaksi.getIdBarang().isBlank()) {
                throw new InvalidDataException("idBarang", "wajib diisi untuk setiap detail transaksi");
            }

            if (detailTransaksiRepository.existsById(detailTransaksi.getId())) {
                throw new DataAlreadyExistsException(
                    "DetailTransaksi",
                    detailTransaksi.getKodeTransaksi() + "-" + detailTransaksi.getIdBarang()
                );
            }

            validateRequiredFields(detailTransaksi);
        }
        return detailTransaksiRepository.saveAll(detailTransaksiList);
    }

    // UPDATE
    public DetailTransaksi update(DetailTransaksiId id, DetailTransaksi updated) {
        DetailTransaksi existing = getById(id);

        validateRequiredFields(updated);

        // Update hanya field jumlah (karena composite key tidak bisa diubah)
        existing.setJumlah(updated.getJumlah());

        return detailTransaksiRepository.save(existing);
    }

    // DELETE
    public void delete(DetailTransaksiId id) {
        if (!detailTransaksiRepository.existsById(id)) {
            throw new DataNotFoundException(
                "DetailTransaksi",
                id.getKodeTransaksi() + "-" + id.getIdBarang()
            );
        }
        detailTransaksiRepository.deleteById(id);
    }

    @Transactional
    public void deleteByKodeTransaksi(String kodeTransaksi) {
        if (!detailTransaksiRepository.existsByIdKodeTransaksi(kodeTransaksi)) {
            throw new DataNotFoundException("DetailTransaksi dengan kode transaksi", kodeTransaksi);
        }
        detailTransaksiRepository.deleteByIdKodeTransaksi(kodeTransaksi);
    }

    @Transactional
    public void deleteBulk(List<DetailTransaksiId> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidDataException("ids", "List ID tidak boleh kosong");
        }

        if (ids.size() > 100) {
            throw new InvalidDataException("ids", "Maksimal 100 data per bulk delete");
        }

        // validasi: pastikan semua ID ada
        long existingCount = detailTransaksiRepository.countByIdIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        detailTransaksiRepository.deleteAllById(ids);
    }

    // HELPER: validasi field wajib
    private void validateRequiredFields(DetailTransaksi detailTransaksi) {
        if (detailTransaksi.getJumlah() == null) {
            throw new InvalidDataException("jumlah", "wajib diisi");
        }

        if (detailTransaksi.getJumlah() <= 0) {
            throw new InvalidDataException("jumlah", "harus lebih dari 0");
        }
    }
}