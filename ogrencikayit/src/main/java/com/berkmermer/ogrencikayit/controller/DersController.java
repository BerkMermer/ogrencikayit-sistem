package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.model.Ders;
import com.berkmermer.ogrencikayit.dao.DersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dersler")
public class DersController {
    @Autowired
    private DersDao dersDao;

    @GetMapping
    public List<Ders> getAllDersler() {
        return dersDao.findAll();
    }

    @GetMapping("/{id}")
    public Ders getDersById(@PathVariable Long id) {
        return dersDao.findById(id);
    }

    @PostMapping
    public void createDers(@RequestBody Ders ders) {
        dersDao.save(ders);
    }

    @PutMapping("/{id}")
    public void updateDers(@PathVariable Long id, @RequestBody Ders updateDers) {
        updateDers.setId(id);
        dersDao.update(updateDers);
    }

    @DeleteMapping("/{id}")
    public void deleteDers(@PathVariable Long id) {
        dersDao.delete(id);
    }
}
