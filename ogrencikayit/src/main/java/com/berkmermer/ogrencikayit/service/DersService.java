package com.berkmermer.ogrencikayit.service;

import com.berkmermer.ogrencikayit.dao.DersDao;
import com.berkmermer.ogrencikayit.dao.KayitDao;
import com.berkmermer.ogrencikayit.model.Ders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DersService {
    @Autowired
    private DersDao dersDao;
    @Autowired
    private KayitDao kayitDao;

    public void dersDurumlariniGuncelle() {
        List<Ders> dersler = dersDao.findAll();
        for (Ders ders : dersler) {
            int kayitliOgrenci = kayitDao.derseKayitliOgrenciSayisi(ders.getId());
            if (ders.getMinOgrenciSayisi() != null && kayitliOgrenci < ders.getMinOgrenciSayisi()) {
                ders.setDurum("KAPALI");
            } else {
                ders.setDurum("ACIK");
            }
            dersDao.update(ders);
        }
    }
} 