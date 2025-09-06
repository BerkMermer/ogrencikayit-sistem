package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.model.Ders;
import com.berkmermer.ogrencikayit.dao.DersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.berkmermer.ogrencikayit.model.ApiResponse;
import com.berkmermer.ogrencikayit.service.DersService;

@RestController
@RequestMapping("/api/dersler")
public class DersController {
    @Autowired
    private DersDao dersDao;
    @Autowired
    private DersService dersService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Ders>>> getAllDersler(@RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        // Sadece ADMIN, OGRETMEN ve OGRENCI dersleri görebilir
        if (!rol.equals("ADMIN") && !rol.equals("OGRETMEN") && !rol.equals("OGRENCI")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        List<Ders> dersler = dersDao.findAll();
        
        // Eğer veritabanı boşsa test verileri ekle
        if (dersler.isEmpty()) {
            Ders ders1 = new Ders();
            ders1.setDersKodu("CS101");
            ders1.setDersAdi("Bilgisayar Programlama");
            ders1.setKontenjan(30);
            ders1.setOgretmenAdi("Dr. Ali Özkan");
            dersDao.save(ders1);
            
            Ders ders2 = new Ders();
            ders2.setDersKodu("CS102");
            ders2.setDersAdi("Veri Yapıları");
            ders2.setKontenjan(25);
            ders2.setOgretmenAdi("Dr. Fatma Demir");
            dersDao.save(ders2);
            
            Ders ders3 = new Ders();
            ders3.setDersKodu("CS103");
            ders3.setDersAdi("Algoritma Analizi");
            ders3.setKontenjan(20);
            ders3.setOgretmenAdi("Dr. Mehmet Kaya");
            dersDao.save(ders3);
            
            dersler = dersDao.findAll();
        }
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Dersler başarıyla getirildi", dersler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Ders>> getDersById(@PathVariable Long id, @RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        // Sadece ADMIN, OGRETMEN ve OGRENCI ders detaylarını görebilir
        if (!rol.equals("ADMIN") && !rol.equals("OGRETMEN") && !rol.equals("OGRENCI")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            Ders ders = dersDao.findById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Ders başarıyla getirildi", ders));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                .body(new ApiResponse<>(false, "Ders bulunamadı", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createDers(@RequestBody Ders ders, @RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        // Sadece ADMIN ve OGRETMEN ders ekleyebilir
        if (!rol.equals("ADMIN") && !rol.equals("OGRETMEN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        try {
            dersDao.save(ders);
            return ResponseEntity.ok(new ApiResponse<>(true, "Ders başarıyla eklendi", null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Ders eklenirken hata oluştu", null));
        }
    }

    @PostMapping("/durum-guncelle")
    public ResponseEntity<ApiResponse<String>> dersDurumlariniGuncelle(@RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        // Sadece ADMIN ders durumlarını güncelleyebilir
        if (!rol.equals("ADMIN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            dersService.dersDurumlariniGuncelle();
            return ResponseEntity.ok(new ApiResponse<>(true, "Tüm derslerin durumu güncellendi", null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Ders durumları güncellenirken hata oluştu", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateDers(@PathVariable Long id, @RequestBody Ders updateDers, @RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        // Sadece ADMIN ve OGRETMEN ders güncelleyebilir
        if (!rol.equals("ADMIN") && !rol.equals("OGRETMEN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        try {
            updateDers.setId(id);
            dersDao.update(updateDers);
            return ResponseEntity.ok(new ApiResponse<>(true, "Ders başarıyla güncellendi", null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Ders güncellenirken hata oluştu", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDers(@PathVariable Long id, @RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        // Sadece ADMIN ve OGRETMEN ders silebilir
        if (!rol.equals("ADMIN") && !rol.equals("OGRETMEN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        try {
            dersDao.delete(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Ders başarıyla silindi", null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Ders silinirken hata oluştu", null));
        }
    }
}
