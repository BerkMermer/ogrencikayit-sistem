package com.berkmermer.ogrencikayit.model;

public class Ogretmen {
    private Long id;
    private String ad;
    private String soyad;
    private String branş;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }
    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }
    public String getBranş() { return branş; }
    public void setBranş(String branş) { this.branş = branş; }
}
