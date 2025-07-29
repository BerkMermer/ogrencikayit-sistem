package com.berkmermer.ogrencikayit.service;

import com.berkmermer.ogrencikayit.dao.NotDao;
import com.berkmermer.ogrencikayit.dao.DersDao;
import com.berkmermer.ogrencikayit.dao.KayitDao;
import com.berkmermer.ogrencikayit.model.Ders;
import com.berkmermer.ogrencikayit.model.Not;
import com.berkmermer.ogrencikayit.model.Kayit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MezuniyetService {
    private static final List<Long> ZORUNLU_DERSLER = Arrays.asList(1L, 2L, 3L, 4L, 5L);
    private static final int MEZUNIYET_KREDISI = 240;
    private static final int KREDI_PER_SINIF = 30;

    @Autowired
    private NotDao notDao;
    @Autowired
    private DersDao dersDao;
    @Autowired
    private KayitDao kayitDao;

    public Map<String, Object> mezuniyetDurumu(Long ogrenciId) {
        Map<String, Object> result = new HashMap<>();
        List<Not> notlar = notDao.ogrencininNotlari(ogrenciId);
        List<Long> gectigiDersler = notlar.stream()
            .filter(n -> !"FF".equals(n.getHarfNotu()))
            .map(Not::getDersId)
            .collect(Collectors.toList());
        int toplamKredi = notlar.stream()
            .filter(n -> !"FF".equals(n.getHarfNotu()))
            .map(n -> {
                Ders d = dersDao.findById(n.getDersId());
                return d != null && d.getKredi() != null ? d.getKredi() : 0;
            })
            .reduce(0, Integer::sum);
        List<Long> eksikDersIdler = ZORUNLU_DERSLER.stream()
            .filter(dersId -> !gectigiDersler.contains(dersId))
            .collect(Collectors.toList());
        List<Ders> eksikDersler = eksikDersIdler.stream()
            .map(dersDao::findById)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        int sinif = (toplamKredi / KREDI_PER_SINIF) + 1;
        boolean mezunOlabilir = eksikDersler.isEmpty() && toplamKredi >= MEZUNIYET_KREDISI;
        result.put("mezunOlabilir", mezunOlabilir);
        result.put("eksikDersler", eksikDersler);
        result.put("eksikKredi", Math.max(0, MEZUNIYET_KREDISI - toplamKredi));
        result.put("sinif", sinif);
        result.put("toplamKredi", toplamKredi);
        return result;
    }

    public Map<String, Object> donemRaporu(Long ogrenciId, String donem) {
        Map<String, Object> result = new HashMap<>();
        List<Kayit> donemKayitlari = kayitDao.ogrencininDonemKayitlari(ogrenciId, donem);
        List<Map<String, Object>> dersNotList = new ArrayList<>();
        int toplamKredi = 0;
        double toplamOrtalama = 0;
        int dersSayisi = 0;
        for (Kayit kayit : donemKayitlari) {
            Ders ders = dersDao.findById(kayit.getDersId());
            Not not = notDao.ogrencininNotlari(ogrenciId).stream()
                .filter(n -> n.getDersId().equals(kayit.getDersId()))
                .findFirst().orElse(null);
            Map<String, Object> dersNot = new HashMap<>();
            dersNot.put("ders", ders);
            dersNot.put("not", not);
            dersNotList.add(dersNot);
            if (ders != null && ders.getKredi() != null) {
                toplamKredi += ders.getKredi();
            }
            if (not != null && not.getOrtalama() != null) {
                toplamOrtalama += not.getOrtalama();
                dersSayisi++;
            }
        }
        double donemOrtalamasi = dersSayisi > 0 ? toplamOrtalama / dersSayisi : 0;
        result.put("derslerVeNotlar", dersNotList);
        result.put("toplamKredi", toplamKredi);
        result.put("donemOrtalamasi", donemOrtalamasi);
        return result;
    }
} 