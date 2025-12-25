package ui.ft.ccit.faculty.transaksi.pemasok.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Mengambil daftar semua pemasok", description = "Mengambil seluruh data pemasok yang tersedia di sistem.\r\n"
            + "Mendukung pagination opsional melalui parameter `page` dan `size`.")
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
    @Operation(summary = "Mengambil detail satu pemasok", description = "Mengambil detail satu pemasok berdasarkan ID.")
    public Pemasok get(@PathVariable String id) {
        return service.getById(id);
    }

    // POST - create pemasok baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat pemasok baru", description = "Membuat satu data pemasok baru ke dalam sistem.")
    public Pemasok create(@RequestBody Pemasok pemasok) {
        return service.save(pemasok);
    }

    // POST - create pemasok bulk baru
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat pemasok secara bulk", description = "Membuat banyak pemasok baru dalam satu transaksi.")
    public List<Pemasok> createBulk(@RequestBody List<Pemasok> pemasok) {
        return service.saveBulk(pemasok);
    }

    // PUT - edit/update pemasok
    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data pemasok", description = "Memperbarui data pemasok berdasarkan ID.")
    public Pemasok update(@PathVariable String id, @RequestBody Pemasok pemasok) {
        return service.update(id, pemasok);
    }

    // DELETE - hapus multiple pemasok
    @DeleteMapping("/bulk")
    @Operation(summary = "Menghapus pemasok secara bulk", description = "Menghapus banyak pemasok berdasarkan daftar ID.")
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }

    // DELETE - hapus pemasok
    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus pemasok", description = "Menghapus satu pemasok berdasarkan ID.")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}