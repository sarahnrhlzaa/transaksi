package ui.ft.ccit.faculty.transaksi.barang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ui.ft.ccit.faculty.transaksi.barang.model.Barang;
import ui.ft.ccit.faculty.transaksi.barang.model.BarangRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
class BarangRepositoryTest {

        @Autowired
        private BarangRepository barangRepository;

        @Test
        void saveAndFindById_shouldPersistAndLoadBarang() {
                Barang barang = new Barang(
                                "T001",
                                "Sabun Mandi",
                                (short) 10,
                                5000.0,
                                20.0,
                                0.0,
                                (byte) 103,
                                "S003");

                barangRepository.save(barang);

                var found = barangRepository.findById("T001");

                assertThat(found).isPresent();
                assertThat(found.get().getNama()).isEqualTo("Sabun Mandi");
        }

        @Test
        void findByNamaContainingIgnoreCase_shouldReturnMatchingRows() {
                barangRepository.save(
                                new Barang("T001", "Sabun Mandi", (short) 10, 5000.0, 20.0, 0.0, (byte) 103, "S003"));
                barangRepository.save(
                                new Barang("T002", "Shampoo Wangi", (short) 5, 15000.0, 25.0, 0.0, (byte) 103, "S003"));

                List<Barang> hasil = barangRepository.findByNamaContainingIgnoreCase("sham");

                assertThat(hasil)
                                .hasSize(1)
                                .first()
                                .extracting(Barang::getIdBarang)
                                .isEqualTo("T002");
        }

        @AfterEach
        void tearDown() {
                barangRepository.deleteById("T001");
                barangRepository.deleteById("T002");
        }
}
