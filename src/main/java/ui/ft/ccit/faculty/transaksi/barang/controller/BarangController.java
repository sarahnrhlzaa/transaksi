package ui.ft.ccit.faculty.transaksi.barang.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ui.ft.ccit.faculty.transaksi.barang.model.Barang;
import ui.ft.ccit.faculty.transaksi.barang.view.BarangService;

import java.util.List;

@RestController
@RequestMapping("/api/barang")
public class BarangController {

    private final BarangService service;

    public BarangController(BarangService service) {
        this.service = service;
    }

    // GET list semua barang
    @GetMapping
    public List<Barang> list(
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

    // GET satu barang by id
    @GetMapping("/{id}")
    public Barang get(@PathVariable String id) {
        return service.getById(id);
    }

    // SEARCH by nama
    @GetMapping("/search")
    public List<Barang> search(@RequestParam String q) {
        return service.searchByNama(q);
    }

    // POST - create barang baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Barang create(@RequestBody Barang barang) {
        return service.save(barang);
    }

    // POST - create barang bulk baru
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Barang> createBulk(@RequestBody List<Barang> barang) {
        return service.saveBulk(barang);
    }

    // PUT - edit/update barang
    @PutMapping("/{id}")
    public Barang update(@PathVariable String id, @RequestBody Barang barang) {
        return service.update(id, barang);
    }

    // DELETE - hapus multiple barang
    @DeleteMapping("/bulk")
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }

    // DELETE - hapus barang
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
