package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.model.Ders;
import com.berkmermer.ogrencikayit.service.KayitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/program")
public class ProgramController {
    @Autowired
    private KayitService kayitService;

    @GetMapping("/ogrenci/{ogrenciId}")
    public List<Ders> ogrencininProgrami(@PathVariable Long ogrenciId) {
        return kayitService.ogrencininDersleri(ogrenciId);
    }
} 