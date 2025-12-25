package ui.ft.ccit.faculty.transaksi.transaksi.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ui.ft.ccit.faculty.transaksi.transaksi.model.Transaksi;
import ui.ft.ccit.faculty.transaksi.transaksi.view.TransaksiService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transaksi")
public class TransaksiController {

    private final TransaksiService service;

    public TransaksiController(TransaksiService service) {
        this.service = service;
    }

    // GET list semua transaksi
    @GetMapping
    public List<Transaksi> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        // TANPA pagination
        if (page == null && size == null) {
            return service.getAll();
        }

        // DENGAN pagination
        int p = (page != null && page >= 0) ? page : 0;
        int s = (size != null && size > 0) ? size : 5;
        return service.getAllWithPagination(p, s);
    }

    // GET satu transaksi by kode
    @GetMapping("/{kode}")
    public Transaksi get(@PathVariable String kode) {
        return service.getByKode(kode);
    }

    // SEARCH by pelanggan
    @GetMapping("/search/pelanggan/{idPelanggan}")
    public List<Transaksi> searchByPelanggan(@PathVariable String idPelanggan) {
        return service.searchByPelanggan(idPelanggan);
    }

    // SEARCH by karyawan
    @GetMapping("/search/karyawan/{idKaryawan}")
    public List<Transaksi> searchByKaryawan(@PathVariable String idKaryawan) {
        return service.searchByKaryawan(idKaryawan);
    }

    // SEARCH by rentang tanggal
    @GetMapping("/search/tanggal")
    public List<Transaksi> searchByTanggal(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return service.searchByTanggal(start, end);
    }

    // POST - create transaksi baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaksi create(@RequestBody Transaksi transaksi) {
        return service.save(transaksi);
    }

    // POST - create transaksi bulk baru
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Transaksi> createBulk(@RequestBody List<Transaksi> transaksiList) {
        return service.saveBulk(transaksiList);
    }

    // PUT - edit/update transaksi
    @PutMapping("/{kode}")
    public Transaksi update(@PathVariable String kode, @RequestBody Transaksi transaksi) {
        return service.update(kode, transaksi);
    }

    // DELETE - hapus multiple transaksi
    @DeleteMapping("/bulk")
    public void deleteBulk(@RequestBody List<String> kodeList) {
        service.deleteBulk(kodeList);
    }

    // DELETE - hapus transaksi
    @DeleteMapping("/{kode}")
    public void delete(@PathVariable String kode) {
        service.delete(kode);
    }
}