package com.berkmermer.ogrencikayit.controller;

import com.berkmermer.ogrencikayit.model.Kullanici;
import com.berkmermer.ogrencikayit.model.ApiResponse;
import com.berkmermer.ogrencikayit.service.KullaniciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        try {
        Kullanici yeniKullanici = new Kullanici();
        yeniKullanici.setKullaniciAdi(request.getKullaniciAdi());
        yeniKullanici.setSifre(request.getSifre());
        yeniKullanici.setEmail(request.getEmail());
            yeniKullanici.setAd(request.getAd());
            yeniKullanici.setSoyad(request.getSoyad());
        // Eğer rol null veya boşsa, otomatik olarak 'OGRENCI' ata
        if (request.getRol() == null || request.getRol().isBlank()) {
            yeniKullanici.setRol("OGRENCI");
        } else {
            yeniKullanici.setRol(request.getRol());
        }
        yeniKullanici.setAktif(true);
        Kullanici kayitli = kullaniciService.kayitOl(yeniKullanici);
        return ResponseEntity.ok(new ApiResponse<>(true, "Kayıt başarılı", kayitli));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Kayıt sırasında hata: " + e.getMessage()));
        }
    }

    @PostMapping("/create-admin")
    public ResponseEntity<ApiResponse<Kullanici>> createAdmin() {
        try {
            // Admin kullanıcısının zaten var olup olmadığını kontrol et
            Optional<Kullanici> existingAdmin = kullaniciService.findByKullaniciAdi("admin");
            if (existingAdmin.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Admin kullanıcısı zaten mevcut", existingAdmin.get()));
            }
            
            Kullanici adminKullanici = new Kullanici();
            adminKullanici.setKullaniciAdi("admin");
            adminKullanici.setSifre("admin123");
            adminKullanici.setEmail("admin@mail.com");
            adminKullanici.setAd("Admin");
            adminKullanici.setSoyad("User");
            adminKullanici.setRol("ADMIN");
            adminKullanici.setAktif(true);
            
            Kullanici kayitli = kullaniciService.kayitOl(adminKullanici);
            return ResponseEntity.ok(new ApiResponse<>(true, "Admin kullanıcısı oluşturuldu", kayitli));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Admin oluşturma hatası: " + e.getMessage()));
        }
    }

    @PostMapping("/create-teacher")
    public ResponseEntity<ApiResponse<Kullanici>> createTeacher() {
        try {
            // Öğretmen kullanıcısının zaten var olup olmadığını kontrol et
            Optional<Kullanici> existingTeacher = kullaniciService.findByKullaniciAdi("ogretmen1");
            if (existingTeacher.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Öğretmen kullanıcısı zaten mevcut", existingTeacher.get()));
            }
            
            Kullanici teacherKullanici = new Kullanici();
            teacherKullanici.setKullaniciAdi("ogretmen1");
            teacherKullanici.setSifre("ogretmen123");
            teacherKullanici.setEmail("ogretmen1@mail.com");
            teacherKullanici.setAd("Öğretmen");
            teacherKullanici.setSoyad("User");
            teacherKullanici.setRol("OGRETMEN");
            teacherKullanici.setAktif(true);
            
            Kullanici kayitli = kullaniciService.kayitOl(teacherKullanici);
            return ResponseEntity.ok(new ApiResponse<>(true, "Öğretmen kullanıcısı oluşturuldu", kayitli));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Öğretmen oluşturma hatası: " + e.getMessage()));
        }
    }

    @PostMapping("/create-student")
    public ResponseEntity<ApiResponse<Kullanici>> createStudent() {
        try {
            // Öğrenci kullanıcısının zaten var olup olmadığını kontrol et
            Optional<Kullanici> existingStudent = kullaniciService.findByKullaniciAdi("ogrenci1");
            if (existingStudent.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci kullanıcısı zaten mevcut", existingStudent.get()));
            }
            
            Kullanici studentKullanici = new Kullanici();
            studentKullanici.setKullaniciAdi("ogrenci1");
            studentKullanici.setSifre("ogrenci123");
            studentKullanici.setEmail("ogrenci1@mail.com");
            studentKullanici.setAd("Öğrenci");
            studentKullanici.setSoyad("User");
            studentKullanici.setRol("OGRENCI");
            studentKullanici.setAktif(true);
            
            Kullanici kayitli = kullaniciService.kayitOl(studentKullanici);
            return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci kullanıcısı oluşturuldu", kayitli));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Öğrenci oluşturma hatası: " + e.getMessage()));
        }
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
        try {
        Kullanici guncellenen = kullaniciService.kullaniciGuncelle(id, yeni);
        return ResponseEntity.ok(new ApiResponse<>(true, "Kullanıcı güncellendi", guncellenen));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Güncelleme sırasında hata: " + e.getMessage()));
        }
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
        private String ad;
        private String soyad;
        private String rol;
        public String getKullaniciAdi() { return kullaniciAdi; }
        public void setKullaniciAdi(String kullaniciAdi) { this.kullaniciAdi = kullaniciAdi; }
        public String getSifre() { return sifre; }
        public void setSifre(String sifre) { this.sifre = sifre; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getAd() { return ad; }
        public void setAd(String ad) { this.ad = ad; }
        public String getSoyad() { return soyad; }
        public void setSoyad(String soyad) { this.soyad = soyad; }
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