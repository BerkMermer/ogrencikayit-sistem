package com.berkmermer.ogrencikayit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class NotControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setupTestData() {
        jdbcTemplate.execute("INSERT INTO ogrenciler (ad, soyad, ogrenci_no) VALUES ('Test', 'Ogrenci', '2024001')");
        jdbcTemplate.execute("INSERT INTO dersler (ders_adi, kredi) VALUES ('Matematik', 3)");
        jdbcTemplate.execute("INSERT INTO notlar (ogrenci_id, ders_id, vize, final_notu) VALUES (1, 1, 80.0, 85.0)");
    }

    @Test
    public void testOgrenciNotlariniGetir() throws Exception {
        mockMvc.perform(get("/notlar/ogrenci/1")
            .param("rol", "OGRENCI"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Öğrenci notları başarıyla getirildi"));
    }

    @Test
    public void testDersNotlariniGetir() throws Exception {
        mockMvc.perform(get("/notlar/ders/1")
            .param("rol", "OGRETMEN"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Ders notları başarıyla getirildi"));
    }

    @Test
    public void testOgrenciNotlariniGetirYanlisRol() throws Exception {
        mockMvc.perform(get("/notlar/ogrenci/1")
            .param("rol", "YONETICI"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testDersNotlariniGetirYanlisRol() throws Exception {
        mockMvc.perform(get("/notlar/ders/1")
            .param("rol", "OGRENCI"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testOgrenciNotlariniGetirOlmayanOgrenci() throws Exception {
        mockMvc.perform(get("/notlar/ogrenci/999")
            .param("rol", "OGRENCI"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void testDersNotlariniGetirOlmayanDers() throws Exception {
        mockMvc.perform(get("/notlar/ders/999")
            .param("rol", "OGRETMEN"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void testRolParametresiEksik() throws Exception {
        mockMvc.perform(get("/notlar/ogrenci/1"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Rol parametresi gerekli"));
    }

    @Test
    public void testOgrenciNotlariniGetirAdminRol() throws Exception {
        mockMvc.perform(get("/notlar/ogrenci/1")
            .param("rol", "ADMIN"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testDersNotlariniGetirAdminRol() throws Exception {
        mockMvc.perform(get("/notlar/ders/1")
            .param("rol", "ADMIN"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testOgrenciNotlariniGetirOgretmenRol() throws Exception {
        mockMvc.perform(get("/notlar/ogrenci/1")
            .param("rol", "OGRETMEN"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }
} 