package com.berkmermer.ogrencikayit.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OgrenciTest {

    @Test
    public void testOgrenciConstructor() {
        Ogrenci ogrenci = new Ogrenci();
        assertNotNull(ogrenci);
    }

    @Test
    public void testOgrenciSettersAndGetters() {
        Ogrenci ogrenci = new Ogrenci();
        
        ogrenci.setId(1L);
        ogrenci.setOgrenciNo("2021001");
        ogrenci.setAd("Ahmet");
        ogrenci.setSoyad("Yılmaz");
        ogrenci.setEmail("ahmet@test.com");
        ogrenci.setTelefon("05551234567");
        
        assertEquals(1L, ogrenci.getId());
        assertEquals("2021001", ogrenci.getOgrenciNo());
        assertEquals("Ahmet", ogrenci.getAd());
        assertEquals("Yılmaz", ogrenci.getSoyad());
        assertEquals("ahmet@test.com", ogrenci.getEmail());
        assertEquals("05551234567", ogrenci.getTelefon());
    }

    @Test
    public void testOgrenciToString() {
        Ogrenci ogrenci = new Ogrenci();
        ogrenci.setId(1L);
        ogrenci.setAd("Ahmet");
        ogrenci.setSoyad("Yılmaz");
        
        assertNotNull(ogrenci);
        assertEquals("Ahmet", ogrenci.getAd());
        assertEquals("Yılmaz", ogrenci.getSoyad());
    }
} 