package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.dao.DersDao;
import com.berkmermer.ogrencikayit.dao.OgrenciDao;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public Map<String, Object> dashboard(@RequestParam String rol) {
        Map<String, Object> result = new HashMap<>();
        
        try {
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
        } catch (Exception e) {
            result.put("hata", "Dashboard yüklenirken hata: " + e.getMessage());
            result.put("status", "error");
        }
        
        return result;
    }
} 