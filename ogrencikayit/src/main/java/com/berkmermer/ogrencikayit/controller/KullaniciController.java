package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.model.Kullanici;
import com.berkmermer.ogrencikayit.model.ApiResponse;
import com.berkmermer.ogrencikayit.service.KullaniciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/kullanici")
public class KullaniciController {
    @Autowired
    private KullaniciService kullaniciService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Kullanici>> login(@RequestBody LoginRequest request) {
        Optional<Kullanici> kullaniciOpt = kullaniciService.girisYap(request.getKullaniciAdi(), request.getSifre());
        if (kullaniciOpt.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Giriş başarılı", kullaniciOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, "Kullanıcı adı veya şifre yanlış"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Kullanici>> register(@RequestBody RegisterRequest request) {
        Kullanici yeniKullanici = new Kullanici();
        yeniKullanici.setKullaniciAdi(request.getKullaniciAdi());
        yeniKullanici.setSifre(request.getSifre());
        yeniKullanici.setEmail(request.getEmail());
        yeniKullanici.setRol(request.getRol());
        yeniKullanici.setAktif(true);
        Kullanici kayitli = kullaniciService.kayitOl(yeniKullanici);
        return ResponseEntity.ok(new ApiResponse<>(true, "Kayıt başarılı", kayitli));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Optional<Kullanici> kullaniciOpt = kullaniciService.emailIleBul(request.getEmail());
        if (kullaniciOpt.isPresent()) {
            // Basit bir token üretimi (gerçek projede UUID ve e-posta ile gönderim yapılmalı)
            String token = java.util.UUID.randomUUID().toString();
            Kullanici kullanici = kullaniciOpt.get();
            kullanici.setSifreSifirlamaToken(token);
            kullaniciService.kayitOl(kullanici); // güncelle
            return ResponseEntity.ok(new ApiResponse<>(true, "Şifre sıfırlama token'ı üretildi", token));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "E-posta bulunamadı"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        Optional<Kullanici> kullaniciOpt = kullaniciService.sifreSifirlamaTokenIleBul(request.getToken());
        if (kullaniciOpt.isPresent()) {
            Kullanici kullanici = kullaniciOpt.get();
            kullanici.setSifre(request.getYeniSifre());
            kullanici.setSifreSifirlamaToken(null);
            kullaniciService.kayitOl(kullanici); // güncelle
            return ResponseEntity.ok(new ApiResponse<>(true, "Şifre başarıyla güncellendi"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "Geçersiz veya süresi dolmuş token"));
        }
    }

    @GetMapping("/rol/{kullaniciAdi}")
    public ResponseEntity<ApiResponse<String>> rolKontrol(@PathVariable String kullaniciAdi) {
        Optional<Kullanici> kullaniciOpt = kullaniciService.emailIleBul(kullaniciAdi);
        if (kullaniciOpt.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Rol bulundu", kullaniciOpt.get().getRol()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "Kullanıcı bulunamadı"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<java.util.List<Kullanici>>> tumKullanicilar(@RequestParam(required = false) String rol) {
        if (rol == null || !rol.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Kullanıcılar getirildi", kullaniciService.tumKullanicilar()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Kullanici>> kullaniciGuncelle(@PathVariable Long id, @RequestBody Kullanici yeni, @RequestParam(required = false) String rol) {
        if (rol == null || !rol.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        Kullanici guncellenen = kullaniciService.kullaniciGuncelle(id, yeni);
        return ResponseEntity.ok(new ApiResponse<>(true, "Kullanıcı güncellendi", guncellenen));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> kullaniciSil(@PathVariable Long id, @RequestParam(required = false) String rol) {
        if (rol == null || !rol.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "Bu işlem için yetkiniz yok", null));
        }
        kullaniciService.kullaniciSil(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Kullanıcı silindi", null));
    }

    // LoginRequest iç sınıfı veya ayrı bir dosyada olmalı
    public static class LoginRequest {
        private String kullaniciAdi;
        private String sifre;
        public String getKullaniciAdi() { return kullaniciAdi; }
        public void setKullaniciAdi(String kullaniciAdi) { this.kullaniciAdi = kullaniciAdi; }
        public String getSifre() { return sifre; }
        public void setSifre(String sifre) { this.sifre = sifre; }
    }
    public static class RegisterRequest {
        private String kullaniciAdi;
        private String sifre;
        private String email;
        private String rol;
        public String getKullaniciAdi() { return kullaniciAdi; }
        public void setKullaniciAdi(String kullaniciAdi) { this.kullaniciAdi = kullaniciAdi; }
        public String getSifre() { return sifre; }
        public void setSifre(String sifre) { this.sifre = sifre; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }
    public static class ForgotPasswordRequest {
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
    public static class ResetPasswordRequest {
        private String token;
        private String yeniSifre;
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getYeniSifre() { return yeniSifre; }
        public void setYeniSifre(String yeniSifre) { this.yeniSifre = yeniSifre; }
    }
} 