package ui.ft.ccit.faculty.transaksi.pelanggan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import ui.ft.ccit.faculty.transaksi.pelanggan.model.Pelanggan;
import ui.ft.ccit.faculty.transaksi.pelanggan.view.PelangganService;

import java.util.List;

@RestController
@RequestMapping("/api/pelanggan")
public class PelangganController {

    private final PelangganService service;

    public PelangganController(PelangganService service) {
        this.service = service;
    }

    // GET list semua pelanggan
    @GetMapping
    @Operation(summary = "Mengambil daftar semua pelanggan", description = "Mengambil seluruh data pelanggan yang tersedia di sistem.\r\n"
            + "Mendukung pagination opsional melalui parameter `page` dan `size`.")
    public List<Pelanggan> list(
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

    // GET satu pelanggan by id
    @GetMapping("/{id}")
    @Operation(summary = "Mengambil detail satu pelanggan", description = "Mengambil detail satu pelanggan berdasarkan ID.")
    public Pelanggan get(@PathVariable String id) {
        return service.getById(id);
    }

    // POST - create pelanggan baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat pelanggan baru", description = "Membuat satu data pelanggan baru ke dalam sistem.")
    public Pelanggan create(@RequestBody Pelanggan pelanggan) {
        return service.save(pelanggan);
    }

    // POST - create pelanggan bulk baru
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat pelanggan secara bulk", description = "Membuat banyak pelanggan baru dalam satu transaksi.")
    public List<Pelanggan> createBulk(@RequestBody List<Pelanggan> pelanggan) {
        return service.saveBulk(pelanggan);
    }

    // PUT - edit/update pelanggan
    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data pelanggan", description = "Memperbarui data pelanggan berdasarkan ID.")
    public Pelanggan update(@PathVariable String id, @RequestBody Pelanggan pelanggan) {
        return service.update(id, pelanggan);
    }

    // DELETE - hapus multiple pelanggan
    @DeleteMapping("/bulk")
    @Operation(summary = "Menghapus pelanggan secara bulk", description = "Menghapus banyak pelanggan berdasarkan daftar ID.")
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }

    // DELETE - hapus pelanggan
    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus pelanggan", description = "Menghapus satu pelanggan berdasarkan ID.")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}