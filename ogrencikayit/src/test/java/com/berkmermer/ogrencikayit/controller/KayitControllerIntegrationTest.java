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
public class KayitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long testOgrenciId;
    private Long testDersId;

    @BeforeEach
    public void setupTestData() {
        jdbcTemplate.execute("INSERT INTO ogrenciler (ad, soyad, ogrenci_no, email) VALUES ('Test', 'Ogrenci', '2024001', 'test@test.com')");
        jdbcTemplate.execute("INSERT INTO dersler (ders_adi, ders_kodu, kredi, kontenjan, durum) VALUES ('Matematik', 'MAT101', 3, 30, 'ACIK')");
        
        testOgrenciId = jdbcTemplate.queryForObject("SELECT id FROM ogrenciler WHERE ogrenci_no = '2024001'", Long.class);
        testDersId = jdbcTemplate.queryForObject("SELECT id FROM dersler WHERE ders_kodu = 'MAT101'", Long.class);
    }

    @Test
    public void testOgrenciKayitOl() throws Exception {
        String kayitJson = """
            {
                "ogrenciId": %d,
                "dersId": %d
            }
            """.formatted(testOgrenciId, testDersId);

        mockMvc.perform(post("/kayitlar")
            .param("rol", "OGRENCI")
            .contentType(MediaType.APPLICATION_JSON)
            .content(kayitJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testOgrenciKayitOlYanlisRol() throws Exception {
        String kayitJson = """
            {
                "ogrenciId": %d,
                "dersId": %d
            }
            """.formatted(testOgrenciId, testDersId);

        mockMvc.perform(post("/kayitlar")
            .param("rol", "ADMIN")
            .contentType(MediaType.APPLICATION_JSON)
            .content(kayitJson))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testOgrenciKayitIptal() throws Exception {
        String kayitJson = """
            {
                "ogrenciId": %d,
                "dersId": %d
            }
            """.formatted(testOgrenciId, testDersId);

        mockMvc.perform(post("/kayitlar")
            .param("rol", "OGRENCI")
            .contentType(MediaType.APPLICATION_JSON)
            .content(kayitJson));

        mockMvc.perform(delete("/kayitlar/1")
            .param("rol", "OGRENCI"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testOgrenciKayitIptalYanlisRol() throws Exception {
        mockMvc.perform(delete("/kayitlar/1")
            .param("rol", "OGRETMEN"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testOgrenciKayitlariGetir() throws Exception {
        mockMvc.perform(get("/kayitlar/ogrenci/" + testOgrenciId)
            .param("rol", "OGRENCI"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testOgrenciKayitlariGetirYanlisRol() throws Exception {
        mockMvc.perform(get("/kayitlar/ogrenci/" + testOgrenciId)
            .param("rol", "YONETICI"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testDersKayitlariGetir() throws Exception {
        mockMvc.perform(get("/kayitlar/ders/" + testDersId)
            .param("rol", "OGRETMEN"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testDersKayitlariGetirYanlisRol() throws Exception {
        mockMvc.perform(get("/kayitlar/ders/" + testDersId)
            .param("rol", "OGRENCI"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testTumKayitlariGetir() throws Exception {
        mockMvc.perform(get("/kayitlar")
            .param("rol", "ADMIN"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testTumKayitlariGetirYanlisRol() throws Exception {
        mockMvc.perform(get("/kayitlar")
            .param("rol", "OGRENCI"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testKayitKontrol() throws Exception {
        mockMvc.perform(get("/kayitlar/kontrol")
            .param("ogrenciId", testOgrenciId.toString())
            .param("dersId", testDersId.toString())
            .param("rol", "OGRENCI"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testKayitKontrolYanlisRol() throws Exception {
        mockMvc.perform(get("/kayitlar/kontrol")
            .param("ogrenciId", testOgrenciId.toString())
            .param("dersId", testDersId.toString())
            .param("rol", "YONETICI"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testRolParametresiEksik() throws Exception {
        mockMvc.perform(get("/kayitlar"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Rol parametresi gerekli"));
    }

    @Test
    public void testOlmayanOgrenciKayit() throws Exception {
        String kayitJson = """
            {
                "ogrenciId": 999,
                "dersId": %d
            }
            """.formatted(testDersId);

        mockMvc.perform(post("/kayitlar")
            .param("rol", "OGRENCI")
            .contentType(MediaType.APPLICATION_JSON)
            .content(kayitJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testOlmayanDersKayit() throws Exception {
        String kayitJson = """
            {
                "ogrenciId": %d,
                "dersId": 999
            }
            """.formatted(testOgrenciId);

        mockMvc.perform(post("/kayitlar")
            .param("rol", "OGRENCI")
            .contentType(MediaType.APPLICATION_JSON)
            .content(kayitJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false));
    }
} 