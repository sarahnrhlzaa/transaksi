package ui.ft.ccit.faculty.transaksi.detailtransaksi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksi;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksi.DetailTransaksiId;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.view.DetailTransaksiService;

import java.util.List;

@RestController
@RequestMapping("/api/detail-transaksi")
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

    // GET satu detail transaksi by composite id
    @GetMapping("/{kodeTransaksi}/{idBarang}")
    public DetailTransaksi get(
            @PathVariable String kodeTransaksi,
            @PathVariable String idBarang) {
        DetailTransaksiId id = new DetailTransaksiId(kodeTransaksi, idBarang);
        return service.getById(id);
    }

    // GET semua detail by kode transaksi
    @GetMapping("/transaksi/{kodeTransaksi}")
    public List<DetailTransaksi> getByKodeTransaksi(@PathVariable String kodeTransaksi) {
        return service.getByKodeTransaksi(kodeTransaksi);
    }

    // GET semua detail by id barang
    @GetMapping("/barang/{idBarang}")
    public List<DetailTransaksi> getByIdBarang(@PathVariable String idBarang) {
        return service.getByIdBarang(idBarang);
    }

    // POST - create detail transaksi baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DetailTransaksi create(@RequestBody DetailTransaksi detailTransaksi) {
        return service.save(detailTransaksi);
    }

    // POST - create detail transaksi bulk
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<DetailTransaksi> createBulk(@RequestBody List<DetailTransaksi> detailTransaksiList) {
        return service.saveBulk(detailTransaksiList);
    }

    // PUT - update detail transaksi
    @PutMapping("/{kodeTransaksi}/{idBarang}")
    public DetailTransaksi update(
            @PathVariable String kodeTransaksi,
            @PathVariable String idBarang,
            @RequestBody DetailTransaksi detailTransaksi) {
        DetailTransaksiId id = new DetailTransaksiId(kodeTransaksi, idBarang);
        return service.update(id, detailTransaksi);
    }

    // DELETE - hapus detail transaksi
    @DeleteMapping("/{kodeTransaksi}/{idBarang}")
    public void delete(
            @PathVariable String kodeTransaksi,
            @PathVariable String idBarang) {
        DetailTransaksiId id = new DetailTransaksiId(kodeTransaksi, idBarang);
        service.delete(id);
    }

    // DELETE - hapus semua detail by kode transaksi
    @DeleteMapping("/transaksi/{kodeTransaksi}")
    public void deleteByKodeTransaksi(@PathVariable String kodeTransaksi) {
        service.deleteByKodeTransaksi(kodeTransaksi);
    }

    // DELETE - hapus multiple detail transaksi (bulk)
    @DeleteMapping("/bulk")
    public void deleteBulk(@RequestBody List<DetailTransaksiId> ids) {
        service.deleteBulk(ids);
    }
}