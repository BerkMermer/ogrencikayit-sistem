package com.berkmermer.ogrencikayit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ogrenciler")
public class Ogrenci {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ogrenci_no")
    private String ogrenciNo;
    
    @Column(name = "ad")
    private String ad;
    
    @Column(name = "soyad")
    private String soyad;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "telefon")
    private String telefon;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getOgrenciNo() { return ogrenciNo; }
    public void setOgrenciNo(String ogrenciNo) { this.ogrenciNo = ogrenciNo; }
    
    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }
    
    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
}
