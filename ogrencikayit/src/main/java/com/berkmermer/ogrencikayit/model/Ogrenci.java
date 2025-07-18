package com.berkmermer.ogrencikayit.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ogrenci")
public class Ogrenci {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ad", nullable = false)
    private String ad;
    
    @Column(name = "soyad", nullable = false)
    private String soyad;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ogrenci_ders",
        joinColumns = @JoinColumn(name = "ogrenci_id"),
        inverseJoinColumns = @JoinColumn(name = "ders_id")
    )
    private List<Ders> dersler;

    // Constructors
    public Ogrenci() {}
    
    public Ogrenci(String ad, String soyad) {
        this.ad = ad;
        this.soyad = soyad;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }
    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }
    public List<Ders> getDersler() { return dersler; }
    public void setDersler(List<Ders> dersler) { this.dersler = dersler; }
}
