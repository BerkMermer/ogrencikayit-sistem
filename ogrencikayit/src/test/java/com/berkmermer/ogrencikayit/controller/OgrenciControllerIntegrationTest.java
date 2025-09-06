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
public class OgrenciControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long testOgrenciId;

    @BeforeEach
    public void setupTestData() {
        jdbcTemplate.execute("INSERT INTO ogrenciler (ad, soyad, ogrenci_no, email, telefon) VALUES ('Test', 'Ogrenci', '2024001', 'test@test.com', '5551234567')");
        
        testOgrenciId = jdbcTemplate.queryForObject("SELECT id FROM ogrenciler WHERE ogrenci_no = '2024001'", Long.class);
    }

    @Test
    public void testTumOgrencileriGetir() throws Exception {
        mockMvc.perform(get("/ogrenciler")
            .param("rol", "ADMIN"))
            .andExpect(status().isOk());
    }

    @Test
    public void testTumOgrencileriGetirYanlisRol() throws Exception {
        mockMvc.perform(get("/ogrenciler")
            .param("rol", "OGRENCI"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testOgrenciGetirById() throws Exception {
        mockMvc.perform(get("/ogrenciler/" + testOgrenciId)
            .param("rol", "ADMIN"))
            .andExpect(status().isOk());
    }

    @Test
    public void testOgrenciGetirByIdYanlisRol() throws Exception {
        mockMvc.perform(get("/ogrenciler/" + testOgrenciId)
            .param("rol", "OGRENCI"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testOgrenciGetirByIdOlmayanOgrenci() throws Exception {
        mockMvc.perform(get("/ogrenciler/999")
            .param("rol", "ADMIN"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testOgrenciEkle() throws Exception {
        String ogrenciJson = """
            {
                "ad": "Yeni",
                "soyad": "Ogrenci",
                "ogrenciNo": "2024002",
                "email": "yeni@test.com",
                "telefon": "5559876543"
            }
            """;

        mockMvc.perform(post("/ogrenciler")
            .param("rol", "ADMIN")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ogrenciJson))
            .andExpect(status().isOk());
    }

    @Test
    public void testOgrenciEkleYanlisRol() throws Exception {
        String ogrenciJson = """
            {
                "ad": "Yeni",
                "soyad": "Ogrenci",
                "ogrenciNo": "2024002"
            }
            """;

        mockMvc.perform(post("/ogrenciler")
            .param("rol", "OGRENCI")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ogrenciJson))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testOgrenciGuncelle() throws Exception {
        String ogrenciJson = """
            {
                "ad": "Guncellenen",
                "soyad": "Ogrenci",
                "ogrenciNo": "2024001",
                "email": "guncellenen@test.com",
                "telefon": "5551234567"
            }
            """;

        mockMvc.perform(put("/ogrenciler/" + testOgrenciId)
            .param("rol", "ADMIN")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ogrenciJson))
            .andExpect(status().isOk());
    }

    @Test
    public void testOgrenciSil() throws Exception {
        mockMvc.perform(delete("/ogrenciler/" + testOgrenciId)
            .param("rol", "ADMIN"))
            .andExpect(status().isOk());
    }

    @Test
    public void testOgrenciSilYanlisRol() throws Exception {
        mockMvc.perform(delete("/ogrenciler/" + testOgrenciId)
            .param("rol", "OGRETMEN"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testRolParametresiEksik() throws Exception {
        mockMvc.perform(get("/ogrenciler"))
            .andExpect(status().isBadRequest());
    }
} 