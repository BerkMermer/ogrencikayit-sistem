package com.berkmermer.ogrencikayit.service;

import com.berkmermer.ogrencikayit.dao.NotDao;
import com.berkmermer.ogrencikayit.model.Not;
import com.berkmermer.ogrencikayit.model.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotServiceTest {

    @Mock
    private NotDao notDao;

    @InjectMocks
    private NotService notService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testNotEkle() {
        Not not = new Not();
        not.setOgrenciId(1L);
        not.setDersId(1L);
        not.setVize(80.0);
        not.setFinalNotu(90.0);

        doNothing().when(notDao).notEkle(any(Not.class));

        notService.notEkle(not);

        verify(notDao).notEkle(any(Not.class));
        assertEquals(86.0, not.getOrtalama(), 0.01);
        assertEquals("BA", not.getHarfNotu());
    }

    @Test
    public void testNotGuncelle() {
        Not not = new Not();
        not.setId(1L);
        not.setOgrenciId(1L);
        not.setDersId(1L);
        not.setVize(85.0);
        not.setFinalNotu(95.0);

        doNothing().when(notDao).notGuncelle(any(Not.class));

        notService.notGuncelle(not);

        verify(notDao).notGuncelle(any(Not.class));
        assertEquals(91.0, not.getOrtalama(), 0.01);
        assertEquals("AA", not.getHarfNotu());
    }

    @Test
    public void testOgrencininNotlari() {
        Not not1 = new Not();
        not1.setId(1L);
        not1.setOgrenciId(1L);
        not1.setDersId(1L);

        Not not2 = new Not();
        not2.setId(2L);
        not2.setOgrenciId(1L);
        not2.setDersId(2L);

        List<Not> notlar = Arrays.asList(not1, not2);
        when(notDao.ogrencininNotlari(1L)).thenReturn(notlar);

        List<Not> result = notService.ogrencininNotlari(1L);

        assertEquals(2, result.size());
        verify(notDao).ogrencininNotlari(1L);
    }

    @Test
    public void testDersinNotlari() {
        Not not = new Not();
        not.setId(1L);
        not.setOgrenciId(1L);
        not.setDersId(1L);

        List<Not> notlar = Arrays.asList(not);
        when(notDao.dersinNotlari(1L)).thenReturn(notlar);

        List<Not> result = notService.dersinNotlari(1L);

        assertEquals(1, result.size());
        verify(notDao).dersinNotlari(1L);
    }

    @Test
    public void testHarfNotuHesapla() {
        Not not = new Not();
        not.setVize(95.0);
        not.setFinalNotu(98.0);
        not.setOrtalama(96.8);

        assertEquals("AA", "AA");
    }
} 