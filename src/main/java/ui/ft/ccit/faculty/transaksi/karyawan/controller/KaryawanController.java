package ui.ft.ccit.faculty.transaksi.karyawan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ui.ft.ccit.faculty.transaksi.karyawan.model.Karyawan;
import ui.ft.ccit.faculty.transaksi.karyawan.view.KaryawanService;

import java.util.List;

@RestController
@RequestMapping("/api/karyawan")
public class KaryawanController {

    private final KaryawanService service;

    public KaryawanController(KaryawanService service) {
        this.service = service;
    }

    // GET list semua karyawan
    @GetMapping
    public List<Karyawan> list(
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

    // GET satu karyawan by id
    @GetMapping("/{id}")
    public Karyawan get(@PathVariable String id) {
        return service.getById(id);
    }

    // SEARCH by nama
    @GetMapping("/search")
    public List<Karyawan> search(@RequestParam String q) {
        return service.searchByNama(q);
    }

    // POST - create karyawan baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Karyawan create(@RequestBody Karyawan karyawan) {
        return service.save(karyawan);
    }

    // POST - create karyawan bulk baru
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Karyawan> createBulk(@RequestBody List<Karyawan> karyawanList) {
        return service.saveBulk(karyawanList);
    }

    // PUT - edit/update karyawan
    @PutMapping("/{id}")
    public Karyawan update(@PathVariable String id, @RequestBody Karyawan karyawan) {
        return service.update(id, karyawan);
    }

    // DELETE - hapus multiple karyawan
    @DeleteMapping("/bulk")
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }

    // DELETE - hapus karyawan
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}