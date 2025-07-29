package com.berkmermer.ogrencikayit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "kayitlar")
public class Kayit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ogrenci_id")
    private Long ogrenciId;

    @Column(name = "ders_id")
    private Long dersId;

    @Column(name = "donem", nullable = true)
    private String donem;
    
    @Column(name = "durum", nullable = true)
    private String onayDurumu;
    
    @Column(name = "danisman_ogretmen_id")
    private Long danismanOgretmenId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOgrenciId() {
        return ogrenciId;
    }

    public void setOgrenciId(Long ogrenciId) {
        this.ogrenciId = ogrenciId;
    }

    public Long getDersId() {
        return dersId;
    }

    public void setDersId(Long dersId) {
        this.dersId = dersId;
    }

    public String getDonem() { return donem; }
    public void setDonem(String donem) { this.donem = donem; }

    public String getOnayDurumu() { return onayDurumu; }
    public void setOnayDurumu(String onayDurumu) { this.onayDurumu = onayDurumu; }
    public Long getDanismanOgretmenId() { return danismanOgretmenId; }
    public void setDanismanOgretmenId(Long danismanOgretmenId) { this.danismanOgretmenId = danismanOgretmenId; }
}
