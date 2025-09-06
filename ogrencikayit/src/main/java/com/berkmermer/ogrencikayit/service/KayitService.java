package com.berkmermer.ogrencikayit.service;

import com.berkmermer.ogrencikayit.dao.DersDao;
import com.berkmermer.ogrencikayit.dao.KayitDao;
import com.berkmermer.ogrencikayit.dao.OgrenciDao;
import com.berkmermer.ogrencikayit.dao.NotDao;
import com.berkmermer.ogrencikayit.model.Ders;
import com.berkmermer.ogrencikayit.model.Ogrenci;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class KayitService {
    @Autowired
    private KayitDao kayitDao;
    
    @Autowired
    private DersDao dersDao;
    
    @Autowired
    private OgrenciDao ogrenciDao;

    @Autowired
    private NotDao notDao;

    private static final LocalDate KAYIT_BASLANGIC = LocalDate.of(2025, 7, 1);
    private static final LocalDate KAYIT_BITIS = LocalDate.of(2025, 12, 31);
    private static final int MAKSIMUM_KREDI = 30;

    public String ogrenciyiDerseKaydet(Long ogrenciId, Long dersId, String donem, Long danismanOgretmenId) {
        try {
            Ogrenci ogrenci = ogrenciDao.findById(ogrenciId);
            Ders ders = dersDao.findById(dersId);
            
            if (ogrenci == null) return "Öğrenci bulunamadı! (ID: " + ogrenciId + ")";
            if (ders == null) return "Ders bulunamadı! (ID: " + dersId + ")";

            if ("KAPALI".equalsIgnoreCase(ders.getDurum())) {
                return "Bu derse kayıt alınamıyor, ders kapalı!";
            }

            if (kayitDao.ogrenciDerseKayitliMi(ogrenciId, dersId)) {
                return "Öğrenci zaten bu derse kayıtlı!";
            }

            int kayitliOgrenciSayisi = kayitDao.derseKayitliOgrenciSayisi(dersId);
            if (kayitliOgrenciSayisi >= ders.getKapasite()) {
                return "Ders kontenjanı dolu!";
            }

            int ogrencininToplamKredisi = kayitDao.ogrencininToplamKredisi(ogrenciId);
            if (ogrencininToplamKredisi + ders.getKredi() > MAKSIMUM_KREDI) {
                return "Maksimum kredi limitini aşıyorsunuz! Mevcut: " + ogrencininToplamKredisi + 
                       ", Limit: " + MAKSIMUM_KREDI + ", Ders kredisi: " + ders.getKredi();
            }

            int tekrarSayisi = notDao.ogrencininDersTekrarSayisi(ogrenciId, dersId);
            if (tekrarSayisi >= 3) {
                return "Bu dersi 3 defadan fazla FF ile geçemediniz, tekrar kayıt olamazsınız!";
            }

            if (kayitDao.zamanCakismasiVarMi(ogrenciId, ders.getGun(), ders.getSaat())) {
                return "Bu ders saati ile çakışan başka bir dersiniz var!";
            }

            kayitDao.kayitEkle(ogrenciId, dersId, donem, "BEKLIYOR", danismanOgretmenId);
            dersDao.guncelleKontenjan(dersId, -1);
            return "Kayıt başarılı!";
        } catch (Exception e) {
            return "Kayıt sırasında hata oluştu: " + e.getMessage();
        }
    }

    public void kayitOnayla(Long kayitId) {
        kayitDao.kayitOnayla(kayitId);
    }

    public void kayitReddet(Long kayitId) {
        kayitDao.kayitReddet(kayitId);
    }

    public String ogrenciyiDerstenCikar(Long ogrenciId, Long dersId) {
        if (!kayitDao.ogrenciDerseKayitliMi(ogrenciId, dersId)) {
            return "Öğrenci bu derse kayıtlı değil!";
        }
        
        kayitDao.ogrenciyiDerstenCikar(ogrenciId, dersId);
        dersDao.guncelleKontenjan(dersId, 1);
        return "Öğrenci dersten çıkarıldı!";
    }

    public List<Ders> ogrencininDersleri(Long ogrenciId) {
        return kayitDao.ogrencininDersleri(ogrenciId);
    }

    public List<Ogrenci> derseKayitliOgrenciler(Long dersId) {
        return kayitDao.derseKayitliOgrenciler(dersId);
    }
}


