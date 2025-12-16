package ui.ft.ccit.faculty.transaksi.jenisbarang;

import org.springframework.web.bind.annotation.*;

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
    public List<JenisBarang> list() {
        return service.getAll();
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
    public JenisBarang create(@RequestBody JenisBarang jenisBarang) {
        return service.save(jenisBarang);
    }

    // PUT - edit/update jenis barang
    @PutMapping("/{id}")
    public JenisBarang update(@PathVariable Byte id, @RequestBody JenisBarang jenisBarang) {
        return service.update(id, jenisBarang);
    }

    // DELETE - hapus jenis barang
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Byte id) {
        service.delete(id);
    }
}