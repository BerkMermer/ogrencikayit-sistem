package com.berkmermer.ogrencikayit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class DersControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long testDersId;

    @BeforeEach
    public void setupTestData() {
        jdbcTemplate.execute("INSERT INTO dersler (ders_adi, ders_kodu, kredi, ogretmen_adi, kontenjan, durum) VALUES ('Matematik', 'MAT101', 3, 'Dr. Ahmet', 30, 'ACIK')");
        
        testDersId = jdbcTemplate.queryForObject("SELECT id FROM dersler WHERE ders_kodu = 'MAT101'", Long.class);
    }

    @Test
    public void testTumDersleriGetir() throws Exception {
        mockMvc.perform(get("/dersler")
            .param("rol", "ADMIN"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Dersler başarıyla getirildi"));
    }

    @Test
    public void testTumDersleriGetirOgrenciRol() throws Exception {
        mockMvc.perform(get("/dersler")
            .param("rol", "OGRENCI"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testTumDersleriGetirYanlisRol() throws Exception {
        mockMvc.perform(get("/dersler")
            .param("rol", "YONETICI"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testDersGetirById() throws Exception {
        mockMvc.perform(get("/dersler/" + testDersId)
            .param("rol", "ADMIN"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testDersGetirByIdOgrenciRol() throws Exception {
        mockMvc.perform(get("/dersler/" + testDersId)
            .param("rol", "OGRENCI"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testDersGetirByIdYanlisRol() throws Exception {
        mockMvc.perform(get("/dersler/" + testDersId)
            .param("rol", "YONETICI"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testDersGetirByIdOlmayanDers() throws Exception {
        mockMvc.perform(get("/dersler/999")
            .param("rol", "ADMIN"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testDersEkle() throws Exception {
        String dersJson = """
            {
                "dersAdi": "Fizik",
                "dersKodu": "FIZ101",
                "kredi": 4,
                "ogretmenAdi": "Dr. Mehmet",
                "kontenjan": 25,
                "durum": "ACIK"
            }
            """;

        mockMvc.perform(post("/dersler")
            .param("rol", "ADMIN")
            .contentType(MediaType.APPLICATION_JSON)
            .content(dersJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testDersEkleYanlisRol() throws Exception {
        String dersJson = """
            {
                "dersAdi": "Fizik",
                "dersKodu": "FIZ101",
                "kredi": 4
            }
            """;

        mockMvc.perform(post("/dersler")
            .param("rol", "OGRENCI")
            .contentType(MediaType.APPLICATION_JSON)
            .content(dersJson))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testDersGuncelle() throws Exception {
        String dersJson = """
            {
                "dersAdi": "Guncellenen Matematik",
                "dersKodu": "MAT101",
                "kredi": 4,
                "ogretmenAdi": "Dr. Yeni",
                "kontenjan": 35
            }
            """;

        mockMvc.perform(put("/dersler/" + testDersId)
            .param("rol", "ADMIN")
            .contentType(MediaType.APPLICATION_JSON)
            .content(dersJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testDersSil() throws Exception {
        mockMvc.perform(delete("/dersler/" + testDersId)
            .param("rol", "ADMIN"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testDersSilYanlisRol() throws Exception {
        mockMvc.perform(delete("/dersler/" + testDersId)
            .param("rol", "OGRETMEN"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testRolParametresiEksik() throws Exception {
        mockMvc.perform(get("/dersler"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Rol parametresi gerekli"));
    }
} 