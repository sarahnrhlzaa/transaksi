package ui.ft.ccit.faculty.transaksi.transaksi.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import ui.ft.ccit.faculty.transaksi.transaksi.model.Transaksi;
import ui.ft.ccit.faculty.transaksi.transaksi.view.TransaksiService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transaksi")
public class TransaksiController {

    private final TransaksiService service;

    public TransaksiController(TransaksiService service) {
        this.service = service;
    }

    // GET list semua transaksi
    @GetMapping
    @Operation(summary = "Mengambil daftar semua transaksi", description = "Mengambil seluruh data transaksi yang tersedia di sistem.\r\n"
            + "Mendukung pagination opsional melalui parameter `page` dan `size`.")
    public List<Transaksi> list(
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

    // GET satu transaksi by id
    @GetMapping("/{id}")
    @Operation(summary = "Mengambil detail satu transaksi", description = "Mengambil detail satu transaksi berdasarkan kode.")
    public Transaksi get(@PathVariable String id) {
        return service.getById(id);
    }

    // GET transaksi by pelanggan
    @GetMapping("/pelanggan/{idPelanggan}")
    @Operation(summary = "Mengambil transaksi berdasarkan pelanggan", description = "Mengambil semua transaksi dari satu pelanggan.")
    public List<Transaksi> getByPelanggan(@PathVariable String idPelanggan) {
        return service.getByPelanggan(idPelanggan);
    }

    // GET transaksi by karyawan
    @GetMapping("/karyawan/{idKaryawan}")
    @Operation(summary = "Mengambil transaksi berdasarkan karyawan", description = "Mengambil semua transaksi yang dilayani oleh satu karyawan.")
    public List<Transaksi> getByKaryawan(@PathVariable String idKaryawan) {
        return service.getByKaryawan(idKaryawan);
    }

    // GET transaksi by date range
    @GetMapping("/date-range")
    @Operation(summary = "Mengambil transaksi berdasarkan rentang tanggal", description = "Mengambil transaksi dalam rentang waktu tertentu.")
    public List<Transaksi> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return service.getByDateRange(start, end);
    }

    // POST - create transaksi baru
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat transaksi baru", description = "Membuat satu data transaksi baru ke dalam sistem.")
    public Transaksi create(@RequestBody Transaksi transaksi) {
        return service.save(transaksi);
    }

    // POST - create transaksi bulk baru
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat transaksi secara bulk", description = "Membuat banyak transaksi baru dalam satu operasi.")
    public List<Transaksi> createBulk(@RequestBody List<Transaksi> transaksi) {
        return service.saveBulk(transaksi);
    }

    // PUT - edit/update transaksi
    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data transaksi", description = "Memperbarui data transaksi berdasarkan kode.")
    public Transaksi update(@PathVariable String id, @RequestBody Transaksi transaksi) {
        return service.update(id, transaksi);
    }

    // DELETE - hapus multiple transaksi
    @DeleteMapping("/bulk")
    @Operation(summary = "Menghapus transaksi secara bulk", description = "Menghapus banyak transaksi berdasarkan daftar kode.")
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }

    // DELETE - hapus transaksi
    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus transaksi", description = "Menghapus satu transaksi berdasarkan kode.")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}