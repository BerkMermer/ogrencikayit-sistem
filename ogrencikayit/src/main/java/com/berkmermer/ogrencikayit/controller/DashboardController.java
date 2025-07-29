package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.dao.DersDao;
import com.berkmermer.ogrencikayit.dao.OgrenciDao;
import com.berkmermer.ogrencikayit.dao.KullaniciDao;
import com.berkmermer.ogrencikayit.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private DersDao dersDao;
    @Autowired
    private OgrenciDao ogrenciDao;
    @Autowired
    private KullaniciDao kullaniciDao;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats(@RequestParam String rol) {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            if ("ADMIN".equals(rol)) {
                stats.put("toplamOgrenci", ogrenciDao.findAll().size());
                stats.put("toplamOgretmen", kullaniciDao.findByRol("OGRETMEN").size());
                stats.put("toplamDers", dersDao.findAll().size());
                stats.put("toplamKullanici", kullaniciDao.findAll().size());
            } else if ("OGRETMEN".equals(rol)) {
                stats.put("toplamOgrenci", ogrenciDao.findAll().size());
                stats.put("toplamDers", dersDao.findAll().size());
                stats.put("dersler", dersDao.findAll());
            } else if ("OGRENCI".equals(rol)) {
                stats.put("toplamDers", dersDao.findAll().size());
                stats.put("kayitliDersler", dersDao.findAll()); // Bu daha sonra öğrencinin kayıtlı dersleri olacak
            }
            
            return ResponseEntity.ok(new ApiResponse<>(true, "İstatistikler başarıyla yüklendi", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "İstatistikler yüklenirken hata: " + e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> dashboard(@RequestParam String rol) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            if ("ADMIN".equals(rol)) {
                result.put("toplamOgrenci", ogrenciDao.findAll().size());
                result.put("toplamDers", dersDao.findAll().size());
                result.put("mesaj", "Admin dashboard");
                result.put("status", "success");
            } else if ("OGRETMEN".equals(rol)) {
                result.put("dersler", dersDao.findAll());
                result.put("mesaj", "Öğretmen dashboard");
                result.put("status", "success");
            } else {
                result.put("hata", "Geçersiz rol");
                result.put("status", "error");
            }
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard başarıyla yüklendi", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Dashboard yüklenirken hata: " + e.getMessage(), null));
        }
    }
} 