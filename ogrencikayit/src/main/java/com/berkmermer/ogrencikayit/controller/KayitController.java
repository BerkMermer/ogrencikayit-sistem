package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.dao.KayitDao;
import com.berkmermer.ogrencikayit.model.Ders;
import com.berkmermer.ogrencikayit.model.Kayit;
import com.berkmermer.ogrencikayit.model.Ogrenci;
import com.berkmermer.ogrencikayit.model.ApiResponse;
import com.berkmermer.ogrencikayit.service.KayitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/kayitlar")
public class KayitController {
    @Autowired
    private KayitDao kayitDao;
    
    @Autowired
    private KayitService kayitService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Kayit>>> tumKayitlar(@RequestParam(required = false) String rol) {
        if (rol == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        if (!"ADMIN".equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Sadece admin tüm kayıtları görebilir!", null));
        }
        
        List<Kayit> kayitlar = kayitDao.tumKayitlar();
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Tüm kayıtlar getirildi", kayitlar));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> ogrenciKayitOl(@RequestBody Kayit kayit, @RequestParam String rol) {
        if (!"OGRENCI".equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Sadece öğrenciler kayıt olabilir!", null));
        }
        
        Long ogrenciId = kayit.getOgrenciId();
        
        String sonuc = kayitService.ogrenciyiDerseKaydet(ogrenciId, kayit.getDersId(), "2024-2025", kayit.getDanismanOgretmenId());
        if (!"Kayıt başarılı!".equals(sonuc)) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, sonuc, null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Kayıt başarıyla oluşturuldu", "Kayıt oluşturuldu"));
    }

    @PostMapping("/onayla")
    public ResponseEntity<ApiResponse> kayitOnayla(@RequestParam Long kayitId, @RequestParam String rol) {
        if (!"OGRETMEN".equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Sadece danışman öğretmenler kayıt onaylayabilir!"));
        }
        kayitService.kayitOnayla(kayitId);
        return ResponseEntity.ok(new ApiResponse(true, "Kayıt onaylandı!"));
    }

    @PostMapping("/reddet")
    public ResponseEntity<ApiResponse> kayitReddet(@RequestParam Long kayitId, @RequestParam String rol) {
        if (!"OGRETMEN".equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Sadece danışman öğretmenler kayıt reddedebilir!"));
        }
        kayitService.kayitReddet(kayitId);
        return ResponseEntity.ok(new ApiResponse(true, "Kayıt reddedildi!"));
    }

    @GetMapping("/ogrenci/{ogrenciId}")
    public ResponseEntity<ApiResponse<List<Kayit>>> ogrenciKayitlariGetir(@PathVariable Long ogrenciId, @RequestParam String rol) {
        if (!"OGRENCI".equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Sadece öğrenciler kendi kayıtlarını görebilir!", null));
        }
        
        List<Kayit> kayitlar = kayitDao.ogrencininDonemKayitlari(ogrenciId, "2024-2025");
        return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci kayıtları getirildi", kayitlar));
    }

    @GetMapping("/ogrenci/{ogrenciId}/onay-bekleyen")
    public List<Kayit> ogrencininOnayBekleyenKayitlari(@PathVariable Long ogrenciId) {
        return kayitDao.ogrencininOnayBekleyenKayitlari(ogrenciId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> ogrenciKayitIptal(@PathVariable Long id, @RequestParam String rol) {
        if (!"OGRENCI".equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Sadece öğrenciler kayıt iptal edebilir!", null));
        }
        
        try {
            kayitDao.kayitSil(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Kayıt başarıyla iptal edildi", "Kayıt iptal edildi"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/ogrenci/{ogrenciId}/dersler")
    public List<Ders> ogrencininDersleri(@PathVariable Long ogrenciId) {
        return kayitService.ogrencininDersleri(ogrenciId);
    }

    @GetMapping("/ders/{dersId}")
    public ResponseEntity<ApiResponse<List<Ogrenci>>> dersKayitlariGetir(@PathVariable Long dersId, @RequestParam String rol) {
        if (!"OGRETMEN".equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Sadece öğretmenler ders kayıtlarını görebilir!", null));
        }
        List<Ogrenci> ogrenciler = kayitDao.derseKayitliOgrenciler(dersId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Ders kayıtları getirildi", ogrenciler));
    }

    @GetMapping("/ders/{dersId}/ogrenciler")
    public List<Ogrenci> derseKayitliOgrenciler(@PathVariable Long dersId) {
        return kayitService.derseKayitliOgrenciler(dersId);
    }



    @GetMapping("/kontrol")
    public ResponseEntity<ApiResponse<Boolean>> kayitKontrol(@RequestParam Long ogrenciId, @RequestParam Long dersId, @RequestParam String rol) {
        if (!"OGRENCI".equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Sadece öğrenciler kayıt kontrolü yapabilir!", null));
        }
        
        boolean kayitliMi = kayitDao.ogrenciDerseKayitliMi(ogrenciId, dersId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Kayıt kontrolü yapıldı", kayitliMi));
    }

    @GetMapping("/ogrenci/{ogrenciId}/kredi")
    public String ogrencininKredisi(@PathVariable Long ogrenciId) {
        int toplamKredi = kayitDao.ogrencininToplamKredisi(ogrenciId);
        return "Öğrenci ID " + ogrenciId + " toplam kredisi: " + toplamKredi + "/30";
    }
}
