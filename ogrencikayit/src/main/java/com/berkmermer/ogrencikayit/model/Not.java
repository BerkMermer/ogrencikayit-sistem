package com.berkmermer.ogrencikayit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "notlar")
public class Not {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ogrenci_id")
    private Long ogrenciId;
    
    @Column(name = "ders_id")
    private Long dersId;
    
    @Column(name = "vize")
    private Double vize;
    
    @Column(name = "final_notu")
    private Double finalNotu;
    
    @Column(name = "ortalama")
    private Double ortalama;
    
    @Column(name = "harf_notu")
    private String harfNotu;
    
    @Column(name = "giren_ogretmen_id")
    private Long girenOgretmenId;
    
    @Column(name = "tarih")
    private String tarih;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOgrenciId() { return ogrenciId; }
    public void setOgrenciId(Long ogrenciId) { this.ogrenciId = ogrenciId; }

    public Long getDersId() { return dersId; }
    public void setDersId(Long dersId) { this.dersId = dersId; }

    public Double getVize() { return vize; }
    public void setVize(Double vize) { this.vize = vize; }

    public Double getFinalNotu() { return finalNotu; }
    public void setFinalNotu(Double finalNotu) { this.finalNotu = finalNotu; }

    public Double getOrtalama() { return ortalama; }
    public void setOrtalama(Double ortalama) { this.ortalama = ortalama; }

    public String getHarfNotu() { return harfNotu; }
    public void setHarfNotu(String harfNotu) { this.harfNotu = harfNotu; }

    public Long getGirenOgretmenId() { return girenOgretmenId; }
    public void setGirenOgretmenId(Long girenOgretmenId) { this.girenOgretmenId = girenOgretmenId; }

    public String getTarih() { return tarih; }
    public void setTarih(String tarih) { this.tarih = tarih; }
} 