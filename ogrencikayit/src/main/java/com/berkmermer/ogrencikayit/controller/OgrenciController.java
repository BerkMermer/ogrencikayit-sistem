package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.model.Ogrenci;
import com.berkmermer.ogrencikayit.dao.OgrenciDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ogrenciler")
public class OgrenciController {
    @Autowired
    private OgrenciDao ogrenciDao;

    @GetMapping
    public List<Ogrenci> getAllOgrenciler() {
        return ogrenciDao.findAll();
    }

    @GetMapping("/{id}")
    public Ogrenci getOgrenciById(@PathVariable Long id) {
        return ogrenciDao.findById(id);
    }

    @PostMapping
    public void createOgrenci(@RequestBody Ogrenci ogrenci) {
        ogrenciDao.save(ogrenci);
    }

    @PutMapping("/{id}")
    public void updateOgrenci(@PathVariable Long id, @RequestBody Ogrenci yeniOgrenci) {
        yeniOgrenci.setId(id);
        ogrenciDao.update(yeniOgrenci);
    }

    @PatchMapping("/{id}/ad")
    public void updateOgrenciAd(@PathVariable Long id, @RequestBody String yeniAd) {
        ogrenciDao.updateAd(id, yeniAd);
    }

    @DeleteMapping("/{id}")
    public void deleteOgrenci(@PathVariable Long id) {
        ogrenciDao.delete(id);
    }
}
