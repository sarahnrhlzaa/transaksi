package ui.ft.ccit.faculty.transaksi.jenisbarang.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import ui.ft.ccit.faculty.transaksi.jenisbarang.model.JenisBarang;
import ui.ft.ccit.faculty.transaksi.jenisbarang.view.JenisBarangService;

import java.util.List;

@RestController
@RequestMapping("/api/jenis-barang")
public class JenisBarangController {

    private final JenisBarangService service;

    public JenisBarangController(JenisBarangService service) {
        this.service = service;
    }

    // GET list semua jenis barang
    @GetMapping
    @Operation(summary = "Mengambil daftar semua jenis barang", description = "Mengambil seluruh data jenis barang yang tersedia di sistem.\r\n"
            + "Mendukung pagination opsional melalui parameter `page` dan `size`.")
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
    @Operation(summary = "Mengambil detail satu jenis barang", description = "Mengambil detail satu jenis barang berdasarkan ID.")
    public JenisBarang get(@PathVariable Integer id) {
        return service.getById(id);
    }

    // POST - create jenis barang baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat jenis barang baru", description = "Membuat satu data jenis barang baru ke dalam sistem.")
    public JenisBarang create(@RequestBody JenisBarang jenisBarang) {
        return service.save(jenisBarang);
    }

    // POST - create jenis barang bulk baru
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat jenis barang secara bulk", description = "Membuat banyak jenis barang baru dalam satu transaksi.")
    public List<JenisBarang> createBulk(@RequestBody List<JenisBarang> jenisBarang) {
        return service.saveBulk(jenisBarang);
    }

    // PUT - edit/update jenis barang
    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data jenis barang", description = "Memperbarui data jenis barang berdasarkan ID.")
    public JenisBarang update(@PathVariable Integer id, @RequestBody JenisBarang jenisBarang) {
        return service.update(id, jenisBarang);
    }

    // DELETE - hapus multiple jenis barang
    @DeleteMapping("/bulk")
    @Operation(summary = "Menghapus jenis barang secara bulk", description = "Menghapus banyak jenis barang berdasarkan daftar ID.")
    public void deleteBulk(@RequestBody List<Integer> ids) {
        service.deleteBulk(ids);
    }

    // DELETE - hapus jenis barang
    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus jenis barang", description = "Menghapus satu jenis barang berdasarkan ID.")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}