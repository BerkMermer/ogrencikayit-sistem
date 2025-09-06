package com.berkmermer.ogrencikayit.service;

import com.berkmermer.ogrencikayit.dao.KayitDao;
import com.berkmermer.ogrencikayit.dao.DersDao;
import com.berkmermer.ogrencikayit.dao.NotDao;
import com.berkmermer.ogrencikayit.dao.OgrenciDao;
import com.berkmermer.ogrencikayit.model.Ders;
import com.berkmermer.ogrencikayit.model.Ogrenci;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KayitServiceTest {

    @Mock
    private KayitDao kayitDao;

    @Mock
    private DersDao dersDao;

    @Mock
    private NotDao notDao;

    @Mock
    private OgrenciDao ogrenciDao;

    @InjectMocks
    private KayitService kayitService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOgrenciyiDerseKaydetBasari() {
        Ogrenci ogrenci = new Ogrenci();
        ogrenci.setId(1L);
        ogrenci.setAd("Ahmet");

        Ders ders = new Ders();
        ders.setId(1L);
        ders.setKapasite(30);
        ders.setKredi(3);
        ders.setDurum("AKTIF");
        ders.setGun("Pazartesi");
        ders.setSaat("09:00-12:00");

        when(ogrenciDao.findById(1L)).thenReturn(ogrenci);
        when(dersDao.findById(1L)).thenReturn(ders);
        when(kayitDao.ogrenciDerseKayitliMi(1L, 1L)).thenReturn(false);
        when(kayitDao.derseKayitliOgrenciSayisi(1L)).thenReturn(25);
        when(kayitDao.ogrencininToplamKredisi(1L)).thenReturn(20);
        when(notDao.ogrencininDersTekrarSayisi(1L, 1L)).thenReturn(0);
        when(kayitDao.zamanCakismasiVarMi(1L, "Pazartesi", "09:00-12:00")).thenReturn(false);
        doNothing().when(kayitDao).kayitEkle(1L, 1L, "2025-1", "BEKLIYOR", 1L);
        doNothing().when(dersDao).guncelleKontenjan(1L, -1);

        String result = kayitService.ogrenciyiDerseKaydet(1L, 1L, "2025-1", 1L);

        assertEquals("Kayıt başarılı!", result);
        verify(kayitDao).kayitEkle(1L, 1L, "2025-1", "BEKLIYOR", 1L);
        verify(dersDao).guncelleKontenjan(1L, -1);
    }

    @Test
    public void testOgrenciyiDerseKaydetKontenjanDolu() {
        Ogrenci ogrenci = new Ogrenci();
        ogrenci.setId(1L);

        Ders ders = new Ders();
        ders.setId(1L);
        ders.setKapasite(30);
        ders.setKredi(3);
        ders.setDurum("AKTIF");

        when(ogrenciDao.findById(1L)).thenReturn(ogrenci);
        when(dersDao.findById(1L)).thenReturn(ders);
        when(kayitDao.ogrenciDerseKayitliMi(1L, 1L)).thenReturn(false);
        when(kayitDao.derseKayitliOgrenciSayisi(1L)).thenReturn(30);

        String result = kayitService.ogrenciyiDerseKaydet(1L, 1L, "2025-1", 1L);

        assertEquals("Ders kontenjanı dolu!", result);
        verify(kayitDao, never()).kayitEkle(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    @Test
    public void testOgrenciyiDerseKaydetZatenKayitli() {
        Ogrenci ogrenci = new Ogrenci();
        ogrenci.setId(1L);

        Ders ders = new Ders();
        ders.setId(1L);
        ders.setKapasite(30);
        ders.setKredi(3);
        ders.setDurum("AKTIF");

        when(ogrenciDao.findById(1L)).thenReturn(ogrenci);
        when(dersDao.findById(1L)).thenReturn(ders);
        when(kayitDao.ogrenciDerseKayitliMi(1L, 1L)).thenReturn(true);

        String result = kayitService.ogrenciyiDerseKaydet(1L, 1L, "2025-1", 1L);

        assertEquals("Öğrenci zaten bu derse kayıtlı!", result);
        verify(kayitDao, never()).kayitEkle(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    @Test
    public void testOgrenciyiDerseKaydetMaksimumKredi() {
        Ogrenci ogrenci = new Ogrenci();
        ogrenci.setId(1L);

        Ders ders = new Ders();
        ders.setId(1L);
        ders.setKapasite(30);
        ders.setKredi(15);
        ders.setDurum("AKTIF");

        when(ogrenciDao.findById(1L)).thenReturn(ogrenci);
        when(dersDao.findById(1L)).thenReturn(ders);
        when(kayitDao.ogrenciDerseKayitliMi(1L, 1L)).thenReturn(false);
        when(kayitDao.derseKayitliOgrenciSayisi(1L)).thenReturn(30);
        when(kayitDao.ogrencininToplamKredisi(1L)).thenReturn(20);

        String result = kayitService.ogrenciyiDerseKaydet(1L, 1L, "2025-1", 1L);

        assertTrue(result.contains("Maksimum kredi limitini aşıyorsunuz"));
        verify(kayitDao, never()).kayitEkle(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    @Test
    public void testOgrenciyiDerstenCikar() {
        when(kayitDao.ogrenciDerseKayitliMi(1L, 1L)).thenReturn(true);
        doNothing().when(kayitDao).ogrenciyiDerstenCikar(1L, 1L);
        doNothing().when(dersDao).guncelleKontenjan(1L, 1);

        String result = kayitService.ogrenciyiDerstenCikar(1L, 1L);

        assertEquals("Öğrenci dersten çıkarıldı!", result);
        verify(kayitDao).ogrenciyiDerstenCikar(1L, 1L);
        verify(dersDao).guncelleKontenjan(1L, 1);
    }

    @Test
    public void testOgrencininDersleri() {
        Ders ders1 = new Ders();
        ders1.setId(1L);
        ders1.setDersAdi("Matematik");

        Ders ders2 = new Ders();
        ders2.setId(2L);
        ders2.setDersAdi("Fizik");

        List<Ders> dersler = Arrays.asList(ders1, ders2);
        when(kayitDao.ogrencininDersleri(1L)).thenReturn(dersler);

        List<Ders> result = kayitService.ogrencininDersleri(1L);

        assertEquals(2, result.size());
        verify(kayitDao).ogrencininDersleri(1L);
    }
} 