package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.service.MezuniyetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/mezuniyet")
public class MezuniyetController {
    @Autowired
    private MezuniyetService mezuniyetService;

    @GetMapping("/ogrenci/{ogrenciId}")
    public Map<String, Object> mezuniyetDurumu(@PathVariable Long ogrenciId) {
        return mezuniyetService.mezuniyetDurumu(ogrenciId);
    }

    @GetMapping("/ogrenci/{ogrenciId}/donem/{donem}")
    public Map<String, Object> donemRaporu(@PathVariable Long ogrenciId, @PathVariable String donem) {
        return mezuniyetService.donemRaporu(ogrenciId, donem);
    }
} 