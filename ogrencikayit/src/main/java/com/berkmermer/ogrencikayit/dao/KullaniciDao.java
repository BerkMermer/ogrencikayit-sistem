package com.berkmermer.ogrencikayit.dao;

import com.berkmermer.ogrencikayit.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KullaniciDao extends JpaRepository<Kullanici, Long> {
    Optional<Kullanici> findByKullaniciAdi(String kullaniciAdi);
    Optional<Kullanici> findByEmail(String email);
    Optional<Kullanici> findBySifreSifirlamaToken(String sifreSifirlamaToken);
} 