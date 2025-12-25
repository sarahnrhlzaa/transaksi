package ui.ft.ccit.faculty.transaksi.jenisbarang.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ui.ft.ccit.faculty.transaksi.jenisbarang.model.JenisBarang;
import ui.ft.ccit.faculty.transaksi.jenisbarang.view.JenisBarangService;

import java.util.List;

@RestController
@RequestMapping("/api/jenisbarang")
public class JenisBarangController {

    private final JenisBarangService service;

    public JenisBarangController(JenisBarangService service) {
        this.service = service;
    }

    // GET list semua jenis barang
    @GetMapping
    public List<JenisBarang> list(
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

    // GET satu jenis barang by id
    @GetMapping("/{id}")
    public JenisBarang get(@PathVariable Byte id) {
        return service.getById(id);
    }

    // SEARCH by nama
    @GetMapping("/search")
    public List<JenisBarang> search(@RequestParam String q) {
        return service.searchByNama(q);
    }

    // POST - create jenis barang baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JenisBarang create(@RequestBody JenisBarang jenisBarang) {
        return service.save(jenisBarang);
    }

    // POST - create jenis barang bulk baru
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<JenisBarang> createBulk(@RequestBody List<JenisBarang> jenisBarangList) {
        return service.saveBulk(jenisBarangList);
    }

    // PUT - edit/update jenis barang
    @PutMapping("/{id}")
    public JenisBarang update(@PathVariable Byte id, @RequestBody JenisBarang jenisBarang) {
        return service.update(id, jenisBarang);
    }

    // DELETE - hapus multiple jenis barang
    @DeleteMapping("/bulk")
    public void deleteBulk(@RequestBody List<Byte> ids) {
        service.deleteBulk(ids);
    }

    // DELETE - hapus jenis barang
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Byte id) {
        service.delete(id);
    }
}