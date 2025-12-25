package ui.ft.ccit.faculty.transaksi.detailtransaksi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import ui.ft.ccit.faculty.transaksi.detailtransaksi.model.DetailTransaksi;
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
    @Operation(summary = "Mengambil daftar semua detail transaksi", description = "Mengambil seluruh data detail transaksi yang tersedia di sistem.\r\n"
            + "Mendukung pagination opsional melalui parameter `page` dan `size`.")
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

    // GET detail transaksi by composite key
    @GetMapping("/{kodeTransaksi}/{idBarang}")
    @Operation(summary = "Mengambil detail satu item transaksi", description = "Mengambil detail satu item berdasarkan kode transaksi dan ID barang.")
    public DetailTransaksi get(@PathVariable String kodeTransaksi, @PathVariable String idBarang) {
        return service.getById(kodeTransaksi, idBarang);
    }

    // GET semua detail dari satu transaksi
    @GetMapping("/transaksi/{kodeTransaksi}")
    @Operation(summary = "Mengambil semua detail dari satu transaksi", description = "Mengambil semua barang yang dibeli dalam satu transaksi.")
    public List<DetailTransaksi> getByTransaksi(@PathVariable String kodeTransaksi) {
        return service.getByTransaksi(kodeTransaksi);
    }

    // GET semua transaksi yang beli barang tertentu
    @GetMapping("/barang/{idBarang}")
    @Operation(summary = "Mengambil semua transaksi untuk satu barang", description = "Mengambil semua transaksi yang membeli barang tertentu.")
    public List<DetailTransaksi> getByBarang(@PathVariable String idBarang) {
        return service.getByBarang(idBarang);
    }

    // POST - create detail transaksi baru (otomatis update stok)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat detail transaksi baru", description = "Membuat satu detail transaksi baru dan mengurangi stok barang secara otomatis.")
    public DetailTransaksi create(@RequestBody DetailTransaksi detail) {
        return service.save(detail);
    }

    // POST - create detail transaksi bulk (otomatis update stok)
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Membuat detail transaksi secara bulk", description = "Membuat banyak detail transaksi sekaligus dan update stok otomatis.")
    public List<DetailTransaksi> createBulk(@RequestBody List<DetailTransaksi> details) {
        return service.saveBulk(details);
    }

    // PUT - update jumlah (adjust stok otomatis)
    @PutMapping("/{kodeTransaksi}/{idBarang}")
    @Operation(summary = "Memperbarui jumlah barang dalam transaksi", description = "Memperbarui jumlah barang dan menyesuaikan stok secara otomatis.")
    public DetailTransaksi update(@PathVariable String kodeTransaksi, 
                                  @PathVariable String idBarang, 
                                  @RequestBody DetailTransaksi detail) {
        return service.update(kodeTransaksi, idBarang, detail);
    }

    // DELETE - hapus detail (kembalikan stok)
    @DeleteMapping("/{kodeTransaksi}/{idBarang}")
    @Operation(summary = "Menghapus detail transaksi", description = "Menghapus satu detail transaksi dan mengembalikan stok barang.")
    public void delete(@PathVariable String kodeTransaksi, @PathVariable String idBarang) {
        service.delete(kodeTransaksi, idBarang);
    }

    // DELETE - hapus semua detail dari satu transaksi (kembalikan semua stok)
    @DeleteMapping("/transaksi/{kodeTransaksi}")
    @Operation(summary = "Menghapus semua detail dari satu transaksi", description = "Menghapus semua detail transaksi dan mengembalikan semua stok barang.")
    public void deleteByTransaksi(@PathVariable String kodeTransaksi) {
        service.deleteByTransaksi(kodeTransaksi);
    }
}