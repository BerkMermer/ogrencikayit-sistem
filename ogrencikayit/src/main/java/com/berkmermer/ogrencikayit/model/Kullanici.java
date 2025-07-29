package com.berkmermer.ogrencikayit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Kullanici {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "kullanici_adi", unique = true, nullable = false)
    private String kullaniciAdi;

    @Column(name = "sifre", nullable = false)
    private String sifre;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "ad")
    private String ad;

    @Column(name = "soyad")
    private String soyad;

    @Column(name = "rol", nullable = false)
    private String rol;

    @Column(name = "aktif", nullable = false)
    private boolean aktif = true;

    @Column(name = "sifre_sifirlama_token")
    private String sifreSifirlamaToken;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public String getSifreSifirlamaToken() { return sifreSifirlamaToken; }
    public void setSifreSifirlamaToken(String sifreSifirlamaToken) { this.sifreSifirlamaToken = sifreSifirlamaToken; }
} 