package ui.ft.ccit.faculty.transaksi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        /*
         * ======================================================
         * GLOBAL METADATA
         * ======================================================
         */
        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("API Sistem Transaksi")
                                                .version("1.0.0")
                                                .description("""
                                                                Dokumentasi API internal Sistem Transaksi.

                                                                Catatan desain:
                                                                - API berbasis REST
                                                                - Pagination opsional
                                                                - Bulk operation bersifat transactional
                                                                - Swagger hanya sebagai dokumentasi manusia
                                                                """)
                                                .contact(new Contact()
                                                                .name("Muhammad Azka Ramadhan")
                                                                .email("m.azka@eng.ui.ac.id")));
        }

        /*
         * ======================================================
         * GROUP: BARANG
         * ======================================================
         */
        @Bean
        public GroupedOpenApi barangApi() {
                return GroupedOpenApi.builder()
                                .group("Barang")
                                .pathsToMatch("/api/barang/**")
                                .build();
        }

        /*
         * ======================================================
         * GROUP: JENIS BARANG
         * ======================================================
         */
        @Bean
        public GroupedOpenApi jenisBarangApi() {
                return GroupedOpenApi.builder()
                                .group("Jenis Barang")
                                .pathsToMatch("/api/jenis-barang/**")
                                .build();
        }

        /*
         * ======================================================
         * GROUP: PEMASOK
         * ======================================================
         */
        @Bean
        public GroupedOpenApi pemasokApi() {
                return GroupedOpenApi.builder()
                                .group("Pemasok")
                                .pathsToMatch("/api/pemasok/**")
                                .build();
        }

        /*
         * ======================================================
         * GROUP: KARYAWAN
         * ======================================================
         */
        @Bean
        public GroupedOpenApi karyawanApi() {
                return GroupedOpenApi.builder()
                                .group("Karyawan")
                                .pathsToMatch("/api/karyawan/**")
                                .build();
        }

        /*
         * ======================================================
         * GROUP: PELANGGAN
         * ======================================================
         */
        @Bean
        public GroupedOpenApi pelangganApi() {
                return GroupedOpenApi.builder()
                                .group("Pelanggan")
                                .pathsToMatch("/api/pelanggan/**")
                                .build();
        }

        /*
         * ======================================================
         * GROUP: TRANSAKSI
         * ======================================================
         */
        @Bean
        public GroupedOpenApi transaksiApi() {
                return GroupedOpenApi.builder()
                                .group("Transaksi")
                                .pathsToMatch("/api/transaksi/**")
                                .build();
        }

        /*
         * ======================================================
         * GROUP: DETAIL TRANSAKSI
         * ======================================================
         */
        @Bean
        public GroupedOpenApi detailTransaksiApi() {
                return GroupedOpenApi.builder()
                                .group("Detail Transaksi")
                                .pathsToMatch("/api/detail-transaksi/**")
                                .build();
        }

}