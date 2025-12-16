package ui.ft.ccit.faculty.transaksi.jenisbarang;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
class JenisBarangRepositoryTest {

    @Autowired
    private JenisBarangRepository jenisBarangRepository;

    @Test
    void saveAndFindById_shouldPersistAndLoadJenisBarang() {
        JenisBarang jenis = new JenisBarang("Makanan");

        JenisBarang saved = jenisBarangRepository.save(jenis);

        var found = jenisBarangRepository.findById(saved.getIdJenisBarang());

        assertThat(found).isPresent();
        assertThat(found.get().getNamaJenis()).isEqualTo("Makanan");
    }

    @Test
    void findByNamaJenisContainingIgnoreCase_shouldReturnMatchingRows() {
        JenisBarang jenis1 = jenisBarangRepository.save(new JenisBarang("Makanan"));
        JenisBarang jenis2 = jenisBarangRepository.save(new JenisBarang("Minuman"));

        List<JenisBarang> hasil = jenisBarangRepository.findByNamaJenisContainingIgnoreCase("minum");

        assertThat(hasil)
                .hasSize(1)
                .first()
                .extracting(JenisBarang::getNamaJenis)
                .isEqualTo("Minuman");

        // cleanup
        jenisBarangRepository.deleteById(jenis1.getIdJenisBarang());
        jenisBarangRepository.deleteById(jenis2.getIdJenisBarang());
    }

    @Test
    void deleteById_shouldRemoveJenisBarang() {
        JenisBarang jenis = jenisBarangRepository.save(new JenisBarang("Elektronik"));
        Byte id = jenis.getIdJenisBarang();

        jenisBarangRepository.deleteById(id);

        assertThat(jenisBarangRepository.findById(id)).isEmpty();
    }
}