package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.model.Ogrenci;
import com.berkmermer.ogrencikayit.dao.OgrenciDao;
import com.berkmermer.ogrencikayit.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ogrenciler")
public class OgrenciController {
    @Autowired
    private OgrenciDao ogrenciDao;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Ogrenci>>> getAllOgrenciler(@RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        // Sadece ADMIN ve OGRETMEN tüm öğrencileri görebilir
        if (!rol.equals("ADMIN") && !rol.equals("OGRETMEN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        List<Ogrenci> ogrenciler = ogrenciDao.findAll();
        
        // Eğer veritabanı boşsa test verileri ekle
        if (ogrenciler.isEmpty()) {
            Ogrenci ogrenci1 = new Ogrenci();
            ogrenci1.setOgrenciNo("2024001");
            ogrenci1.setAd("Ahmet");
            ogrenci1.setSoyad("Yılmaz");
            ogrenci1.setEmail("ahmet.yilmaz@email.com");
            ogrenci1.setTelefon("0555-111-1111");
            ogrenciDao.save(ogrenci1);
            
            Ogrenci ogrenci2 = new Ogrenci();
            ogrenci2.setOgrenciNo("2024002");
            ogrenci2.setAd("Ayşe");
            ogrenci2.setSoyad("Demir");
            ogrenci2.setEmail("ayse.demir@email.com");
            ogrenci2.setTelefon("0555-222-2222");
            ogrenciDao.save(ogrenci2);
            
            Ogrenci ogrenci3 = new Ogrenci();
            ogrenci3.setOgrenciNo("2024003");
            ogrenci3.setAd("Mehmet");
            ogrenci3.setSoyad("Kaya");
            ogrenci3.setEmail("mehmet.kaya@email.com");
            ogrenci3.setTelefon("0555-333-3333");
            ogrenciDao.save(ogrenci3);
            
            ogrenciler = ogrenciDao.findAll();
        }
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenciler başarıyla getirildi", ogrenciler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Ogrenci>> getOgrenciById(@PathVariable Long id, @RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        // Sadece ADMIN ve OGRETMEN öğrenci detaylarını görebilir
        if (!rol.equals("ADMIN") && !rol.equals("OGRETMEN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            Ogrenci ogrenci = ogrenciDao.findById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci başarıyla getirildi", ogrenci));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                .body(new ApiResponse<>(false, "Öğrenci bulunamadı", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createOgrenci(@RequestBody Ogrenci ogrenci, @RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        // Sadece ADMIN öğrenci ekleyebilir
        if (!rol.equals("ADMIN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            ogrenciDao.save(ogrenci);
            return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci başarıyla eklendi", null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Öğrenci eklenirken hata oluştu", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateOgrenci(@PathVariable Long id, @RequestBody Ogrenci yeniOgrenci, @RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        // Sadece ADMIN öğrenci güncelleyebilir
        if (!rol.equals("ADMIN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            yeniOgrenci.setId(id);
            ogrenciDao.update(yeniOgrenci);
            return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci başarıyla güncellendi", null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Öğrenci güncellenirken hata oluştu", null));
        }
    }

    @PatchMapping("/{id}/ad")
    public ResponseEntity<ApiResponse<String>> updateOgrenciAd(@PathVariable Long id, @RequestBody String yeniAd, @RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        // Sadece ADMIN öğrenci güncelleyebilir
        if (!rol.equals("ADMIN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            ogrenciDao.updateAd(id, yeniAd);
            return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci adı başarıyla güncellendi", null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Öğrenci adı güncellenirken hata oluştu", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOgrenci(@PathVariable Long id, @RequestParam(required = false) String rol) {
        // Rol kontrolü
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        // Sadece ADMIN öğrenci silebilir
        if (!rol.equals("ADMIN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            ogrenciDao.delete(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci başarıyla silindi", null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Öğrenci silinirken hata oluştu", null));
        }
    }
}
