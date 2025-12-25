package ui.ft.ccit.faculty.transaksi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;

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
         * ENTITY REGISTRY (1 BARIS = 1 TABEL)
         * 
         * SESUAIKAN DENGAN NAMA PATH YANG ADA DI REQUEST MAPPING. Contoh:
         * static final List<String> ENTITIES = List.of(
         * "barang",
         * "pemasok",
         * "pelanggan",
         * "transaksi");
         * 
         * 
         * ======================================================
         */
        static final List<String> ENTITIES = List.of(
                        "barang",
                        "jenisbarang",
                        "pemasok",
                        "karyawan",
                        "pelanggan",
                        "transaksi",
                        "detailtransaksi");

        /*
         * ======================================================
         * AUTO GROUP PER ENTITY
         * ======================================================
         */
        @Bean
        public List<GroupedOpenApi> entityGroups() {
                return ENTITIES.stream()
                                .map(entity -> GroupedOpenApi.builder()
                                                .group(capitalize(entity))
                                                .pathsToMatch("/api/" + entity + "/**")
                                                .addOpenApiCustomizer(customizer(entity))
                                                .build())
                                .toList();
        }

        /*
         * ======================================================
         * CUSTOMIZER
         * ======================================================
         */
        private OpenApiCustomizer customizer(String entity) {
                return openAPI -> {
                        if (openAPI.getPaths() == null)
                                return;

                        openAPI.getPaths().forEach((path, item) -> {
                                enrich(entity, path, "GET", item.getGet());
                                enrich(entity, path, "POST", item.getPost());
                                enrich(entity, path, "PUT", item.getPut());
                                enrich(entity, path, "DELETE", item.getDelete());
                        });
                };
        }

        /*
         * ======================================================
         * ENRICHMENT CORE
         * ======================================================
         */
        private void enrich(String entity, String rawPath, String method, Operation op) {
                if (op == null)
                        return;
                if (alreadyDocumented(op))
                        return;

                String normalized = normalizePath(rawPath);
                EndpointDoc doc = EndpointDoc.match(entity, method, normalized);

                if (doc == null)
                        return;

                op.setSummary(doc.summary(entity));
                op.setDescription(doc.description(entity));
        }

        private boolean alreadyDocumented(Operation op) {
                return StringUtils.hasText(op.getSummary())
                                || StringUtils.hasText(op.getDescription());
        }

        private String normalizePath(String path) {
                return path.endsWith("/") && path.length() > 1
                                ? path.substring(0, path.length() - 1)
                                : path;
        }

        private String capitalize(String s) {
                return s.substring(0, 1).toUpperCase() + s.substring(1);
        }

        /*
         * ======================================================
         * GENERIC DOCUMENTATION REGISTRY
         * ======================================================
         */
        enum EndpointDoc {

                GET_ALL("GET", "/api/{resource}"),
                GET_ONE("GET", "/api/{resource}/{id}"),
                SEARCH("GET", "/api/{resource}/search"),
                CREATE("POST", "/api/{resource}"),
                BULK_CREATE("POST", "/api/{resource}/bulk"),
                UPDATE("PUT", "/api/{resource}/{id}"),
                DELETE("DELETE", "/api/{resource}/{id}"),
                BULK_DELETE("DELETE", "/api/{resource}/bulk");

                private final String method;
                private final String pathPattern;

                EndpointDoc(String method, String pathPattern) {
                        this.method = method;
                        this.pathPattern = pathPattern;
                }

                static EndpointDoc match(String resource, String method, String path) {
                        for (EndpointDoc doc : values()) {
                                String expected = doc.pathPattern.replace("{resource}", resource);
                                if (doc.method.equals(method) && expected.equals(path)) {
                                        return doc;
                                }
                        }
                        return null;
                }

                String summary(String resource) {
                        return switch (this) {
                                case GET_ALL -> "Mengambil daftar semua " + resource;
                                case GET_ONE -> "Mengambil detail satu " + resource;
                                case SEARCH -> "Mencari " + resource;
                                case CREATE -> "Membuat " + resource + " baru";
                                case BULK_CREATE -> "Membuat " + resource + " secara bulk";
                                case UPDATE -> "Memperbarui " + resource;
                                case DELETE -> "Menghapus " + resource;
                                case BULK_DELETE -> "Menghapus " + resource + " secara bulk";
                        };
                }

                String description(String resource) {
                        return switch (this) {
                                case GET_ALL ->
                                        "Mengambil seluruh data " + resource + " yang tersedia di sistem.";
                                case GET_ONE ->
                                        "Mengambil detail satu " + resource + " berdasarkan ID.";
                                case SEARCH ->
                                        "Mencari " + resource + " berdasarkan kata kunci tertentu.";
                                case CREATE ->
                                        "Membuat satu data " + resource + " baru di sistem.";
                                case BULK_CREATE ->
                                        "Membuat banyak data " + resource + " dalam satu transaksi.";
                                case UPDATE ->
                                        "Memperbarui data " + resource + " berdasarkan ID.";
                                case DELETE ->
                                        "Menghapus satu data " + resource + " berdasarkan ID.";
                                case BULK_DELETE ->
                                        "Menghapus banyak data " + resource + " berdasarkan daftar ID.";
                        };
                }
        }
}