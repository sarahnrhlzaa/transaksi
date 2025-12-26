package ui.ft.ccit.faculty.transaksi.pemasok.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ui.ft.ccit.faculty.transaksi.pemasok.model.Pemasok;
import ui.ft.ccit.faculty.transaksi.pemasok.view.PemasokService;

import java.util.List;

@RestController
@RequestMapping("/api/pemasok")
public class PemasokController {

    private final PemasokService service;

    public PemasokController(PemasokService service) {
        this.service = service;
    }

    // GET list semua pemasok
    @GetMapping
    public List<Pemasok> list(
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

    // GET satu pemasok by id
    @GetMapping("/{id}")
    public Pemasok get(@PathVariable String id) {
        return service.getById(id);
    }

    // SEARCH by nama
    @GetMapping("/search")
    public List<Pemasok> search(@RequestParam String q) {
        return service.searchByNama(q);
    }

    // POST - create pemasok baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pemasok create(@RequestBody Pemasok pemasok) {
        return service.save(pemasok);
    }

    // POST - create pemasok bulk baru
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Pemasok> createBulk(@RequestBody List<Pemasok> pemasok) {
        return service.saveBulk(pemasok);
    }

    // PUT - edit/update pemasok
    @PutMapping("/{id}")
    public Pemasok update(@PathVariable String id, @RequestBody Pemasok pemasok) {
        return service.update(id, pemasok);
    }

    // DELETE - hapus multiple pemasok
    @DeleteMapping("/bulk")
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }

    // DELETE - hapus pemasok
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}