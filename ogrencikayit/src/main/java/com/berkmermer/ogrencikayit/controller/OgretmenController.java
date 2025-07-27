package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.dao.OgretmenDao;
import com.berkmermer.ogrencikayit.model.Ogretmen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ogretmen")
public class OgretmenController {
    @Autowired
    private OgretmenDao ogretmenDao;

    @PostMapping
    public String ogretmenEkle(@RequestBody Ogretmen ogretmen) {
        ogretmenDao.ogretmenEkle(ogretmen);
        return "Öğretmen eklendi!";
    }

    @GetMapping
    public List<Ogretmen> tumOgretmenler() {
        return ogretmenDao.tumOgretmenler();
    }
} 