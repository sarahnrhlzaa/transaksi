package ui.ft.ccit.faculty.transaksi.karyawan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Mengambil daftar semua karyawan", description = "Mengambil seluruh data karyawan yang tersedia di sistem.\r\n"
            + "Mendukung pagination opsional melalui parameter `page` dan `size`.")
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
    @Operation(summary = "Mengambil detail satu karyawan", description = "Mengambil detail satu karyawan berdasarkan ID.")
    public Karyawan get(@PathVariable String id) {
        return service.getById(id);
    }

    // POST - create karyawan baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat karyawan baru", description = "Membuat satu data karyawan baru ke dalam sistem.")
    public Karyawan create(@RequestBody Karyawan karyawan) {
        return service.save(karyawan);
    }

    // POST - create karyawan bulk baru
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat karyawan secara bulk", description = "Membuat banyak karyawan baru dalam satu transaksi.")
    public List<Karyawan> createBulk(@RequestBody List<Karyawan> karyawan) {
        return service.saveBulk(karyawan);
    }

    // PUT - edit/update karyawan
    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data karyawan", description = "Memperbarui data karyawan berdasarkan ID.")
    public Karyawan update(@PathVariable String id, @RequestBody Karyawan karyawan) {
        return service.update(id, karyawan);
    }

    // DELETE - hapus multiple karyawan
    @DeleteMapping("/bulk")
    @Operation(summary = "Menghapus karyawan secara bulk", description = "Menghapus banyak karyawan berdasarkan daftar ID.")
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }

    // DELETE - hapus karyawan
    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus karyawan", description = "Menghapus satu karyawan berdasarkan ID.")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}