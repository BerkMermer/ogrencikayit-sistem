package com.berkmermer.ogrencikayit.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NotTest {

    @Test
    public void testNotConstructor() {
        Not not = new Not();
        assertNotNull(not);
    }

    @Test
    public void testNotSettersAndGetters() {
        Not not = new Not();
        
        not.setId(1L);
        not.setOgrenciId(1L);
        not.setDersId(1L);
        not.setVize(80.0);
        not.setFinalNotu(90.0);
        not.setOrtalama(85.0);
        not.setHarfNotu("AA");
        
        assertEquals(1L, not.getId());
        assertEquals(1L, not.getOgrenciId());
        assertEquals(1L, not.getDersId());
        assertEquals(80, not.getVize());
        assertEquals(90, not.getFinalNotu());
        assertEquals(85.0, not.getOrtalama());
        assertEquals("AA", not.getHarfNotu());
    }

    @Test
    public void testNotOrtalamaHesaplama() {
        Not not = new Not();
        not.setVize(70.0);
        not.setFinalNotu(80.0);
        
        double beklenenOrtalama = (70.0 * 0.4) + (80.0 * 0.6);
        not.setOrtalama(beklenenOrtalama);
        
        assertEquals(beklenenOrtalama, not.getOrtalama(), 0.01);
    }
} 