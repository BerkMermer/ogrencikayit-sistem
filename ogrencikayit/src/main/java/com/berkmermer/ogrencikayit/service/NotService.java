package com.berkmermer.ogrencikayit.service;

import com.berkmermer.ogrencikayit.dao.NotDao;
import com.berkmermer.ogrencikayit.model.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotService {
    @Autowired
    private NotDao notDao;

    public void notEkle(Not not) {
        double ortalama = not.getVize() * 0.4 + not.getFinalNotu() * 0.6;
        not.setOrtalama(ortalama);
        not.setHarfNotu(harfNotuHesapla(ortalama));
        notDao.notEkle(not);
    }

    public void notGuncelle(Not not) {
        double ortalama = not.getVize() * 0.4 + not.getFinalNotu() * 0.6;
        not.setOrtalama(ortalama);
        not.setHarfNotu(harfNotuHesapla(ortalama));
        notDao.notGuncelle(not);
    }

    public void notSil(Long id) {
        notDao.notSil(id);
    }

    public List<Not> ogrencininNotlari(Long ogrenciId) {
        return notDao.ogrencininNotlari(ogrenciId);
    }

    public List<Not> dersinNotlari(Long dersId) {
        return notDao.dersinNotlari(dersId);
    }

    public List<Not> tumNotlar() {
        return notDao.tumNotlar();
    }

    private String harfNotuHesapla(double ortalama) {
        if (ortalama >= 90) return "AA";
        if (ortalama >= 85) return "BA";
        if (ortalama >= 80) return "BB";
        if (ortalama >= 75) return "CB";
        if (ortalama >= 70) return "CC";
        if (ortalama >= 65) return "DC";
        if (ortalama >= 60) return "DD";
        if (ortalama >= 50) return "FD";
        return "FF";
    }
} 