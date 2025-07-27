package com.berkmermer.ogrencikayit.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DersTest {

    @Test
    public void testDersConstructor() {
        Ders ders = new Ders();
        assertNotNull(ders);
    }

    @Test
    public void testDersSettersAndGetters() {
        Ders ders = new Ders();
        
        ders.setId(1L);
        ders.setDersKodu("CS101");
        ders.setDersAdi("Bilgisayar Programlama");
        ders.setKredi(3);
        ders.setKontenjan(30);
        ders.setKapasite(25);
        ders.setGun("Pazartesi");
        ders.setSaat("09:00-12:00");
        ders.setSinif("A101");
        ders.setOgretmenAdi("Dr. Ahmet Yılmaz");
        ders.setOgretmenId(1L);
        ders.setOnKosulDersId(0L);
        ders.setMinOgrenciSayisi(10);
        ders.setDurum("AKTIF");
        
        assertEquals(1L, ders.getId());
        assertEquals("CS101", ders.getDersKodu());
        assertEquals("Bilgisayar Programlama", ders.getDersAdi());
        assertEquals(3, ders.getKredi());
        assertEquals(30, ders.getKontenjan());
        assertEquals(25, ders.getKapasite());
        assertEquals("Pazartesi", ders.getGun());
        assertEquals("09:00-12:00", ders.getSaat());
        assertEquals("A101", ders.getSinif());
        assertEquals("Dr. Ahmet Yılmaz", ders.getOgretmenAdi());
        assertEquals(1L, ders.getOgretmenId());
        assertEquals(0L, ders.getOnKosulDersId());
        assertEquals(10, ders.getMinOgrenciSayisi());
        assertEquals("AKTIF", ders.getDurum());
    }

    @Test
    public void testDersKontenjanKontrol() {
        Ders ders = new Ders();
        ders.setKontenjan(30);
        ders.setKapasite(25);
        
        assertTrue(ders.getKontenjan() > ders.getKapasite());
        assertEquals(5, ders.getKontenjan() - ders.getKapasite());
    }
} 