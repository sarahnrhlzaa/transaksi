package ui.ft.ccit.faculty.transaksi.detailtransaksi.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.InvalidDataException;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksi;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksi.DetailTransaksiId;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksiRepository;
import ui.ft.ccit.faculty.transaksi.transaksi.model.TransaksiRepository;
import ui.ft.ccit.faculty.transaksi.barang.model.Barang;
import ui.ft.ccit.faculty.transaksi.barang.model.BarangRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetailTransaksiService {

    private final DetailTransaksiRepository detailTransaksiRepository;
    private final TransaksiRepository transaksiRepository;
    private final BarangRepository barangRepository;

    public DetailTransaksiService(DetailTransaksiRepository detailTransaksiRepository,
                                 TransaksiRepository transaksiRepository,
                                 BarangRepository barangRepository) {
        this.detailTransaksiRepository = detailTransaksiRepository;
        this.transaksiRepository = transaksiRepository;
        this.barangRepository = barangRepository;
    }

    public List<DetailTransaksi> getAll() {
        return detailTransaksiRepository.findAll();
    }

    public List<DetailTransaksi> getAllWithPagination(int page, int size) {
        return detailTransaksiRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public DetailTransaksi getById(String kodeTransaksi, String idBarang) {
        DetailTransaksiId id = new DetailTransaksiId(kodeTransaksi, idBarang);
        return detailTransaksiRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("DetailTransaksi", 
                    kodeTransaksi + "-" + idBarang));
    }

    public List<DetailTransaksi> getByTransaksi(String kodeTransaksi) {
        return detailTransaksiRepository.findByKodeTransaksi(kodeTransaksi);
    }

    public List<DetailTransaksi> getByBarang(String idBarang) {
        return detailTransaksiRepository.findByIdBarang(idBarang);
    }

    // CREATE - dengan UPDATE STOK otomatis
    @Transactional
    public DetailTransaksi save(DetailTransaksi detail) {
        // Validasi kodeTransaksi
        if (detail.getKodeTransaksi() == null || detail.getKodeTransaksi().isBlank()) {
            throw new InvalidDataException("kodeTransaksi", null, "Kode transaksi wajib diisi");
        }

        if (!transaksiRepository.existsById(detail.getKodeTransaksi())) {
            throw new DataNotFoundException("Transaksi", detail.getKodeTransaksi());
        }

        // Validasi idBarang
        if (detail.getIdBarang() == null || detail.getIdBarang().isBlank()) {
            throw new InvalidDataException("idBarang", null, "ID barang wajib diisi");
        }

        Barang barang = barangRepository.findById(detail.getIdBarang())
                .orElseThrow(() -> new DataNotFoundException("Barang", detail.getIdBarang()));

        // Validasi jumlah
        if (detail.getJumlah() == null || detail.getJumlah() <= 0) {
            throw new InvalidDataException("jumlah", detail.getJumlah(), "Jumlah harus lebih dari 0");
        }

        // VALIDASI STOK - cek apakah stok cukup
        if (barang.getStok() < detail.getJumlah()) {
            throw new InvalidDataException("jumlah", detail.getJumlah(), 
                "Stok tidak cukup. Tersedia: " + barang.getStok() + ", diminta: " + detail.getJumlah());
        }

        // Cek apakah detail ini sudah ada
        DetailTransaksiId id = new DetailTransaksiId(detail.getKodeTransaksi(), detail.getIdBarang());
        if (detailTransaksiRepository.existsById(id)) {
            throw new DataAlreadyExistsException("DetailTransaksi", 
                detail.getKodeTransaksi() + "-" + detail.getIdBarang());
        }

        // UPDATE STOK BARANG - kurangi stok
        barang.setStok((short) (barang.getStok() - detail.getJumlah()));
        barangRepository.save(barang);

        // Simpan detail transaksi
        return detailTransaksiRepository.save(detail);
    }

    @Transactional
    public List<DetailTransaksi> saveBulk(List<DetailTransaksi> detailList) {
        for (DetailTransaksi detail : detailList) {
            // Validasi kodeTransaksi
            if (detail.getKodeTransaksi() == null || detail.getKodeTransaksi().isBlank()) {
                throw new InvalidDataException("kodeTransaksi", null, "Kode transaksi wajib diisi untuk setiap data");
            }

            if (!transaksiRepository.existsById(detail.getKodeTransaksi())) {
                throw new DataNotFoundException("Transaksi", detail.getKodeTransaksi());
            }

            // Validasi idBarang
            if (detail.getIdBarang() == null || detail.getIdBarang().isBlank()) {
                throw new InvalidDataException("idBarang", null, "ID barang wajib diisi untuk setiap data");
            }

            Barang barang = barangRepository.findById(detail.getIdBarang())
                    .orElseThrow(() -> new DataNotFoundException("Barang", detail.getIdBarang()));

            // Validasi jumlah
            if (detail.getJumlah() == null || detail.getJumlah() <= 0) {
                throw new InvalidDataException("jumlah", detail.getJumlah(), "Jumlah harus lebih dari 0");
            }

            // VALIDASI STOK
            if (barang.getStok() < detail.getJumlah()) {
                throw new InvalidDataException("jumlah", detail.getJumlah(), 
                    "Stok barang " + detail.getIdBarang() + " tidak cukup. Tersedia: " + barang.getStok());
            }

            // Cek duplikat
            DetailTransaksiId id = new DetailTransaksiId(detail.getKodeTransaksi(), detail.getIdBarang());
            if (detailTransaksiRepository.existsById(id)) {
                throw new DataAlreadyExistsException("DetailTransaksi", 
                    detail.getKodeTransaksi() + "-" + detail.getIdBarang());
            }

            // UPDATE STOK
            barang.setStok((short) (barang.getStok() - detail.getJumlah()));
            barangRepository.save(barang);
        }

        return detailTransaksiRepository.saveAll(detailList);
    }

    // UPDATE - adjust stok kalau jumlah berubah
    @Transactional
    public DetailTransaksi update(String kodeTransaksi, String idBarang, DetailTransaksi updated) {
        DetailTransaksi existing = getById(kodeTransaksi, idBarang);

        // Validasi jumlah baru
        if (updated.getJumlah() == null || updated.getJumlah() <= 0) {
            throw new InvalidDataException("jumlah", updated.getJumlah(), "Jumlah harus lebih dari 0");
        }

        Barang barang = barangRepository.findById(idBarang)
                .orElseThrow(() -> new DataNotFoundException("Barang", idBarang));

        // Hitung selisih jumlah
        short selisih = (short) (updated.getJumlah() - existing.getJumlah());

        if (selisih > 0) {
            // Jumlah bertambah, cek stok cukup
            if (barang.getStok() < selisih) {
                throw new InvalidDataException("jumlah", updated.getJumlah(), 
                    "Stok tidak cukup untuk menambah jumlah. Tersedia: " + barang.getStok());
            }
            // Kurangi stok
            barang.setStok((short) (barang.getStok() - selisih));
        } else if (selisih < 0) {
            // Jumlah berkurang, kembalikan stok
            barang.setStok((short) (barang.getStok() + Math.abs(selisih)));
        }

        barangRepository.save(barang);

        existing.setJumlah(updated.getJumlah());
        return detailTransaksiRepository.save(existing);
    }

    // DELETE - kembalikan stok
    @Transactional
    public void delete(String kodeTransaksi, String idBarang) {
        DetailTransaksi detail = getById(kodeTransaksi, idBarang);

        // KEMBALIKAN STOK
        Barang barang = barangRepository.findById(idBarang)
                .orElseThrow(() -> new DataNotFoundException("Barang", idBarang));
        
        barang.setStok((short) (barang.getStok() + detail.getJumlah()));
        barangRepository.save(barang);

        // Hapus detail
        DetailTransaksiId id = new DetailTransaksiId(kodeTransaksi, idBarang);
        detailTransaksiRepository.deleteById(id);
    }

    // DELETE semua detail dari satu transaksi - kembalikan semua stok
    @Transactional
    public void deleteByTransaksi(String kodeTransaksi) {
        List<DetailTransaksi> details = detailTransaksiRepository.findByKodeTransaksi(kodeTransaksi);

        // Kembalikan stok untuk setiap detail
        for (DetailTransaksi detail : details) {
            Barang barang = barangRepository.findById(detail.getIdBarang())
                    .orElseThrow(() -> new DataNotFoundException("Barang", detail.getIdBarang()));
            
            barang.setStok((short) (barang.getStok() + detail.getJumlah()));
            barangRepository.save(barang);
        }

        // Hapus semua detail
        detailTransaksiRepository.deleteByKodeTransaksi(kodeTransaksi);
    }
}