package com.berkmermer.ogrencikayit.service;

import com.berkmermer.ogrencikayit.dao.KullaniciDao;
import com.berkmermer.ogrencikayit.model.Kullanici;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KullaniciService {
    @Autowired
    private KullaniciDao kullaniciDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Kullanici> girisYap(String kullaniciAdi, String sifre) {
        Optional<Kullanici> kullaniciOpt = kullaniciDao.findByKullaniciAdi(kullaniciAdi);
        if (kullaniciOpt.isPresent() && passwordEncoder.matches(sifre, kullaniciOpt.get().getSifre())) {
            return kullaniciOpt;
        }
        return Optional.empty();
    }

    public Optional<Kullanici> findByKullaniciAdi(String kullaniciAdi) {
        return kullaniciDao.findByKullaniciAdi(kullaniciAdi);
    }

    public Kullanici kayitOl(Kullanici kullanici) {
        kullanici.setSifre(passwordEncoder.encode(kullanici.getSifre()));
        kullanici.setAktif(true);
        return kullaniciDao.save(kullanici);
    }

    public Optional<Kullanici> sifreSifirlamaTokenIleBul(String token) {
        return kullaniciDao.findBySifreSifirlamaToken(token);
    }

    public Optional<Kullanici> emailIleBul(String email) {
        return kullaniciDao.findByEmail(email);
    }

    public boolean rolKontrol(Kullanici kullanici, String rol) {
        return kullanici.getRol().equalsIgnoreCase(rol);
    }

    public java.util.List<Kullanici> tumKullanicilar() {
        return kullaniciDao.findAll();
    }

    public Optional<Kullanici> kullaniciIdIleBul(Long id) {
        return kullaniciDao.findById(id);
    }

    public Kullanici kullaniciGuncelle(Long id, Kullanici yeni) {
        Kullanici mevcut = kullaniciDao.findById(id).orElseThrow();
        mevcut.setKullaniciAdi(yeni.getKullaniciAdi());
        mevcut.setEmail(yeni.getEmail());
        if (yeni.getSifre() != null && !yeni.getSifre().isBlank()) {
            mevcut.setSifre(passwordEncoder.encode(yeni.getSifre()));
        }
        mevcut.setRol(yeni.getRol());
        mevcut.setAktif(yeni.isAktif());
        return kullaniciDao.save(mevcut);
    }

    public void kullaniciSil(Long id) {
        kullaniciDao.deleteById(id);
    }
} 