package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.model.Not;
import com.berkmermer.ogrencikayit.service.NotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.berkmermer.ogrencikayit.model.ApiResponse;

@RestController
@RequestMapping("/api/notlar")
public class NotController {
    @Autowired
    private NotService notService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Not>>> tumNotlar(@RequestParam(required = false) String rol) {
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        if (!rol.equals("ADMIN") && !rol.equals("OGRETMEN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            List<Not> notlar = notService.tumNotlar();
            return ResponseEntity.ok(new ApiResponse<>(true, "Tüm notlar getirildi", notlar));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(new ApiResponse<>(false, "Notlar getirilirken hata oluştu: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> notEkle(@RequestBody Not not, @RequestParam(required = false) String rol) {
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        if (!rol.equals("OGRETMEN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            notService.notEkle(not);
            return ResponseEntity.ok(new ApiResponse<>(true, "Not başarıyla eklendi", null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Not eklenirken hata oluştu", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> notGuncelle(@PathVariable Long id, @RequestBody Not not, @RequestParam(required = false) String rol) {
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        if (!rol.equals("OGRETMEN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            not.setId(id);
            notService.notGuncelle(not);
            return ResponseEntity.ok(new ApiResponse<>(true, "Not başarıyla güncellendi", null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Not güncellenirken hata oluştu", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> notSil(@PathVariable Long id, @RequestParam(required = false) String rol) {
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        if (!rol.equals("OGRETMEN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            notService.notSil(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Not başarıyla silindi", null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Not silinirken hata oluştu", null));
        }
    }

    @GetMapping("/ogrenci/{ogrenciId}")
    public ResponseEntity<ApiResponse<List<Not>>> ogrencininNotlari(@PathVariable Long ogrenciId, @RequestParam(required = false) String rol) {
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        if (!rol.equals("OGRENCI") && !rol.equals("OGRETMEN") && !rol.equals("ADMIN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            List<Not> notlar = notService.ogrencininNotlari(ogrenciId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci notları başarıyla getirildi", notlar));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Öğrenci notları getirilirken hata oluştu", null));
        }
    }

    @GetMapping("/ders/{dersId}")
    public ResponseEntity<ApiResponse<List<Not>>> dersinNotlari(@PathVariable Long dersId, @RequestParam(required = false) String rol) {
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Rol parametresi gerekli", null));
        }
        
        if (!rol.equals("OGRETMEN") && !rol.equals("ADMIN")) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        
        try {
            List<Not> notlar = notService.dersinNotlari(dersId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Ders notları başarıyla getirildi", notlar));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(new ApiResponse<>(false, "Ders notları getirilirken hata oluştu", null));
        }
    }
}