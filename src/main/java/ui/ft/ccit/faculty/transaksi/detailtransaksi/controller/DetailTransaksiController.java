package ui.ft.ccit.faculty.transaksi.detailtransaksi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksi;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.view.DetailTransaksiService;

import java.util.List;

@RestController
@RequestMapping("/api/detailtransaksi")
public class DetailTransaksiController {

    private final DetailTransaksiService service;

    public DetailTransaksiController(DetailTransaksiService service) {
        this.service = service;
    }

    // GET list semua detail transaksi
    @GetMapping
    public List<DetailTransaksi> list(
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

    // GET satu detail transaksi by composite key
    @GetMapping("/{kodeTransaksi}/{idBarang}")
    public DetailTransaksi get(@PathVariable String kodeTransaksi, @PathVariable String idBarang) {
        return service.getById(kodeTransaksi, idBarang);
    }

    // SEARCH by kode transaksi
    @GetMapping("/transaksi/{kodeTransaksi}")
    public List<DetailTransaksi> searchByTransaksi(@PathVariable String kodeTransaksi) {
        return service.searchByTransaksi(kodeTransaksi);
    }

    // SEARCH by id barang
    @GetMapping("/barang/{idBarang}")
    public List<DetailTransaksi> searchByBarang(@PathVariable String idBarang) {
        return service.searchByBarang(idBarang);
    }

    // POST - create detail transaksi baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DetailTransaksi create(@RequestBody DetailTransaksi detailTransaksi) {
        return service.save(detailTransaksi);
    }

    // POST - create detail transaksi bulk baru
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<DetailTransaksi> createBulk(@RequestBody List<DetailTransaksi> detailList) {
        return service.saveBulk(detailList);
    }

    // PUT - edit/update detail transaksi (hanya jumlah yang bisa diubah)
    @PutMapping("/{kodeTransaksi}/{idBarang}")
    public DetailTransaksi update(@PathVariable String kodeTransaksi, 
                                   @PathVariable String idBarang,
                                   @RequestBody DetailTransaksi detailTransaksi) {
        return service.update(kodeTransaksi, idBarang, detailTransaksi);
    }

    // DELETE - hapus detail transaksi
    @DeleteMapping("/{kodeTransaksi}/{idBarang}")
    public void delete(@PathVariable String kodeTransaksi, @PathVariable String idBarang) {
        service.delete(kodeTransaksi, idBarang);
    }

    // DELETE - hapus semua detail berdasarkan kode transaksi
    @DeleteMapping("/transaksi/{kodeTransaksi}")
    public void deleteByTransaksi(@PathVariable String kodeTransaksi) {
        service.deleteByTransaksi(kodeTransaksi);
    }
}