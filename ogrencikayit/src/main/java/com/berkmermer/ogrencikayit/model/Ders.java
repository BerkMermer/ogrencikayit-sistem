package com.berkmermer.ogrencikayit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dersler")
public class Ders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ders_kodu")
    private String dersKodu;
    
    @Column(name = "ders_adi")
    private String dersAdi;
    
    @Column(name = "kontenjan")
    private Integer kontenjan;
    
    @Column(name = "ogretmen_adi")
    private String ogretmenAdi;
    
    @Column(name = "kapasite")
    private Integer kapasite;
    
    @Column(name = "kredi")
    private Integer kredi;
    
    @Column(name = "gun")
    private String gun;
    
    @Column(name = "saat")
    private String saat;
    
    @Column(name = "sinif")
    private String sinif;
    
    @Column(name = "ogretmen_id")
    private Long ogretmenId;
    
    @Column(name = "on_kosul_ders_id")
    private Long onKosulDersId;
    
    @Column(name = "min_ogrenci_sayisi")
    private Integer minOgrenciSayisi;
    
    @Column(name = "durum")
    private String durum;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDersKodu() { return dersKodu; }
    public void setDersKodu(String dersKodu) { this.dersKodu = dersKodu; }
    
    public String getDersAdi() { return dersAdi; }
    public void setDersAdi(String dersAdi) { this.dersAdi = dersAdi; }
    
    public Integer getKontenjan() { return kontenjan; }
    public void setKontenjan(Integer kontenjan) { this.kontenjan = kontenjan; }
    
    public String getOgretmenAdi() { return ogretmenAdi; }
    public void setOgretmenAdi(String ogretmenAdi) { this.ogretmenAdi = ogretmenAdi; }
    
    public Integer getKapasite() { return kapasite; }
    public void setKapasite(Integer kapasite) { this.kapasite = kapasite; }
    
    public Integer getKredi() { return kredi; }
    public void setKredi(Integer kredi) { this.kredi = kredi; }
    
    public String getGun() { return gun; }
    public void setGun(String gun) { this.gun = gun; }
    
    public String getSaat() { return saat; }
    public void setSaat(String saat) { this.saat = saat; }
    
    public String getSinif() { return sinif; }
    public void setSinif(String sinif) { this.sinif = sinif; }
    
    public Long getOgretmenId() { return ogretmenId; }
    public void setOgretmenId(Long ogretmenId) { this.ogretmenId = ogretmenId; }

    public Long getOnKosulDersId() { return onKosulDersId; }
    public void setOnKosulDersId(Long onKosulDersId) { this.onKosulDersId = onKosulDersId; }

    public Integer getMinOgrenciSayisi() { return minOgrenciSayisi; }
    public void setMinOgrenciSayisi(Integer minOgrenciSayisi) { this.minOgrenciSayisi = minOgrenciSayisi; }
    public String getDurum() { return durum; }
    public void setDurum(String durum) { this.durum = durum; }
}
