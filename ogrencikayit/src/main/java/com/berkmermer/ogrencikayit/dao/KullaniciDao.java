package com.berkmermer.ogrencikayit.dao;

import com.berkmermer.ogrencikayit.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface KullaniciDao extends JpaRepository<Kullanici, Long> {
    Optional<Kullanici> findByKullaniciAdi(String kullaniciAdi);
    Optional<Kullanici> findByEmail(String email);
    Optional<Kullanici> findBySifreSifirlamaToken(String sifreSifirlamaToken);
    java.util.List<Kullanici> findByRol(String rol);
} 